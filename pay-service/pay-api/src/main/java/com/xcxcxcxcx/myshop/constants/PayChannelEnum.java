package com.xcxcxcxcx.myshop.constants;

/**
 * @author XCXCXCXCX
 * @date 2018/11/2
 * @comments
 */
public enum PayChannelEnum {

    WECHAT_PAY_CHANNEL("wechat_pay","微信支付"),
    WECHAT_TRANSFER_CHANNEL("wechat_transfer","微信转账"),
    ALI_PAY_CHANNEL("ali_pay","支付宝支付");

    private String code;

    private String channelName;

    PayChannelEnum(String code, String channelName) {
        this.code = code;
        this.channelName = channelName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
