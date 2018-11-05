package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public class TopicGrabResponse extends AbstractResponse{

    private Long userId;

    private int grabAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getGrabAmount() {
        return grabAmount;
    }

    public void setGrabAmount(int grabAmount) {
        this.grabAmount = grabAmount;
    }

    @Override
    public String toString() {
        return "TopicGrabResponse{" +
                "userId=" + userId +
                ", grabAmount=" + grabAmount +
                '}';
    }
}
