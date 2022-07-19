package com.enation.app.javashop.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.payment.BankVoucherMapper;
import com.enation.app.javashop.model.payment.dos.BankVoucherDO;
import com.enation.app.javashop.service.payment.BankVoucherManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BankVoucherManagerImpl implements BankVoucherManager {
    @Autowired
    private BankVoucherMapper bankVoucherMapper;
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public WebPage list(Long pageNo, Long pageSize, Long buyerId, Integer isPay, Long createTime) {
        return list(pageNo,pageSize,buyerId,null,isPay,createTime);
    }

    @Override
    public WebPage list(Long pageNo, Long pageSize, Long buyerId,String mobile, Integer isPay, Long createTime) {

        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        if(createTime != null) {
            wrapper.between(createTime != null,"b.create_time",createTime/1000,createTime/1000 + 24*3600);
        }
        wrapper.eq(buyerId != null, "b.buyer_id", buyerId)
                .eq(isPay != null,"b.is_pay", isPay )
                .like(mobile != null,"m.mobile", mobile )
                .orderByDesc("b.id");
        return PageConvert.convert(bankVoucherMapper.pageBankVoucher(page,wrapper));
    }

    @Override
    public BankVoucherDO add(BankVoucherDO bankVoucher) {
        bankVoucher.setCreateTime(System.currentTimeMillis()/1000);
        bankVoucherMapper.insert(bankVoucher);
        return bankVoucher;
    }

    @Override
    public BankVoucherDO edit(BankVoucherDO bankVoucher, Long id) {
        bankVoucher.setId(id);
        bankVoucherMapper.updateById(bankVoucher);
        return bankVoucher;
    }

    @Override
    public void delete(Long id) {
        bankVoucherMapper.deleteById(id);
    }

    @Override
    public BankVoucherDO getModel(Long id) {
        return bankVoucherMapper.selectById(id);
    }

    @Override
    public List<BankVoucherDO> exportExcel(Long buyerId, Integer isPay, Long createTime) {
        List<BankVoucherDO> list = new QueryChainWrapper<>(bankVoucherMapper)
                .eq(buyerId != null, "buyer_id", buyerId)
                .eq(isPay != null,"is_pay", isPay )
                .eq(createTime != null,"create_time",createTime)
                .orderByDesc("id")
                //分页查询
                .list();
        return list;
    }
}
