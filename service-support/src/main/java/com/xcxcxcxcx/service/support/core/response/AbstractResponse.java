package com.xcxcxcxcx.service.support.core.response;

import java.io.Serializable;

/**
 * @author XCXCXCXCX
 * @date 2018/10/21
 * @comments
 */
public abstract class AbstractResponse implements Serializable{

    private static final long serialVersionUID = 8597899637940392226L;

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public abstract String toString();
}
