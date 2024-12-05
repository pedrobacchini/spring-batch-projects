package com.springbatch.simplepartitionerlocal.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MultiResourcePartitionerConfig {

    @Bean
    @StepScope
    public MultiResourcePartitioner pessoaPartitioner(
        @Value("file:files/pessoas-temp*") Resource[] resources) {
        final MultiResourcePartitioner multiResourcePartitioner = new MultiResourcePartitioner();
        multiResourcePartitioner.setKeyName("file");
        multiResourcePartitioner.setResources(resources);
        return multiResourcePartitioner;
    }

    @Bean
    @StepScope
    public MultiResourcePartitioner dadosBancariosPartitioner(
        @Value("file:files/dados-bancarios-temp*") Resource[] resources) {
        final MultiResourcePartitioner multiResourcePartitioner = new MultiResourcePartitioner();
        multiResourcePartitioner.setKeyName("file");
        multiResourcePartitioner.setResources(resources);
        return multiResourcePartitioner;
    }

}
