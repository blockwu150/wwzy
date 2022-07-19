package com.enation.app.javashop.service.payment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.test.TestConfig;
import com.enation.app.javashop.mapper.goods.GoodsMapper;
import com.enation.app.javashop.mapper.payment.PayLogMapper;
import com.enation.app.javashop.model.goods.dos.GoodsDO;
import com.enation.app.javashop.model.goods.dto.GoodsQueryParam;
import com.enation.app.javashop.model.trade.order.dos.PayLog;
import com.enation.app.javashop.service.goods.GoodsQueryManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收款单业务层测试
 * @author zs
 * @version 1.0
 * @since 7.2.2
 * 2020/07/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@MapperScan(basePackages = "com.enation.app.javashop.mapper")
public class PayLogManagerTest {

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsQueryManager goodsQueryManager;
    @Autowired
    private JavashopConfig javashopConfig;

    @Test
    public void add() {

        PayLog payLog = new PayLog();
        payLog.setOrderSn("xxxxxxaaaa");

        payLogManager.add(payLog);
    }

    @Test
    public void edit() {

        PayLog payLog = new PayLog();
        payLog.setOrderSn("xxxxxxaaaa哈哈");

        payLogManager.edit(payLog, 1288403695152320513l);
    }


    @Test
    public void delete() {

        payLogManager.delete(1288403695152320513l);
    }

    @Test
    public void getModel() {

        PayLog model = payLogManager.getModel(24418821716742168l);
        System.out.println(model);
    }

    @Test
    public void getModel1() {

        PayLog model = payLogManager.getModel("29046245100371970");
        System.out.println(model);
    }

    @Test
    public void test1() {

        GoodsQueryParam goodsQueryParam = new GoodsQueryParam();

        goodsQueryParam.setPageNo(1L);
        goodsQueryParam.setPageSize(10L);

        System.out.println(goodsQueryManager.list(goodsQueryParam));

//        Map param = new HashMap();
//        param.put("enable_quantity", 44);
//        param.put("quantity", 55);
//        param.put("goods_id", 1295180068793155585L);
//        this.goodsMapper.updateQuantity(param);

        new UpdateChainWrapper<>(goodsMapper)
                .set("enable_quantity", 101)
                .set("quantity", 109)
                .eq("goods_id", 1295180068793155585L)
                .update();



        System.out.println(goodsQueryManager.list(goodsQueryParam));
//        Map param = new HashMap();
//        param.put("enable_quantity", 27);
//        param.put("quantity", 28);
//        param.put("goods_id", 1295245501688819714L);
//        this.goodsMapper.updateQuantity(param);
//
//        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("goods_id", 1295245501688819714L);
//
//        IPage<Map<String, Object>> page =
//                goodsMapper.selectMapsPage(new Page(1, 10), queryWrapper);
//
//        System.out.println(page.getRecords());
    }

}
