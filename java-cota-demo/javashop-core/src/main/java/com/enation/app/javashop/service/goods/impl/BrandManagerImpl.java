package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.goods.BrandMapper;
import com.enation.app.javashop.mapper.goods.CategoryBrandMapper;
import com.enation.app.javashop.mapper.goods.GoodsMapper;
import com.enation.app.javashop.model.errorcode.GoodsErrorCode;
import com.enation.app.javashop.model.goods.dos.BrandDO;
import com.enation.app.javashop.model.goods.dos.CategoryBrandDO;
import com.enation.app.javashop.model.goods.dos.GoodsDO;
import com.enation.app.javashop.model.goods.vo.SelectVO;
import com.enation.app.javashop.service.goods.BrandManager;
import com.enation.app.javashop.service.goods.GoodsGalleryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 品牌业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-16 16:32:46
 */
@Service
public class BrandManagerImpl implements BrandManager {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 查询品牌列表
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param name 品牌名称
     * @return WebPage 分页数据
     */
    @Override
    public WebPage list(long page, long pageSize, String name) {

        QueryWrapper<BrandDO> wrapper = new QueryWrapper<>();
        //以brandid倒叙
        wrapper.orderByDesc("brand_id");
        //如果名称不为空，则作为条件like-name
        wrapper.like(name != null,"name", name);
        //调用mapper进行分页的Map式查询
        IPage<BrandDO> iPage = brandMapper.selectPage(new Page<>(page, pageSize), wrapper);

        return PageConvert.convert(iPage);
    }

    /**
     * 添加品牌
     *
     * @param brand 品牌
     * @return Brand 品牌
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BrandDO add(BrandDO brand) {

        //检测名称重复
        this.checkSameName(brand.getName(), null);

        brand.setDisabled(1);
        this.brandMapper.insert(brand);

        return brand;
    }

    /**
     * 修改品牌
     *
     * @param brand 品牌
     * @param id 品牌主键
     * @return Brand 品牌
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BrandDO edit(BrandDO brand, Long id) {
        BrandDO brandDO = this.getModel(id);
        if (brandDO == null) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌不存在");
        }

        //检测名称重复
        this.checkSameName(brand.getName(), id);

        brand.setBrandId(id);
        this.brandMapper.updateById(brand);

        return brand;
    }

    /**
     * 删除品牌
     *
     * @param ids 品牌主键
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //检测是否有分类关联
        QueryWrapper<CategoryBrandDO> wrapper = new QueryWrapper<>();
        wrapper.in("brand_id", ids);
        Integer count = this.categoryBrandMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有分类关联，不能删除");
        }

        // 检测是否有商品关联
        QueryWrapper<GoodsDO> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.in("brand_id", ids).and(i -> {
            i.eq("disabled", 1).or().eq("disabled", 0);
        });
        int hasRel = this.goodsMapper.selectCount(goodsWrapper);

        if (hasRel > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有商品关联，不能删除");
        }

        this.brandMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 获取品牌
     *
     * @param id 品牌主键
     * @return Brand 品牌
     */
    @Override
    public BrandDO getModel(Long id) {

        return brandMapper.selectById(id);
    }

    /**
     * 查询某分类下的品牌
     *
     * @param categoryId 分类id
     * @return 品牌列表
     */
    @Override
    public List<BrandDO> getBrandsByCategory(Long categoryId) {

        return this.brandMapper.getBrandsByCategory(categoryId);

    }

    @Override
    public List<SelectVO> getCatBrand(Long categoryId) {

        //查询某个分类下的所有品牌
        List<SelectVO> selectVOS = brandMapper.getCatBrand(categoryId);

        //设置是否选中
        if (!selectVOS.isEmpty()) {
            for (SelectVO selectVO : selectVOS) {
                if (selectVO.getFalgid() != null && categoryId.toString().equals(selectVO.getFalgid())) {
                    selectVO.setSelected(true);
                } else {
                    selectVO.setSelected(false);
                }
            }
        }
        return selectVOS;

    }

    /**
     * 查询全部的品牌
     * @return 品牌列表
     */
    @Override
    public List<BrandDO> getAllBrands() {

        QueryWrapper<BrandDO> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("brand_id");

        return this.brandMapper.selectList(wrapper);
    }

    /**
     * 检测品牌名称是否重复
     *
     * @param name 品牌名称
     * @param id 品牌id
     */
    private void checkSameName(String name, Long id) {

        QueryWrapper<BrandDO> wrapper = new QueryWrapper<>();

        wrapper.eq("name", name);
        //如果id不为空
        wrapper.ne(id != null,"brand_id", id);

        List list = brandMapper.selectList(wrapper);
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌名称重复");
        }
    }
}