package com.springbatch.bdremotepartitioner.job;

import com.springbatch.bdremotepartitioner.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.expression.FunctionExpression;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.util.function.Function;

import static com.springbatch.bdremotepartitioner.Constants.WORKER_DADOS_BANCARIOS_STEP_NAME;
import static com.springbatch.bdremotepartitioner.Constants.WORKER_PESSOA_STEP_NAME;

@Profile("manager")
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class ManagerConfig {

    private static final int GRID_SIZE = 2;

    private final JobBuilderFactory jobBuilderFactory;
    private final RemotePartitioningManagerStepBuilderFactory remotePartitioningManagerStepBuilderFactory;
    private final KafkaTemplate kafkaTemplate;

    public ManagerConfig(
        final JobBuilderFactory jobBuilderFactory,
        final RemotePartitioningManagerStepBuilderFactory remotePartitioningManagerStepBuilderFactory,
        final KafkaTemplate kafkaTemplate) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.remotePartitioningManagerStepBuilderFactory = remotePartitioningManagerStepBuilderFactory;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public Job remotePartitioningJob(
        @Qualifier("migrarPessoaStep") Step migrarPessoaStep,
        @Qualifier("migrarDadosBancariosStep") Step migrarDadosBancariosStep) {
        return jobBuilderFactory.get("remotePartitioningJob")
            .start(migrarPessoaStep)
            .next(migrarDadosBancariosStep)
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step migrarPessoaStep(
        @Qualifier("pessoaPartitioner") Partitioner pessoaPartitioner) {
        return remotePartitioningManagerStepBuilderFactory.get("migrarPessoaStep")
            .partitioner(WORKER_PESSOA_STEP_NAME, pessoaPartitioner)
            .gridSize(GRID_SIZE)
            .outputChannel(outboundRequests())
            .build();
    }

    @Bean
    public Step migrarDadosBancariosStep(
        @Qualifier("dadosBancariosPartitioner") Partitioner dadosBancariosPartitioner) {
        return remotePartitioningManagerStepBuilderFactory.get("migrarDadosBancariosStep")
            .partitioner(WORKER_DADOS_BANCARIOS_STEP_NAME, dadosBancariosPartitioner)
            .gridSize(GRID_SIZE)
            .outputChannel(outboundRequests())
            .build();
    }

    @Bean
    public DirectChannel outboundRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler messageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        messageHandler.setTopicExpression(new LiteralExpression(Constants.TOPIC_NAME));
        Function<Message<?>, Long> partitionIdFn = (m) -> {
            StepExecutionRequest executionRequest = (StepExecutionRequest) m.getPayload();
            return executionRequest.getStepExecutionId() % Constants.TOPIC_PARTITION_COUNT;
        };
        messageHandler.setPartitionIdExpression(new FunctionExpression<>(partitionIdFn));
        return IntegrationFlows
            .from(outboundRequests())
            .log()
            .handle(messageHandler)
            .get();
    }

}
