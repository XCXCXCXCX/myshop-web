package com.xcxcxcxcx.myshop.usercenter.dto;

/**
 * @author XCXCXCXCX
 * @date 2018/11/8
 * @comments
 */
public class ResponseData<T> {

    private String code;

    private String message;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static <T> ResponseData buildSuccess(T data){
        ResponseData responseData = new ResponseData();
        responseData.setCode(ResponseEnum.SUCCESS.getCode());
        responseData.setMessage(ResponseEnum.SUCCESS.getMsg());
        responseData.setData(data);
        return responseData;
    }

    public static <T> ResponseData buildFailed(String code, String msg, T data){
        ResponseData responseData = new ResponseData();
        responseData.setCode(code);
        responseData.setMessage(msg);
        responseData.setData(data);
        return responseData;
    }

    public static <T> ResponseData buildFailed(T data){
        return buildFailed(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),data);
    }
}
