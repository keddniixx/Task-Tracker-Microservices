package com.server.notification.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.notification.model.TaskDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, TaskDTO> consumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "task");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        JsonDeserializer<TaskDTO> jsonDeserializer = new JsonDeserializer<>(TaskDTO.class, objectMapper);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, TaskDTO> consumerFactory
    ) {
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, TaskDTO>();
        containerFactory.setConcurrency(1);
        containerFactory.setConsumerFactory(consumerFactory);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, TaskDTO> consumerFactory2(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "task2");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        JsonDeserializer<TaskDTO> jsonDeserializer = new JsonDeserializer<>(TaskDTO.class, objectMapper);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskDTO> kafkaListenerContainerFactory2(
            ConsumerFactory<String, TaskDTO> consumerFactory2
    ) {
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, TaskDTO>();
        containerFactory.setConcurrency(1);
        containerFactory.setConsumerFactory(consumerFactory2);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, TaskDTO> consumerFactory3(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "task3");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        JsonDeserializer<TaskDTO> jsonDeserializer = new JsonDeserializer<>(TaskDTO.class, objectMapper);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskDTO> kafkaListenerContainerFactory3(
            ConsumerFactory<String, TaskDTO> consumerFactory3
    ) {
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, TaskDTO>();
        containerFactory.setConcurrency(1);
        containerFactory.setConsumerFactory(consumerFactory3);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return containerFactory;
    }
}
