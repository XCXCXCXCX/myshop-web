package com.xcxcxcxcx.myshop.log.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dto.LogEntityBuilder;
import com.xcxcxcxcx.myshop.log.service.ILogSenderService;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Service("logSenderService")
public class LogSenderServiceImpl implements ILogSenderService {

    private static final String LOG_TOPIC_NAME = "log-topic";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void synDeliver(Logger logger, LogEntityBuilder.LogEntity logEntity, AbstractResponse response) {

        try{

//            ProducerRecord<String, LogEntityBuilder.LogEntity> record =
//                    new ProducerRecord<String, LogEntityBuilder.LogEntity>(LOG_TOPIC_NAME, logEntity);

            kafkaTemplate.send(LOG_TOPIC_NAME, logEntity).get(1000, TimeUnit.MILLISECONDS);

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());

        }catch (TimeoutException | InterruptedException | ExecutionException e) {
            response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
            response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
            logger.error("log发送到kafka失败: {" + logEntity.toString() + "}, exception : {" + e +"}");
        }

    }
}
