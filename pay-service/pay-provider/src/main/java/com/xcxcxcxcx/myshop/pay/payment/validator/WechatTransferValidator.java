package com.xcxcxcxcx.myshop.pay.payment.validator;

import com.xcxcxcxcx.myshop.dto.PaymentTransferRequest;
import com.xcxcxcxcx.myshop.pay.exception.ValidateException;
import com.xcxcxcxcx.myshop.pay.payment.abs.Validator;
import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
@Component("wechatTransferValidator")
public class WechatTransferValidator implements Validator{

    @Override
    public void validate(AbstractRequest request) {

        if(request == null){
            throw new ValidateException("请求参数为空");
        }

        PaymentTransferRequest transferRequest = (PaymentTransferRequest) request;

        if(StringUtils.isEmpty(((PaymentTransferRequest) request).getTopicId())){
            throw new ValidateException("topicId为空");
        }
        if(StringUtils.isEmpty(((PaymentTransferRequest) request).getTradeNo())){
            throw new ValidateException("tradeNo为空");
        }
        if(StringUtils.isEmpty(((PaymentTransferRequest) request).getTransferAmount())){
            throw new ValidateException("transferAmount为空");
        }
        if(StringUtils.isEmpty(((PaymentTransferRequest) request).getUserAccount())){
            throw new ValidateException("userAccount为空");
        }
        if(StringUtils.isEmpty(((PaymentTransferRequest) request).getUserId())){
            throw new ValidateException("userId为空");
        }

    }
}
