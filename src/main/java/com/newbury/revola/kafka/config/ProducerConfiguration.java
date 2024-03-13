package com.newbury.revola.kafka.config;

import com.newbury.revola.domain.ErrorDetails;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class ProducerConfiguration {

    private final Logger log = LoggerFactory.getLogger(ProducerConfiguration.class);

    @Value("${spring.kafka.bootstrap.servers}")
    String bootstrapServers;

    @Value("${dmm.general.error.topic}")
    String generalErrorTopic;

    private static final String VFLAAS = "_VFLAAS";
    private static final String VFLAAS_GROUP = "vflaasGroup";
    private static final String deserializationExceptionMsg = "failed to deserialize; nested exception is org.apache.kafka.common.errors.SerializationException: Can't deserialize data";
    private static final String ALL = "all";

    @Bean
    public ProducerFactory<String, ?> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30000000);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, ALL);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    public void handleDeserializationException(Exception ex, ConsumerRecord<?, ?> consumerRecord) {
        String messageKey = consumerRecord.key().toString();
        if (messageKey.endsWith(VFLAAS)) {
            if (ex instanceof ListenerExecutionFailedException) {
                String message = ex.getCause().getMessage();
                String errorDescription;
                String payload = "";
                if (message.contains(deserializationExceptionMsg)) {
                    String[] byteArrayString = message.substring(message.indexOf("[[") + 2, message.indexOf("]")).split(",");
                    log.info("byteArrayString : {} ", Arrays.toString(byteArrayString));
                    if (byteArrayString.length > 0) {
                        byte[] bytes = new byte[byteArrayString.length];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = Byte.parseByte(byteArrayString[i].trim());
                        }
                        payload = new String(bytes, StandardCharsets.UTF_8);
                        log.info("UTF_8 converted payload : {} ", payload);
                        errorDescription = "Deserialization exception occurred in topic : " + consumerRecord.topic() + ", \n " +
                                "offset : " + consumerRecord.offset() + ", partition : " + consumerRecord.partition();
                    } else {
                        errorDescription = "Not able to extract byteArray from exception : " + message + ", occurred in topic : " + consumerRecord.topic() + ", \n " +
                                "offset : " + consumerRecord.offset() + ", partition : " + consumerRecord.partition();
                    }
                    sendErrorToKafkaErrorTopic(consumerRecord, payload, errorDescription);
                }
            }
        }
    }
    
    private void sendErrorToKafkaErrorTopic(ConsumerRecord<?, ?> consumerRecord, String payload, String errorDescription) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimeStamp(LocalDateTime.now());
        errorDetails.setResolutionStatus("Created");
        errorDetails.setService("NB-Bulk-Allocation microservice");
        errorDetails.setPayload(payload);
        errorDetails.setErrorDescription(errorDescription);
        errorDetails.setPath("");
        KafkaTemplate<String, ErrorDetails> kafkaTemplate = new KafkaTemplate<>((ProducerFactory<String, ErrorDetails>) producerFactory());
        kafkaTemplate.send(generalErrorTopic, (UUID.randomUUID() + VFLAAS), errorDetails);
    }



}