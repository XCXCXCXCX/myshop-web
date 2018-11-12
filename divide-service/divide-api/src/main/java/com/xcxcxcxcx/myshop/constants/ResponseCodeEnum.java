package com.xcxcxcxcx.myshop.constants;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public enum ResponseCodeEnum {

    SUCCESS("00000000","操作成功"),
    SYSTEM_BUSY("00001002","系统繁忙"),
    REQUEST_PARAM_INCORRECT("00001005","请求参数不正确"),
    TOPIC_NOT_EXIST("00002001","不存在topic"),
    TOPIC_HAS_EXPIRED("00002002","topic已失效"),
    TOPIC_ERROR_STATUS("00002003","topic状态有误"),
    BILL_NOT_EXIST("00002004","账单不存在"),
    BILL_EXISTED("00002005","账单已存在"),
    BILL_ERROR_STATUS("00002006","账单状态有误"),
    SETTLE_ACCOUNT_ERROR("00003001","对账有误");

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
