package net.maslyna.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.PostCreatedResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostListeners {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.post}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void listener(PostCreatedResponse response) {
//        PostCreatedResponse response = objectMapper.readValue(o, PostCreatedResponse.class);
        log.info("object = {}", response);
    }
}
