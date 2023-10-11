package net.maslyna.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topics.notification.notification-send.post-liked}")
    private String postLikedTopic;

    @Value("${spring.kafka.topics.notification.notification-send.comment-liked}")
    private String commentLikedTopic;

    @Value("${spring.kafka.topics.notification.notification-send.post-created}")
    private String postCreatedTopic;

    @Value("${spring.kafka.topics.partitions}")
    private int partitions;

    @Bean
    public NewTopic postCreatedTopic() {
        return TopicBuilder.name(postCreatedTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic postLikedTopic() {
        return TopicBuilder.name(postLikedTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic commentLikedTopic() {
        return TopicBuilder.name(commentLikedTopic)
                .partitions(partitions)
                .build();
    }
}
