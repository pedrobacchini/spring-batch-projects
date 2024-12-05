package com.springbatch.simplepartitionerlocal.reader;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ArquivoPessoaReaderConfig {

    @Bean
    public FlatFileItemReader<Pessoa> arquivoPessoaReader() {
        return new FlatFileItemReaderBuilder<Pessoa>()
            .name("arquivoPessoaReader")
            .resource(new FileSystemResource("files/pessoas.csv"))
            .delimited()
            .names("nome", "email", "dataNascimentoStr", "idade", "id")
            .addComment("--")
            .targetType(Pessoa.class)
            .build();
    }

}
