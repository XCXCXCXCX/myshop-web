package com.xcxcxcxcx.myshop.pay.payment.channel;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.xcxcxcxcx.myshop.constants.PayChannelEnum;
import com.xcxcxcxcx.myshop.constants.ResponseCodeEnum;
import com.xcxcxcxcx.myshop.dto.PaymentNotifyRequest;
import com.xcxcxcxcx.myshop.dto.PaymentNotifyResponse;
import com.xcxcxcxcx.myshop.dto.PaymentRequest;
import com.xcxcxcxcx.myshop.dto.PaymentResponse;
import com.xcxcxcxcx.myshop.pay.dal.entity.PayEntity;
import com.xcxcxcxcx.myshop.pay.dal.persistence.PayMapper;
import com.xcxcxcxcx.myshop.pay.payment.abs.BasePayment;
import com.xcxcxcxcx.myshop.pay.payment.abs.PaymentContext;
import com.xcxcxcxcx.myshop.pay.payment.abs.Validator;
import com.xcxcxcxcx.myshop.pay.payment.context.WechatPayContext;
import com.xcxcxcxcx.myshop.pay.service.ILogSenderService;
import com.xcxcxcxcx.myshop.pay.util.LogEntityBuilder;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
@Service("wechatPayment")
public class WechatPayment extends BasePayment {

    private static final Logger logger = LoggerFactory.getLogger(WechatPayment.class);

    @Autowired
    ILogSenderService logSenderService;

    @Override
    public AbstractResponse notifyPayResult(AbstractRequest request) {
        PaymentNotifyResponse response = new PaymentNotifyResponse();

        //检查参数
        PaymentNotifyRequest paymentNotifyRequest = (PaymentNotifyRequest) request;
        Map<String, Object> params = paymentNotifyRequest.getResultMap();

        //若支付成功或支付状态故障,进行持久化
        //不同的厂商，map结构和参数名也会不一样
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
                logSenderService.synDeliver(logger,logEntity,response);
            }finally {
                if(row < 1){
                    response.setCode(ResponseCodeEnum.SUCCESS.getCode());
                    response.setMsg("回调支付成功但持久化异常，检查是否已存在该订单，以及订单状态是否有误");
                    return response;
                }
            }
        }


        response.setCode("SUCCESS");
        response.setMsg("支付成功");

        return response;
    }

    @Resource(name = "wechatValidator")
    Validator validator;

    @Override
    protected Validator getValidator() {
        return validator;
    }

    @Override
    protected String getPayChannel() {
        return PayChannelEnum.WECHAT_PAY_CHANNEL.getCode();
    }

    @Override
    protected PaymentContext createContext(AbstractRequest request) {

        PaymentRequest paymentRequest = (PaymentRequest) request;

        WechatPayContext context = new WechatPayContext();

        context.setTradeNo(paymentRequest.getTradeNo());

        context.setBody(paymentRequest.getDescription());

        context.setTotalAmount(paymentRequest.getPayAmount());

        context.setTradeType("NATIVE");

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
        PaymentResponse response = new PaymentResponse();

        //调用远程接口，返回二维码
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
        response.setTradeNo(context.getTradeNo());
        response.setUrl("://i'm a fake QR code");

        return response;
    }

    @Autowired
    PayMapper payMapper;

    @Override
    protected void afterPay(AbstractRequest request, AbstractResponse response, PaymentContext context) {

        PaymentRequest paymentRequest = (PaymentRequest) request;

        if("00000000".equals(response.getCode())){
            //持久化订单
            PayEntity payEntity = new PayEntity();
            payEntity.setPayId(context.getTradeNo());
            payEntity.setPayDate(paymentRequest.getPayDate());
            payEntity.setPayAmount(context.getTotalAmount());//负数表示支出
            payEntity.setTopicId(paymentRequest.getTopicId());
            payEntity.setUserId(paymentRequest.getUserId());
            payEntity.setStatus(0);//预支付成功:0 ,预支付失败:-1

            payMapper.insertPay(payEntity);
        }

    }
}
