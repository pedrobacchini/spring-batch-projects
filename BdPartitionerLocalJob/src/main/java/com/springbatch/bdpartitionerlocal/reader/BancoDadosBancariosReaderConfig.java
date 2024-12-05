package com.springbatch.bdpartitionerlocal.reader;

import com.springbatch.bdpartitionerlocal.dominio.DadosBancarios;
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
public class BancoDadosBancariosReaderConfig {

  @Bean
  @StepScope
  public JdbcPagingItemReader<DadosBancarios> dadosBancariosReader(
      @Qualifier("appDataSource") DataSource dataSource,
      @Qualifier("queryProviderPessoa") PagingQueryProvider queryProvider) {
    return new JdbcPagingItemReaderBuilder<DadosBancarios>()
        .name("dadosBancariosReader")
        .dataSource(dataSource)
        .queryProvider(queryProvider)
        .pageSize(2000)
        .rowMapper(new BeanPropertyRowMapper<>(DadosBancarios.class))
        .build();
  }

  @Bean
  @StepScope
  public SqlPagingQueryProviderFactoryBean queryProviderDadosBancarios(
      @Value("#{stepExecutionContext['minValue']}") Long minValue,
      @Value("#{stepExecutionContext['maxValue']}") Long maxValue,
      @Qualifier("appDataSource") DataSource dataSource) {
    final SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
    queryProvider.setSelectClause("*");
    queryProvider.setFromClause("from dados_bancarios_origem");
    queryProvider.setWhereClause("where id >= "+ minValue + " and id <= " + maxValue);
    queryProvider.setSortKey("id");
    queryProvider.setDataSource(dataSource);
    return queryProvider;
  }
}
