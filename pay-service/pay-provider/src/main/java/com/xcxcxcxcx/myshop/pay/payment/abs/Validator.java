package com.xcxcxcxcx.myshop.pay.payment.abs;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public interface Validator {

    void validate(AbstractRequest request);
}
