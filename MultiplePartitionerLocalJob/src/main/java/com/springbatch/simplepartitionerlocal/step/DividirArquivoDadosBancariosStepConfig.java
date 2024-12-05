package com.springbatch.simplepartitionerlocal.step;

import com.springbatch.simplepartitionerlocal.dominio.DadosBancarios;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DividirArquivoDadosBancariosStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    public DividirArquivoDadosBancariosStepConfig(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step dividirArquivoDadosBancariosStep(
        @Qualifier("dadosBancariosReader") FlatFileItemReader<DadosBancarios> dadosBancariosReader,
        MultiResourceItemWriter<DadosBancarios> dadosBancariosWriter) {
        return stepBuilderFactory.get("dividirArquivoDadosBancariosStep")
            .<DadosBancarios, DadosBancarios>chunk(2000)
            .reader(dadosBancariosReader)
            .writer(dadosBancariosWriter)
            .stream(dadosBancariosWriter)
            .build();
    }
}
