package com.springbatch.simplepartitionerlocal.reader;

import com.springbatch.simplepartitionerlocal.dominio.DadosBancarios;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ArquivoDadosBancariosPartitionReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<DadosBancarios> dadosBancariosPartitionReader(
        @Value("#{stepExecutionContext[file]}") final Resource resource) {
        return new FlatFileItemReaderBuilder<DadosBancarios>()
            .name("dadosBancariosReader")
            .resource(resource)
            .delimited()
            .names("pessoaId", "agencia", "conta", "banco", "id")
            .addComment("--")
            .targetType(DadosBancarios.class)
            .build();
    }

}
