package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.request.AbstractRequest;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class LogCleanRequest extends AbstractRequest{

    //清除离目前超过${expiredValue}的log
    private Long expiredValue;

    public Long getExpiredValue() {
        return expiredValue;
    }

    public void setExpiredValue(Long expiredValue) {
        this.expiredValue = expiredValue;
    }

    @Override
    public String toString() {
        return "LogCleanRequest{" +
                "expiredValue=" + expiredValue +
                '}';
    }
}
