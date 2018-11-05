package com.xcxcxcxcx.myshop.pay.payment.channel;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dto.*;
import com.xcxcxcxcx.myshop.pay.dal.entity.PayEntity;
import com.xcxcxcxcx.myshop.pay.dal.persistence.PayMapper;
import com.xcxcxcxcx.myshop.pay.payment.abs.BasePayment;
import com.xcxcxcxcx.myshop.pay.payment.abs.PaymentContext;
import com.xcxcxcxcx.myshop.pay.payment.abs.Validator;
import com.xcxcxcxcx.myshop.pay.payment.constants.PayChannelEnum;
import com.xcxcxcxcx.myshop.pay.payment.context.WechatTransferContext;
import com.xcxcxcxcx.myshop.pay.service.ILogSenderService;
import com.xcxcxcxcx.myshop.pay.util.LogEntityBuilder;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class WechatTransferPayment extends BasePayment{

    private static final Logger logger = LoggerFactory.getLogger(WechatTransferPayment.class);

    @Autowired
    ILogSenderService logSenderService;

    @Override
    public AbstractResponse notifyPayResult(AbstractRequest request) {
        PaymentNotifyResponse response = new PaymentNotifyResponse();

        //检查参数
        PaymentNotifyRequest paymentNotifyRequest = (PaymentNotifyRequest) request;
        Map<String, Object> params = paymentNotifyRequest.getResultMap();

        //若支付成功或支付状态故障,进行持久化
        if ("SUCCESS".equals(params.get("result_code").toString())) {
            int row = 0;
            try{
                row = payMapper.updatePayStatus(params.get("tradeNo").toString(), 0, 1);
            }catch (Exception e){
                logger.error("支付成功但持久化异常 :" + e);
                //日志服务记录
                LogEntityBuilder.LogEntity logEntity =
                        LogEntityBuilder.createLog(LogEntityBuilder.OpTypeEnum.ERROR,
                                -1L,"支付成功但持久化异常 :" + e).build();
                logSenderService.synDeliver(logger, logEntity, response);
            }finally {
                if(row < 1){
                    response.setCode("EXCEPTION");
                    response.setMsg("回调支付成功但持久化异常，检查是否存在该订单，以及订单状态是否有误");
                    return response;
                }
            }
        }

        response.setCode("SUCCESS");
        response.setMsg("支付成功");

        return response;
    }

    @Resource(name = "wechatTransferValidator")
    Validator validator;

    @Override
    protected Validator getValidator() {
        return validator;
    }

    @Override
    protected String getPayChannel() {
        return PayChannelEnum.WECHAT_TRANSFER_CHANNEL.getCode();
    }

    @Override
    protected PaymentContext createContext(AbstractRequest request) {

        PaymentTransferRequest paymentRequest = (PaymentTransferRequest) request;

        WechatTransferContext context = new WechatTransferContext();

        context.setTradeNo(paymentRequest.getTradeNo());

        context.setBody("用户领取红包到账");

        context.setTotalAmount(paymentRequest.getTransferAmount());

        context.setTradeType("APP");

        context.setUserAccount(paymentRequest.getUserAccount());

        return context;
    }

    @Override
    protected void requestValidate(AbstractRequest request) {
        getValidator().validate(request);
    }

    @Override
    protected void dataPrepare(PaymentContext context) {
        //TODO
    }

    @Override
    protected AbstractResponse doPay(PaymentContext context) {
        PaymentTransferResponse response = new PaymentTransferResponse();

        //调用远程接口，发起转账
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
        response.setTradeNo(context.getTradeNo());

        return response;
    }

    @Autowired
    PayMapper payMapper;

    @Override
    protected void afterPay(AbstractRequest request, AbstractResponse response, PaymentContext context) {

        PaymentRequest paymentRequest = (PaymentRequest) request;

        if(response.getCode() == "SUCCESS"){
            //持久化订单
            PayEntity payEntity = new PayEntity();
            payEntity.setPayId(context.getTradeNo());
            payEntity.setPayDate(paymentRequest.getPayDate());
            payEntity.setPayAmount(-context.getTotalAmount());//负数表示支出
            payEntity.setTopicId(paymentRequest.getTopicId());
            payEntity.setUserId(paymentRequest.getUserId());
            payEntity.setStatus(0);//预支付成功:0 ,预支付失败:-1

            payMapper.insertPay(payEntity);
        }

    }
}
