package com.xcxcxcxcx.myshop.pay.payment.context;

/**
 * @author XCXCXCXCX
 * @date 2018/11/3
 * @comments
 */
public class WechatTransferContext extends WechatPayContext{

    //用户账户
    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}
