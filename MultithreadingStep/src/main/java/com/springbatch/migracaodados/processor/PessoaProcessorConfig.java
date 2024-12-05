package com.springbatch.migracaodados.processor;

import com.springbatch.migracaodados.dominio.Pessoa;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class PessoaProcessorConfig {
    private static final RestTemplate restTemplate = new RestTemplate();
    @Bean
    public AsyncItemProcessor<Pessoa, Pessoa> asyncItemProcessor() {
        final AsyncItemProcessor<Pessoa, Pessoa> pessoaPessoaAsyncItemProcessor = new AsyncItemProcessor<>();
        pessoaPessoaAsyncItemProcessor.setDelegate(pessoaProcessor());
        pessoaPessoaAsyncItemProcessor.setTaskExecutor(taskExecutor());
        return pessoaPessoaAsyncItemProcessor;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setQueueCapacity(8);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("Multithread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    private ItemProcessor<Pessoa, Pessoa> pessoaProcessor() {
        return pessoa -> {
            try {
                String uri = String.format("http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d", pessoa.getId());
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            } catch (RestClientResponseException e) {
                System.out.println(pessoa.getId());
                return pessoa;
            }
            return pessoa;
        };
    }
}
