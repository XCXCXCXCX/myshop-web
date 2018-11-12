package com.xcxcxcxcx.myshop.divide.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.IDivideCoreService;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.divide.dal.entity.Bill;
import com.xcxcxcxcx.myshop.divide.dal.entity.Topic;
import com.xcxcxcxcx.myshop.divide.dal.persistence.BillMapper;
import com.xcxcxcxcx.myshop.divide.dal.persistence.BillStatusEnum;
import com.xcxcxcxcx.myshop.divide.dal.persistence.TopicMapper;
import com.xcxcxcxcx.myshop.divide.dal.persistence.TopicStatusEnum;
import com.xcxcxcxcx.myshop.divide.exception.ServiceException;
import com.xcxcxcxcx.myshop.divide.exception.ValidateException;
import com.xcxcxcxcx.myshop.divide.util.ExceptionUtils;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public class DivideCoreServiceImpl implements IDivideCoreService {

    private final static Logger logger = LoggerFactory.getLogger(DivideCoreServiceImpl.class);

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    BillMapper billMapper;

    public TopicCreateResponse createTopic(TopicCreateRequest topicCreateRequest) {

        TopicCreateResponse response = new TopicCreateResponse();

        try{
            validateRequest(topicCreateRequest);

            Topic topic = new Topic();
            topic.setPublisherId(topicCreateRequest.getPublisherId());
            topic.setUnitAmount(topicCreateRequest.getUnitAmount());
            topic.setStatus(TopicStatusEnum.NEW.getCode());
            long nowTimeStamp = System.currentTimeMillis();
            topic.setBeginTime(new Timestamp(nowTimeStamp));
            topic.setActiveTime(new Timestamp(nowTimeStamp + topicCreateRequest.getDelayBeginTime()));
            topic.setEndTime(new Timestamp(nowTimeStamp + topicCreateRequest.getDelayBeginTime() + topicCreateRequest.getDuration()));
            int row = topicMapper.insertTopic(topic);
            if(row < 1){
                response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
                response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
                return response;
            }
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setTopicId(topic.getTopicId());

        }catch (Exception e){
            logger.error("createTopic occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("createTopic response : " + response.toString());
        }

        return response;
    }

//    public TopicStatusUpdateResponse updateTopicStatus(TopicStatusUpdateRequest topicStatusUpdateRequest) {
//        TopicStatusUpdateResponse response = new TopicStatusUpdateResponse();
//
//        try{
//            validateRequest(topicStatusUpdateRequest);
//
//            Long topicId = topicStatusUpdateRequest.getTopicId();
//            int newStatus = topicStatusUpdateRequest.getStatus();
//
//            Topic topic = topicMapper.getTopicByTopicid(topicId);
//            if(topic == null){
//                response.setCode(ResponseCodeEnum.TOPIC_NOT_EXIST.getCode());
//                response.setMsg(ResponseCodeEnum.TOPIC_NOT_EXIST.getMsg());
//                return response;
//            }
//
//            int currentStatus = topic.getStatus();
//
//            if(currentStatus >= newStatus){
//                response.setCode(ResponseCodeEnum.TOPIC_ERROR_STATUS.getCode());
//                response.setMsg(ResponseCodeEnum.TOPIC_ERROR_STATUS.getMsg());
//                return response;
//            }
//
//            int row = topicMapper.updateTopicStatus(topicId,currentStatus,newStatus);
//            if(row < 1){
//                response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
//                response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
//                return response;
//            }
//            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
//            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
//
//        }catch (Exception e){
//            logger.error("updateTopicStatus occur exception : " + e);
//            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
//            response.setCode(serviceException.getErrorCode());
//            response.setMsg(serviceException.getErrorMessage());
//        }finally {
//            logger.info("updateTopicStatus response : " + response.toString());
//        }
//
//        return response;
//    }

    public TopicJoinResponse joinTopic(TopicJoinRequest topicJoinRequest) {
        TopicJoinResponse response = new TopicJoinResponse();

        try{
            validateRequest(topicJoinRequest);

            Long topicId = topicJoinRequest.getTopicId();
            Topic topic = topicMapper.getTopicByTopicid(topicId);
            double unitAmount = topic.getUnitAmount();

            if(topic.getStatus() != TopicStatusEnum.NEW.getCode()){
                throw new ValidateException("该topic目前不在可加入状态");
            }

            //topic active前1分钟禁止加入，进入prepare状态
            if(topic.getActiveTime().getTime() - 60 * 1000 < System.currentTimeMillis()){
                topicMapper.updateTopicStatus(topicId, TopicStatusEnum.NEW.getCode(), TopicStatusEnum.PREPARE.getCode());
            }

            Long userId = topicJoinRequest.getUserId();
            Bill bill = billMapper.getBillByTopicidAndUserid(topicId,userId);
            if(bill != null){
                response.setCode(ResponseCodeEnum.BILL_EXISTED.getCode());
                response.setMsg(ResponseCodeEnum.BILL_EXISTED.getMsg());
                return response;
            }
            bill = new Bill();
            bill.setTopicId(topicId);
            bill.setUserId(userId);
            bill.setCurrentAmount(unitAmount);
            bill.setStatus(BillStatusEnum.NEW.getCode());

            int row = billMapper.insertBill(bill);
            if(row < 1){
                response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
                response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
                return response;
            }
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());

        }catch (Exception e){
            logger.error("joinTopic occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("joinTopic response : " + response.toString());
        }

        return response;
    }

    public TopicQueryByUseridResponse topicQueryByUserid(TopicQueryByUseridRequest topicQueryByUseridRequest) {

        TopicQueryByUseridResponse response = new TopicQueryByUseridResponse();

        try{
            validateRequest(topicQueryByUseridRequest);

            List<Topic> topicList = topicMapper.getTopicByUserid(topicQueryByUseridRequest.getUserId());
            List<TopicEntity> topicEntityList = new ArrayList<TopicEntity>();

            for(Topic topic : topicList){
                TopicEntity entity = new TopicEntity();
                entity.setTopicId(topic.getTopicId());
                entity.setStatus(topic.getStatus());
                entity.setActiveTime(topic.getActiveTime());
                entity.setBeginTime(topic.getBeginTime());
                entity.setEndTime(topic.getEndTime());
                entity.setPublisherId(topic.getPublisherId());
                entity.setUnitAmount(topic.getUnitAmount());
                topicEntityList.add(entity);
            }

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setTopicList(topicEntityList);

        }catch (Exception e){
            logger.error("topicQueryByUserid occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("topicQueryByUserid response : " + response.toString());
        }

        return response;
    }

    public TopicQueryByTopicidResponse topicQueryByTopicid(TopicQueryByTopicidRequest topicQueryByTopicidRequest) {
        TopicQueryByTopicidResponse response = new TopicQueryByTopicidResponse();

        try{
            validateRequest(topicQueryByTopicidRequest);

            Topic topic = topicMapper.getTopicByTopicid(topicQueryByTopicidRequest.getTopicId());
            List<Bill> billList = billMapper.getBillByTopicid(topic.getTopicId());
            List<BillEntity> billEntities = billList.stream().map(x->{
                BillEntity billEntity = new BillEntity();
                billEntity.setTopicId(x.getTopicId());
                billEntity.setUserId(x.getUserId());
                billEntity.setBillId(x.getBillId());
                billEntity.setCurrentAmount(x.getCurrentAmount());
                billEntity.setStatus(x.getStatus());
                return billEntity;
            }).collect(Collectors.toList());
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setTopicId(topic.getTopicId());
            response.setPublisherId(topic.getPublisherId());
            response.setUnitAmount(topic.getUnitAmount());
            response.setStatus(topic.getStatus());
            response.setBillList(billEntities);

        }catch (Exception e){
            logger.error("topicQueryByTopicid occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("topicQueryByTopicid response : " + response.toString());
        }

        return response;
    }

    public BillQueryResponse billQueryByTopicIdAndUserId(BillQueryRequest billQueryRequest) {
        BillQueryResponse response = new BillQueryResponse();

        try{

            validateRequest(billQueryRequest);

            Bill bill = billMapper.getBillByTopicidAndUserid(billQueryRequest.getTopicId(), billQueryRequest.getUserId());
            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
            response.setBillId(bill.getBillId());
            response.setUserId(bill.getUserId());
            response.setStatus(bill.getStatus());

        }catch (Exception e){
            logger.error("billQueryByTopicIdAndUserId occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("billQueryByTopicIdAndUserId response : " + response.toString());
        }

        return response;
    }

    @Override
    public AfterPayUpdateResponse updateAfterPay(AfterPayUpdateRequest afterPayUpdateRequest) {

        AfterPayUpdateResponse response = new AfterPayUpdateResponse();

        try{
            validateRequest(afterPayUpdateRequest);

            int row = billMapper.updateBillStatus(afterPayUpdateRequest.getBillId(), BillStatusEnum.NEW.getCode(), BillStatusEnum.PAYED.getCode());

            if(row < 1){
                throw new ValidateException("更新失败");
            }

            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());

        }catch (Exception e){
            logger.error("updateAfterPay occur exception : " + e);
            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }finally {
            logger.info("updateAfterPay response : " + response.toString());
        }


        return response;
    }

    //    public BillStatusUpdateResponse billStatusUpdate(BillStatusUpdateRequest billStatusUpdateRequest) {
//
//        BillStatusUpdateResponse response = new BillStatusUpdateResponse();
//
//        try {
//
//            validateRequest(billStatusUpdateRequest);
//
//            Bill bill = billMapper.getBillByBillid(billStatusUpdateRequest.getBillId());
//            if(bill == null){
//                response.setCode(ResponseCodeEnum.BILL_NOT_EXIST.getCode());
//                response.setMsg(ResponseCodeEnum.BILL_NOT_EXIST.getMsg());
//                return response;
//            }
//
//            int currentStatus = bill.getStatus();
//            int newStatus = billStatusUpdateRequest.getStatus();
//            if(currentStatus >= newStatus){
//                response.setCode(ResponseCodeEnum.BILL_ERROR_STATUS.getCode());
//                response.setMsg(ResponseCodeEnum.BILL_ERROR_STATUS.getMsg());
//                return response;
//            }
//
//            int row = billMapper.updateBillStatus(billStatusUpdateRequest.getBillId(), currentStatus,billStatusUpdateRequest.getStatus());
//            if(row < 1){
//                response.setCode(ResponseCodeEnum.SYSTEM_BUSY.getCode());
//                response.setMsg(ResponseCodeEnum.SYSTEM_BUSY.getMsg());
//                return response;
//            }
//            response.setCode(ResponseCodeEnum.SUCCESS.getCode());
//            response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
//
//        }catch (Exception e){
//            logger.error("billStatusUpdate occur exception : " + e);
//            ServiceException serviceException = (ServiceException) ExceptionUtils.handlerException4biz(e);
//            response.setCode(serviceException.getErrorCode());
//            response.setMsg(serviceException.getErrorMessage());
//        }finally {
//            logger.info("billStatusUpdate response : " + response.toString());
//        }
//
//        return response;
//    }

    private void validateRequest(AbstractRequest request) {

        if(request == null){
            throw new ValidateException("request对象为空");
        }

        if(request instanceof TopicCreateRequest){
            if(StringUtils.isEmpty(((TopicCreateRequest) request).getPublisherId())){
                throw new ValidateException("publisherId为空");
            }
            if(StringUtils.isEmpty(((TopicCreateRequest) request).getUnitAmount())){
                throw new ValidateException("unitAmount为空");
            }
            //默认延迟开始时间为15分钟
            if(StringUtils.isEmpty(((TopicCreateRequest) request).getDelayBeginTime())){
                ((TopicCreateRequest) request).setDelayBeginTime(900*1000L);
            }
            //默认持续时间5分钟
            if(StringUtils.isEmpty(((TopicCreateRequest) request).getDuration())){
                ((TopicCreateRequest) request).setDuration(300*1000L);
            }
        }
        if(request instanceof AfterPayUpdateRequest){
            if(StringUtils.isEmpty(((AfterPayUpdateRequest) request).getBillId())){
                throw new ValidateException("billId为空");
            }
        }
        if(request instanceof TopicJoinRequest){
            if(StringUtils.isEmpty(((TopicJoinRequest) request).getUserId())){
                throw new ValidateException("userId为空");
            }
            if(StringUtils.isEmpty(((TopicJoinRequest) request).getTopicId())){
                throw new ValidateException("topicId为空");
            }
        }
        if(request instanceof TopicQueryByUseridRequest){
            if(StringUtils.isEmpty(((TopicQueryByUseridRequest) request).getUserId())){
                throw new ValidateException("userId为空");
            }
        }
        if(request instanceof TopicQueryByTopicidRequest){
            if(StringUtils.isEmpty(((TopicQueryByTopicidRequest) request).getTopicId())){
                throw new ValidateException("topicId为空");
            }
        }
        if(request instanceof BillQueryRequest){
            if(StringUtils.isEmpty(((BillQueryRequest) request).getUserId())){
                throw new ValidateException("userId为空");
            }
            if(StringUtils.isEmpty(((BillQueryRequest) request).getTopicId())){
                throw new ValidateException("topicId为空");
            }
        }

    }
}
