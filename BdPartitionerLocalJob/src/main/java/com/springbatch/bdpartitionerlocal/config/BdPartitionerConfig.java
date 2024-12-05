package com.springbatch.bdpartitionerlocal.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BdPartitionerConfig {

    @Bean
    public ColumnRangePartitioner pessoaPartitioner(@Qualifier("appDataSource") DataSource dataSource) {
        ColumnRangePartitioner pessoaPartitioner = new ColumnRangePartitioner();
        pessoaPartitioner.setTable("pessoa_origem");
        pessoaPartitioner.setColumn("id");
        pessoaPartitioner.setDataSource(dataSource);
        return pessoaPartitioner;
    }

    @Bean
    public ColumnRangePartitioner dadosBancariosPartitioner(@Qualifier("appDataSource") DataSource dataSource) {
        ColumnRangePartitioner pessoaPartitioner = new ColumnRangePartitioner();
        pessoaPartitioner.setTable("dados_bancarios_origem");
        pessoaPartitioner.setColumn("id");
        pessoaPartitioner.setDataSource(dataSource);
        return pessoaPartitioner;
    }

}
