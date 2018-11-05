package com.xcxcxcxcx.myshop.pay.payment.validator;

import com.xcxcxcxcx.myshop.dto.PaymentRequest;
import com.xcxcxcxcx.myshop.pay.exception.ValidateException;
import com.xcxcxcxcx.myshop.pay.payment.abs.Validator;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
@Component("wechatValidator")
public class WechatValidator implements Validator{

    @Override
    public void validate(AbstractRequest request) {

        if(request == null){
            throw new ValidateException("请求参数为空");
        }

        PaymentRequest paymentRequest = (PaymentRequest) request;

        if(StringUtils.isEmpty(paymentRequest.getTradeNo())){
            throw new ValidateException("tradeNo为空");
        }
        if(StringUtils.isEmpty(paymentRequest.getUserId())){
            throw new ValidateException("userId为空");
        }
        if(StringUtils.isEmpty(paymentRequest.getPayChannel())){
            throw new ValidateException("payChannel为空");
        }
        if(StringUtils.isEmpty(paymentRequest.getPayAmount())){
            throw new ValidateException("payAmount为空");
        }
        if(StringUtils.isEmpty(paymentRequest.getDescription())){
            throw new ValidateException("description为空");
        }
        if(StringUtils.isEmpty(paymentRequest.getPayDate())){
            throw new ValidateException("payDate为空");
        }
    }

}
