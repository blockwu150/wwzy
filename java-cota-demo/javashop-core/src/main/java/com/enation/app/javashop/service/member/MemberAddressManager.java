package com.enation.app.javashop.service.member;

import com.enation.app.javashop.model.member.dos.MemberAddress;
import com.enation.app.javashop.framework.database.WebPage;

import java.util.List;

/**
 * 会员地址业务层
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018-03-18 15:37:00
 */
public interface MemberAddressManager {

    /**
     * 查询会员地址列表
     *
     * @return 地址集合
     */
    List<MemberAddress> list();

    /**
     * 查询会员地址列表
     *
     * @param page     页数
     * @param pageSize 每页显示数
     * @param memberId 会员id
     * @return
     */
    WebPage list(long page, long pageSize, Long memberId);

    /**
     * 添加会员地址
     *
     * @param memberAddress 会员地址
     * @return MemberAddress 会员地址
     */
    MemberAddress add(MemberAddress memberAddress);

    /**
     * 修改会员地址
     *
     * @param memberAddress 会员地址
     * @param id            会员地址主键
     * @return MemberAddress 会员地址
     */
    MemberAddress edit(MemberAddress memberAddress, Long id);

    /**
     * 删除会员地址
     *
     * @param id 会员地址主键
     */
    void delete(Long id);

    /**
     * 获取会员地址
     *
     * @param id 会员地址主键
     * @return MemberAddress  会员地址
     */
    MemberAddress getModel(Long id);

    /**
     * 获取会员默认地址
     *
     * @param memberId 会员id
     * @return 会员默认地址
     */
    MemberAddress getDefaultAddress(Long memberId);


    /**
     * 修改地址为默认
     *
     * @param id 地址的id
     */
    void editDefault(Long id);


}
