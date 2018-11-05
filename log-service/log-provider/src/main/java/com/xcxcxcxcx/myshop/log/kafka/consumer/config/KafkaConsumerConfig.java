package com.xcxcxcxcx.myshop.log.kafka.consumer.config;

import com.xcxcxcxcx.myshop.dto.LogEntityBuilder;
import com.xcxcxcxcx.myshop.log.kafka.consumer.LogMessageListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    public static final String BROKE_ADDR = "192.168.179.130:9092";

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setMessageListener(logMessageListener());
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setClientId("log-consumer");
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKE_ADDR);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                IntegerDeserializer.class);
        //配置反序列化类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES,
                "com.xcxcxcxcx.myshop.dto");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                LogEntityBuilder.LogEntity.class);
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
//                LogEntityBuilder.LogEntity.class);


        return props;
    }

    @Bean
    public LogMessageListener logMessageListener(){
        return new LogMessageListener();
    }
}