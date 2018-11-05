package com.xcxcxcxcx.myshop.log.kafka.consumer;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.dto.LogEntityBuilder;
import com.xcxcxcxcx.myshop.log.dal.entity.LogEntity;
import com.xcxcxcxcx.myshop.log.dal.persistence.LogMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Component
public class LogMessageListener{

    private static final Logger logger = LoggerFactory.getLogger(LogMessageListener.class);

    @Autowired
    LogMapper logMapper;

    @KafkaListener(id = "logMessageListener", topics = "log-topic")
    public void onMessage(LogEntityBuilder.LogEntity logEntity) {
        logger.debug("debug:监听到消息");

        int row = 0;
        try {
            row = logMapper.insertLog((LogEntity)logEntity);
            if(row < 1){
                logger.error("log消息持久化失败: " + logEntity.toString());
                return;
            }
        }catch (Exception e){
            logger.error("log消息持久化失败: " + logEntity.toString());
            return;
        }

        logger.debug("debug:消息处理完毕");
    }

//    @KafkaListener(id = "logMessageListener2", topics = "log-topic")
//    public void onMessage2(String message) {
//        System.out.println(message);
//    }


}
