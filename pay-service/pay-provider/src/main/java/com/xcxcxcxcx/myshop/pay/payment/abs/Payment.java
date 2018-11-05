package com.xcxcxcxcx.myshop.pay.payment.abs;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;
import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public interface Payment {

    AbstractResponse process(AbstractRequest request);

    AbstractResponse notifyPayResult(AbstractRequest request);

}
