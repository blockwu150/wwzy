package com.enation.app.javashop.service.member;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.member.dos.AskMessageDO;

/**
 * 会员商品咨询消息业务接口
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public interface AskMessageManager {

    /**
     * 获取会员商品咨询消息分页列表数据
     * @param pageNo 页数
     * @param pageSize 每页记录数
     * @param isRead 是否已读 YES：是 NO：否
     * @return
     */
    WebPage list(Long pageNo, Long pageSize, String isRead);

    /**
     * 将会员商品咨询消息设置为已读
     * @param ids 消息id组
     */
    void read(Long[] ids);

    /**
     * 删除会员商品咨询消息
     * @param ids 消息id
     */
    void delete(Long[] ids);

    /**
     * 删除会员商品咨询消息
     * @param askId 会员商品咨询ID
     * @param memberId 会员id
     * @param msgType 消息类型 ASK：咨询 REPLY：回复
     */
    void delete(Long askId, Long memberId, String msgType);

    /**
     * 根据咨询ID删除会员消息
     * @param askId 咨询ID
     */
    void deleteByAskId(Long askId);

    /**
     * 获取会员商品咨询消息
     * @param id 消息id
     * @return
     */
    AskMessageDO getModel(Long id);

    /**
     * 会员商品咨询消息入库
     * @param askMessageDO 咨询消息
     */
    void addAskMessage(AskMessageDO askMessageDO);
}
