package com.xcxcxcxcx.myshop.divide.service;

/**
 * @author XCXCXCXCX
 * @date 2018/11/5
 * @comments
 */
public interface IPersistentTransactionService {


    //1).更新topic的current_amount
    //2).更新topic下的bill的status（status = 2表示已抢购）
    void updateTopicAndBill(Long topicId, int addAmount, Long billId, int oldStatus, int status);

}
