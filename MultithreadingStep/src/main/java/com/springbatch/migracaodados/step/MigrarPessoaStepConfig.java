package com.springbatch.migracaodados.step;

import com.springbatch.migracaodados.dominio.Pessoa;
import com.springbatch.migracaodados.listener.ChunkLogListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MigrarPessoaStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManagerApp;

    @Bean
    public Step migrarPessoaStep(
        ItemReader<Pessoa> arquivoPessoaReader,
        ClassifierCompositeItemWriter<Pessoa> pessoaClassifierWriter,
        AsyncItemProcessor<Pessoa, Pessoa> pessoaProcessor,
        AsyncItemWriter<Pessoa> arquivoPessoasInvalidasWriter) {
        return ((SimpleStepBuilder<Pessoa, Pessoa>) stepBuilderFactory
            .get("migrarPessoaStep")
            .<Pessoa, Pessoa>chunk(1000)
            .reader(arquivoPessoaReader)
            .writer(pessoaClassifierWriter)
            .processor((ItemProcessor) pessoaProcessor)
            .stream(arquivoPessoasInvalidasWriter)
            .transactionManager(transactionManagerApp)
            .listener(new ChunkLogListener()))
            .build();
    }

}
