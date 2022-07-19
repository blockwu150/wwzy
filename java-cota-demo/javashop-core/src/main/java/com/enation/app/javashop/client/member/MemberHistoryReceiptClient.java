package com.enation.app.javashop.client.member;

import com.enation.app.javashop.model.member.dos.ReceiptHistory;
import com.enation.app.javashop.model.member.vo.ReceiptHistoryVO;

/**
 * 会员发票历史查询客户端
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午2:55
 * @since v7.0
 */

public interface MemberHistoryReceiptClient {

    /**
     * 根据订单sn查询历史发票信息
     *
     * @param orderSn 订单sn
     * @return 历史发票信息
     */
    ReceiptHistoryVO getReceiptHistory(String orderSn);

    /**
     * 添加发票历史
     *
     * @param receiptHistory 发票历史
     * @return ReceiptHistory 发票历史
     */
    ReceiptHistory add(ReceiptHistory receiptHistory);

    /**
     * 修改发票历史记录
     * @param receiptHistory
     * @return
     */
    ReceiptHistory edit(ReceiptHistory receiptHistory);

    /**
     * 修改该订单的发票的订单金额
     * @param orderPrice
     * @param orderSn
     */
    void updatePriceByOrderSn(Double orderPrice,String orderSn);

}
