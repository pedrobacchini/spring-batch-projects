package com.springbatch.simplepartitionerlocal.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class SimplePartitionerJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job simplePartitionerJob(
        @Qualifier("migrarPessoaManager") Step migrarPessoaStep,
        @Qualifier("migrarDadosBancariosManager") Step migrarDadosBancariosStep) {
        return jobBuilderFactory.get("simplePartitionerJob")
            .start(dividirArquivoFlow(null, null))
            .next(migrarPessoaStep)
            .next(migrarDadosBancariosStep)
            .end()
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Flow dividirArquivoFlow(
        @Qualifier("dividirArquivoPessoaStep") final Step dividirArquivoPessoaStep,
        @Qualifier("dividirArquivoDadosBancariosStep") final Step dividirArquivoDadosBancariosStep) {
        return new FlowBuilder<Flow>("dividirArquivoFlow")
            .start(dividirArquivoPessoaStep)
            .next(dividirArquivoDadosBancariosStep)
            .build();
    }

}
