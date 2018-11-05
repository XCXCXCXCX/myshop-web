package com.xcxcxcxcx.myshop.divide.dal.persistence;

import com.xcxcxcxcx.myshop.divide.dal.entity.Bill;

import java.util.List;

/**
 * @author XCXCXCXCX
 * @date 2018/10/31
 * @comments
 */
public interface BillMapper {

    int insertBill(Bill bill);

    Bill getBillByTopicidAndUserid(Long topicId, Long userId);

    List<Bill> getBillByTopicid(Long topicId);

    int updateBillStatus(Long billId, int oldStatus, int status);

    Bill getBillByBillid(Long billId);
}
