package com.xcxcxcxcx.myshop.log.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.ILogCollectService;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.myshop.log.dal.dto.LogQuery;
import com.xcxcxcxcx.myshop.log.dal.entity.LogEntity;
import com.xcxcxcxcx.myshop.log.dal.persistence.LogMapper;
import com.xcxcxcxcx.myshop.log.exception.ServiceException;
import com.xcxcxcxcx.myshop.log.exception.ValidateException;
import com.xcxcxcxcx.myshop.log.service.ILogSenderService;
import com.xcxcxcxcx.myshop.util.ExceptionUtils;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Service("logCollectService")
public class LogCollectServiceImpl implements ILogCollectService{

    private static final String LOG_TOPIC_NAME = "log-topic";

    private static final Logger logger = LoggerFactory.getLogger(LogCollectServiceImpl.class);

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    LogMapper logMapper;

    @Autowired
    ILogSenderService logSenderService;

    @Override
    public LogDeliverResponse deliverLog(LogDeliverRequest request) {
        LogDeliverResponse response = new LogDeliverResponse();

        try{

            validateRequest(request);

            logSenderService.synDeliver(logger, request.getLogEntity(), response);

        } catch (Exception e) {
            logger.error("deliverLog occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("deliverLog response : " + response.toString());
        }

        return response;
    }

    private void validateRequest(AbstractRequest request) {

        if(request == null){
            throw new ValidateException("请求对象为空");
        }

        if(request instanceof LogDeliverRequest){

            if(StringUtils.isEmpty(((LogDeliverRequest) request).getLogEntity())){
                throw new ValidateException("LogEntity对象为空");
            }

        }
        if(request instanceof LogQueryRequest){

            if(StringUtils.isEmpty(((LogQueryRequest) request).getPageNum())){
                throw new ValidateException("pageNum为空");
            }
            if(StringUtils.isEmpty(((LogQueryRequest) request).getPageSize())){
                throw new ValidateException("pageSize为空");
            }

        }
        if(request instanceof LogCleanRequest){

            if(StringUtils.isEmpty(((LogCleanRequest) request).getExpiredValue())){
                throw new ValidateException("expiredValue为空");
            }

        }

    }

    @Override
    public LogQueryResponse queryLog(LogQueryRequest request) {
        LogQueryResponse response = new LogQueryResponse();

        try{

            validateRequest(request);

            LogQuery logQuery = new LogQuery();
            logQuery.setUserId(request.getUserId());
            logQuery.setBlurLogInfo(request.getBlurLogInfo());
            logQuery.setFromApp(request.getFromApp());
            logQuery.setOpType(request.getOpType());
            logQuery.setFromTime(request.getFromTime());
            logQuery.setToTime(request.getToTime());

            List<LogEntity> results =  logMapper.queryLog(logQuery);
            List<LogEntityBuilder.LogEntity> logEntities = new ArrayList<>();
            for(LogEntity result : results){
                LogEntityBuilder.LogEntity logEntity = new LogEntityBuilder.LogEntity();
                logEntity.setOpId(result.getOpId());
                logEntity.setOpTime(result.getOpTime());
                logEntity.setLogInfo(result.getLogInfo());
                logEntity.setFromApp(result.getFromApp());
                logEntity.setOpType(result.getOpType());
                logEntity.setUserId(result.getUserId());
                logEntities.add(logEntity);
            }

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setLogEntities(logEntities);

        } catch (Exception e) {
            logger.error("queryLog occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("queryLog response : " + response.toString());
        }

        return response;
    }

    @Override
    public LogCleanResponse cleanLog(LogCleanRequest request) {
        LogCleanResponse response = new LogCleanResponse();

        try{

            validateRequest(request);

            int row = logMapper.deleteLog(request.getExpiredValue());

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setDelRow(row);

        } catch (Exception e) {
            logger.error("cleanLog occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("cleanLog response : " + response.toString());
        }

        return response;
    }
}
