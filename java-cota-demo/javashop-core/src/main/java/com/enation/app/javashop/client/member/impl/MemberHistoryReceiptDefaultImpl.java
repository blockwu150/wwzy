package com.enation.app.javashop.client.member.impl;

import com.enation.app.javashop.client.member.MemberHistoryReceiptClient;
import com.enation.app.javashop.model.member.dos.ReceiptHistory;
import com.enation.app.javashop.model.member.vo.ReceiptHistoryVO;
import com.enation.app.javashop.service.member.ReceiptHistoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 会员历史默认发票实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午2:57
 * @since v7.0
 */

@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class MemberHistoryReceiptDefaultImpl implements MemberHistoryReceiptClient {

    @Autowired
    private ReceiptHistoryManager receiptHistoryManager;

    @Override
    public ReceiptHistoryVO getReceiptHistory(String orderSn) {
        return receiptHistoryManager.getReceiptHistory(orderSn);
    }

    @Override
    public ReceiptHistory add(ReceiptHistory receiptHistory) {
        return receiptHistoryManager.add(receiptHistory);
    }

    @Override
    public ReceiptHistory edit(ReceiptHistory receiptHistory) {
        return receiptHistoryManager.edit(receiptHistory, receiptHistory.getHistoryId());
    }

    @Override
    public void updatePriceByOrderSn(Double orderPrice, String orderSn) {
        receiptHistoryManager.updatePriceByOrderSn(orderPrice, orderSn);
    }
}
