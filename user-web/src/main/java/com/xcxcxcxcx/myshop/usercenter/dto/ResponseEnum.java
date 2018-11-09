package com.xcxcxcxcx.myshop.usercenter.dto;

/**
 * @author XCXCXCXCX
 * @date 2018/11/8
 * @comments
 */
public enum ResponseEnum {

    SUCCESS("0","操作成功"),
    FAILED("1","系统繁忙");


    private String code;

    private String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
}
