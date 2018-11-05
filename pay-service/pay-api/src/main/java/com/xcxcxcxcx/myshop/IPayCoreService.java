package com.xcxcxcxcx.myshop;

import com.xcxcxcxcx.myshop.dto.*;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public interface IPayCoreService {

    //预支付,创建支付订单写入本地数据库
    PaymentResponse prePay(PaymentRequest request);

    //回调，用于获取支付状态，修改本地数据库中支付订单状态
    PaymentNotifyResponse payCallback(PaymentNotifyRequest request);

    //转账给客户
    PaymentTransferResponse transfer(PaymentTransferRequest request);

    //转账回调通知
    PaymentNotifyResponse transferNotify(PaymentNotifyRequest request);

}
