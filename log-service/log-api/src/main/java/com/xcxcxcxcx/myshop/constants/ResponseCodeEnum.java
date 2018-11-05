package com.xcxcxcxcx.myshop.constants;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public enum ResponseCodeEnum {

    SUCCESS("00000000","操作成功"),
    SYSTEM_BUSY("00001002","系统繁忙"),
    REQUEST_PARAM_INCORRECT("00001005","请求参数不正确");

    private String code;

    private String msg;

    ResponseCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
