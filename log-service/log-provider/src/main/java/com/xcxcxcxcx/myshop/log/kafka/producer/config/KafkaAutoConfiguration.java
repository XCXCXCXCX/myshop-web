package com.xcxcxcxcx.myshop.log.kafka.producer.config;

import com.xcxcxcxcx.myshop.dto.LogEntityBuilder;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Configuration
@EnableKafka
public class KafkaAutoConfiguration {

    private static final String BROKE_ADDR = "192.168.179.130:9092";

    @Bean
    public ProducerFactory<Integer, LogEntityBuilder.LogEntity> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        System.out.println("加载kafka producer配置");
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKE_ADDR);
        props.put(ProducerConfig.ACKS_CONFIG,"-1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return props;
    }

    @Bean
    public KafkaTemplate<Integer, LogEntityBuilder.LogEntity> kafkaTemplate() {
        return new KafkaTemplate<Integer, LogEntityBuilder.LogEntity>(producerFactory());
    }
}
