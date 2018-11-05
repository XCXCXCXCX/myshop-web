package com.xcxcxcxcx.myshop.divide.service.impl;

import com.xcxcxcxcx.myshop.divide.dal.persistence.BillMapper;
import com.xcxcxcxcx.myshop.divide.dal.persistence.TopicMapper;
import com.xcxcxcxcx.myshop.divide.exception.ValidateException;
import com.xcxcxcxcx.myshop.divide.service.IPersistentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XCXCXCXCX
 * @date 2018/11/5
 * @comments
 */
@Service("persistentTransactionService")
public class PersistentTransactionServiceImpl implements IPersistentTransactionService{

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    BillMapper billMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTopicAndBill(Long topicId, int addAmount, Long billId, int oldStatus, int status) {

        int row1 = billMapper.updateBillStatus(billId,oldStatus,status);

        if(row1 < 1){
            throw new ValidateException("billId = " + billId + ":状态有误");
        }

        int row2 = topicMapper.updateTopicCurrentAmount(topicId,addAmount);

        if(row2 < 1){
            throw new ValidateException("topicId = " + topicId + ":currentAmount更新失败");
        }

    }
}
