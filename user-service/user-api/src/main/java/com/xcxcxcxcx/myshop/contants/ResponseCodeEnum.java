package com.xcxcxcxcx.myshop.contants;

/**
 * @author XCXCXCXCX
 * @date 2018/10/21
 * @comments
 */
public enum ResponseCodeEnum {

    SUCCESS("00000000","操作成功"),
    USER_OR_PASSWORD_NOT_EXIST("00001001","用户名或密码不存在"),
    SYSTEM_BUSY("00001002","系统繁忙"),
    INVALID_TOKEN("00001003","无效的token"),
    SIGNATURE_VALIDATE_FAILED("00001004","签名验证失败"),
    REQUEST_PARAM_INCORRECT("00001005","请求参数不正确");


    private String code;

    private String msg;

    ResponseCodeEnum(String code,String msg){
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
