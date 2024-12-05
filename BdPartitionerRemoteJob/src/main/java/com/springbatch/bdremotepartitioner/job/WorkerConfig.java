package com.springbatch.bdremotepartitioner.job;

import com.springbatch.bdremotepartitioner.Constants;
import com.springbatch.bdremotepartitioner.dominio.DadosBancarios;
import com.springbatch.bdremotepartitioner.dominio.Pessoa;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import static com.springbatch.bdremotepartitioner.Constants.WORKER_DADOS_BANCARIOS_STEP_NAME;
import static com.springbatch.bdremotepartitioner.Constants.WORKER_PESSOA_STEP_NAME;

@Profile("worker")
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class WorkerConfig {

    private final RemotePartitioningWorkerStepBuilderFactory remotePartitioningWorkerStepBuilderFactory;
    private final PlatformTransactionManager platformTransactionManager;

    public WorkerConfig(
        final RemotePartitioningWorkerStepBuilderFactory remotePartitioningWorkerStepBuilderFactory,
        @Qualifier("transactionManagerApp") final PlatformTransactionManager platformTransactionManager) {
        this.remotePartitioningWorkerStepBuilderFactory = remotePartitioningWorkerStepBuilderFactory;
        this.platformTransactionManager = platformTransactionManager;
    }


    @Bean
    public Step migrarPessoaStep(
        JdbcPagingItemReader<Pessoa> pessoaReader,
        JdbcBatchItemWriter<Pessoa> pessoaWriter) {
        return remotePartitioningWorkerStepBuilderFactory
            .get(WORKER_PESSOA_STEP_NAME)
            .inputChannel(inboundRequests())
            .<Pessoa, Pessoa>chunk(10000)
            .reader(pessoaReader)
            .writer(pessoaWriter)
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean
    public Step migrarDadosBancariosStep(
        JdbcPagingItemReader<DadosBancarios> dadosBancariosReader,
        JdbcBatchItemWriter<DadosBancarios> dadosBancariosWriter) {
        return remotePartitioningWorkerStepBuilderFactory
            .get(WORKER_DADOS_BANCARIOS_STEP_NAME)
            .inputChannel(inboundRequests())
            .<DadosBancarios, DadosBancarios>chunk(10000)
            .reader(dadosBancariosReader)
            .writer(dadosBancariosWriter)
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean
    public DirectChannel inboundRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConsumerFactory<String, String> cf) {
        return IntegrationFlows
            .from(Kafka.messageDrivenChannelAdapter(cf, Constants.TOPIC_NAME))
            .channel(inboundRequests())
            .get();
    }

}
