package com.springbatch.simplepartitionerlocal.step;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DividirArquivoPessoaStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    public DividirArquivoPessoaStepConfig(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step dividirArquivoPessoaStep(
        @Qualifier("arquivoPessoaReader") FlatFileItemReader<Pessoa> pessoaReader,
        MultiResourceItemWriter<Pessoa> pessoaWriter) {
        return stepBuilderFactory.get("dividirArquivoPessoaStep")
            .<Pessoa, Pessoa>chunk(2000)
            .reader(pessoaReader)
            .writer(pessoaWriter)
            .stream(pessoaWriter)
            .build();
    }

}
