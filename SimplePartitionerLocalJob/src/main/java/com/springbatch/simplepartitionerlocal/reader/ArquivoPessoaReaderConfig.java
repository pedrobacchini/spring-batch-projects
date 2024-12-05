package com.springbatch.simplepartitionerlocal.reader;

import java.util.Date;

import com.springbatch.simplepartitionerlocal.config.ArquivoPartitioner;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;

@Configuration
public class ArquivoPessoaReaderConfig {

    @Bean
    @StepScope
    public CustomArquivoReader<Pessoa> arquivoPessoaReader(
        @Value("#{stepExecutionContext[particao]}") Integer particao,
        ArquivoPartitioner partitioner) {
        return new CustomArquivoReader<>(
            arquivoPessoaReader(partitioner.calcularPrimeiroItemLeitura(particao)),
            partitioner.getItemsLimit());
    }

    public FlatFileItemReader<Pessoa> arquivoPessoaReader(int currentItemCount) {
        return new FlatFileItemReaderBuilder<Pessoa>()
            .name("arquivoPessoaReader")
            .resource(new FileSystemResource("files/pessoas.csv"))
            .delimited()
            .names("nome", "email", "dataNascimento", "idade", "id")
            .addComment("--")
            .currentItemCount(currentItemCount)
            .fieldSetMapper(fieldSetMapper())
            .build();
    }

    private FieldSetMapper<Pessoa> fieldSetMapper() {
        return fieldSet -> {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(fieldSet.readString("nome"));
            pessoa.setEmail(fieldSet.readString("email"));
            pessoa.setDataNascimento(new Date(fieldSet.readDate("dataNascimento", "yyyy-MM-dd HH:mm:ss").getTime()));
            pessoa.setIdade(fieldSet.readInt("idade"));
            pessoa.setId(fieldSet.readInt("id"));
            return pessoa;
        };
    }

}
