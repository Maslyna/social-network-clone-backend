package net.maslyna.follower.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topics.notification.post}")
    private String postTopic;

    @Value("${spring.kafka.topics.partitions}")
    private int partitions;

    @Bean
    public NewTopic postCreatedTopic() {
        return TopicBuilder.name(postTopic)
                .partitions(partitions)
                .build();
    }

}
