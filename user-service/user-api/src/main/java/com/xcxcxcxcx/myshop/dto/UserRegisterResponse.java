package com.xcxcxcxcx.myshop.dto;

import com.xcxcxcxcx.service.support.core.response.AbstractResponse;

/**
 * @author XCXCXCXCX
 * @date 2018/10/21
 * @comments
 */
public class UserRegisterResponse extends AbstractResponse {

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserRegisterResponse{" +
                "uid=" + uid +
                '}';
    }

}
