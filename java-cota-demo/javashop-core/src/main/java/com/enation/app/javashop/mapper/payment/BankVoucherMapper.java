package com.enation.app.javashop.mapper.payment;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.payment.dos.BankVoucherDO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 收款单的Mapper
 * @author zs
 * @version 1.0
 * @since 7.2.2
 * 2020/07/29
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface BankVoucherMapper extends BaseMapper<BankVoucherDO> {
    @Select({"select b.*,m.mobile\n" +
            "from es_payment_bank_voucher b inner join es_member m on m.member_id=b.buyer_id ${ew.customSqlSegment}"})
    IPage<Map> pageBankVoucher(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
