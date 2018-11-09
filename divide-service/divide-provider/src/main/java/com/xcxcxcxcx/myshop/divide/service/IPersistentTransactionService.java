package com.xcxcxcxcx.myshop.divide.service;

/**
 * @author XCXCXCXCX
 * @date 2018/11/5
 * @comments
 */
public interface IPersistentTransactionService {


    void updateBillAmountAndStatus(Long billId, int reduceAmount, int oldStatus, int status);

}