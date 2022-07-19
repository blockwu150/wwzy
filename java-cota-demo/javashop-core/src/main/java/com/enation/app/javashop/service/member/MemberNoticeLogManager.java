package com.enation.app.javashop.service.member;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.member.dto.MemberNoticeDTO;
import com.enation.app.javashop.model.member.dos.MemberNoticeLog;

import java.util.List;

/**
 * 会员站内消息历史业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-05 14:10:16
 */
public interface MemberNoticeLogManager {

    /**
     * 查询会员站内消息历史列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param read     是否已读,1已读 0未读
     * @return WebPage
     */
    WebPage list(long page, long pageSize, Integer read);

    /**
     * 添加会员站内消息历史
     *
     * @param content  消息内容
     * @param sendTime 发送时间
     * @param memberId 会员id
     * @param title    标题
     * @return 历史消息
     */
    MemberNoticeLog add(String content, long sendTime, Long memberId, String title);


    /**
     * 设置已读
     *
     * @param ids 消息id组
     */
    void read(Long[] ids);

    /**
     * 删除历史消息
     *
     * @param ids 消息id组
     */
    void delete(Long[] ids);

    /**
     * 获取会员站内未读消息数量信息
     * @return
     */
    MemberNoticeDTO getNum();

}
