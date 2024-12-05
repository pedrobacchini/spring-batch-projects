package com.springbatch.bdremotepartitioner.config;

import com.springbatch.bdremotepartitioner.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(Constants.TOPIC_NAME)
                .partitions(Constants.TOPIC_PARTITION_COUNT)
                .build();
    }
}
