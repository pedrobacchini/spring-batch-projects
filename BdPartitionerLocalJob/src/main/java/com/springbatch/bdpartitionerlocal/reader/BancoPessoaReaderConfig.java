package com.springbatch.bdpartitionerlocal.reader;

import com.springbatch.bdpartitionerlocal.dominio.Pessoa;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class BancoPessoaReaderConfig {

    @Bean
    @StepScope
    public JdbcPagingItemReader<Pessoa> pessoaReader(
        @Qualifier("appDataSource") DataSource dataSource,
        @Qualifier("queryProviderPessoa") PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<Pessoa>()
            .name("pessoaReader")
            .dataSource(dataSource)
            .queryProvider(queryProvider)
            .pageSize(2000)
            .rowMapper(new BeanPropertyRowMapper<>(Pessoa.class))
            .build();
    }

    @Bean
    @StepScope
    public SqlPagingQueryProviderFactoryBean queryProviderPessoa(
        @Value("#{stepExecutionContext['minValue']}") Long minValue,
        @Value("#{stepExecutionContext['maxValue']}") Long maxValue,
        @Qualifier("appDataSource") DataSource dataSource) {
        final SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("from pessoa_origem");
        queryProvider.setWhereClause("where id >= "+ minValue + " and id <= " + maxValue);
        queryProvider.setSortKey("id");
        queryProvider.setDataSource(dataSource);
        return queryProvider;
    }

}
