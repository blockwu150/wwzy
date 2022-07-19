package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.app.javashop.client.member.ShopClient;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.validation.annotation.DemoSiteDisable;
import com.enation.app.javashop.mapper.goods.*;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.message.CategoryChangeMsg;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.GoodsErrorCode;
import com.enation.app.javashop.model.goods.dos.*;
import com.enation.app.javashop.model.goods.vo.CategoryPluginVO;
import com.enation.app.javashop.model.goods.vo.CategoryVO;
import com.enation.app.javashop.model.member.enums.CommonStatusEnum;
import com.enation.app.javashop.model.shop.vo.ShopVO;
import com.enation.app.javashop.service.goods.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 商品分类业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@Service
public class CategoryManagerImpl implements CategoryManager {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategorySpecMapper categorySpecMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Autowired
    private BrandMapper brandMapper;


    @Autowired
    protected Cache cache;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    protected ShopClient shopClient;

    /**
     * 获取缓存key
     *
     * @return
     */
    protected String getCacheKey() {
        return CachePrefix.GOODS_CAT.getPrefix() + "ALL";

    }

    /**
     * 添加商品分类
     *
     * @param category 商品分类
     * @return Category 商品分类
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @DemoSiteDisable
    public CategoryDO add(CategoryDO category) {

        CategoryDO parent = null;
        //不能添加重复的分类名称
        this.checkSameName(category.getName(), null);

        // 非顶级分类
        if (category.getParentId() != null && category.getParentId() != 0) {
            parent = this.checkExistCat(category.getParentId(), "父分类不存在");
            // 替换catPath 根据catPath规则来匹配级别
            String catPath = parent.getCategoryPath().replace("|", ",");
            String[] str = catPath.split(",");
            // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
            if (str.length >= 4) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
            }
        }

        this.categoryMapper.insert(category);
        Long categoryId = category.getCategoryId();

        // 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
        // 注意末尾都要加|，以防止查询子孙时出错
        // 不是顶级类别，有父
        if (parent != null) {
            category.setCategoryPath(parent.getCategoryPath() + categoryId + "|");
        } else {// 是顶级类别
            category.setCategoryPath("0|" + categoryId + "|");
        }

        UpdateWrapper<CategoryDO> updateWrapper = new UpdateWrapper<>();
        //设置分类路径
        updateWrapper.set("category_path", category.getCategoryPath());
        //按分类id修改
        updateWrapper.eq("category_id", categoryId);

        this.categoryMapper.update(new CategoryDO(), updateWrapper);

        //清空分类缓存
        cache.remove(getCacheKey());

        //发送分类变化消息
        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(categoryId, CategoryChangeMsg.ADD_OPERATION);
        this.messageSender.send(new MqMessage(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg));

        return category;
    }

    /**
     * 修改商品分类
     *
     * @param category 商品分类
     * @param id       商品分类主键
     * @return Category 商品分类
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @DemoSiteDisable
    public CategoryDO edit(CategoryDO category, Long id) {

        //验证分类是否存在
        CategoryDO catTemp = this.checkExistCat(id, "该分类不存在");

        //不能添加重复的分类名称
        this.checkSameName(category.getName(), id);

        category.setCategoryId(id);
        // 如果有子分类则不能更换上级分类
        // 更换上级分类
        if (!category.getParentId().equals(catTemp.getParentId())) {
            // 查看是否有子分类
            List list = this.list(id, null, null);
            if (list != null && list.size() > 0) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "当前分类有子分类，不能更换上级分类");
            } else {
                CategoryDO parent = this.checkExistCat(category.getParentId(), "父分类不存在");
                // 替换catPath 根据catPath规则来匹配级别
                String catPath = parent.getCategoryPath().replace("|", ",");
                String[] str = catPath.split(",");
                // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
                if (str.length >= 4) {
                    throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
                }
                category.setCategoryPath(parent.getCategoryPath() + category.getCategoryId() + "|");
            }
        }

        this.categoryMapper.updateById(category);

        //清空分类缓存
        cache.remove(getCacheKey());

        //发送分类变化消息
        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(id, CategoryChangeMsg.UPDATE_OPERATION);
        this.messageSender.send(new MqMessage(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg));

        return category;
    }

    /**
     * 删除商品分类
     *
     * @param id 商品分类主键
     */
    @Override
    @DemoSiteDisable
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Long id) {

        List<CategoryVO> list = this.listAllChildren(id);
        if (list != null && list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在子类别不能删除");
        }
        // 检测是否有商品关联
        QueryWrapper<GoodsDO> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.eq("category_id", id).ne("disabled", -1);
        int count = this.goodsMapper.selectCount(goodsWrapper);

        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在商品不能删除");
        }

        //清空缓存
        cache.remove(CachePrefix.GOODS_CAT.getPrefix() + id);
        cache.remove(getCacheKey());

        this.categoryMapper.deleteById(id);
        //删除该分类下的规格和品牌
        Map map = new HashMap();
        map.put("category_id", id);
        this.categorySpecMapper.deleteByMap(map);
        this.categoryBrandMapper.deleteByMap(map);
    }

    /**
     * 获取商品分类
     *
     * @param id 商品分类主键
     * @return Category 商品分类
     */
    @Override
    public CategoryDO getModel(Long id) {

        return this.categoryMapper.selectById(id);
    }

    /**
     * 查询某分类下的子分类
     *
     * @param parentId 父id，顶级为0
     * @param format   类型，如果值是plugin则查询插件使用的格式数据
     * @param isShow   是否显示 YES：是，NO：否
     * @return 分类列表
     */
    @Override
    public List list(Long parentId, String format, String isShow) {
        if (parentId == null) {
            return null;
        }

        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("category_order");
        //根据父分类id查询
        wrapper.eq("parent_id", parentId);
        //如果显示条件不为空
        wrapper.eq(StringUtil.notEmpty(isShow), "is_show", isShow);

        List<CategoryDO> list = this.categoryMapper.selectList(wrapper);

        if (format != null) {
            //DO转为VO
            List<CategoryPluginVO> resList = list.stream().map(categoryDO -> new CategoryPluginVO(categoryDO))
                    .collect(toList());
            return resList;
        } else {

            //设置每个分类的子分类
            List<CategoryVO> resList = list.stream().map(category -> {
                CategoryVO categoryVO = new CategoryVO(category);
                List<CategoryVO> listAllChildren = this.listChildren(category.getCategoryId());
                if (StringUtil.isNotEmpty(listAllChildren)) {
                    categoryVO.setChildren(listAllChildren);
                }
                return categoryVO;
            }).collect(toList());

            return resList;
        }
    }

    /**
     * 获取某个类别的所有子类
     *
     * @param parentId 分类父id
     * @return 分类id
     */
    protected List<CategoryVO> listChildren(Long parentId) {
        // 从缓存取所有的分类
        List<CategoryDO> list = (List<CategoryDO>) cache.get(getCacheKey());
        if (list == null) {
            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryVO> topCatList = new ArrayList<CategoryVO>();

        for (CategoryDO cat : list) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentId) == 0) {
                topCatList.add(categoryVo);
            }
        }
        return topCatList;
    }

    /**
     * 初始化分类缓存
     *
     * @return 分类列表
     */
    protected List<CategoryDO> initCategory() {
        //数据库读取分类列表
        List<CategoryDO> list = this.getCategoryList();
        //将分类存入redis缓存
        if (list != null && list.size() > 0) {
            for (CategoryDO cat : list) {
                cache.put(CachePrefix.GOODS_CAT.getPrefix() + cat.getCategoryId(), cat);
            }
            cache.put(getCacheKey(), list);
        }
        return list;
    }

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    protected List<CategoryDO> getCategoryList() {
        // 不能修改为缓存读取
        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("category_order").eq("is_show", CommonStatusEnum.YES.value());

        List<CategoryDO> list = this.categoryMapper.selectList(wrapper);
        return list;
    }

    /**
     * 查询所有的分类，父子关系
     *
     * @param parentId 父id，顶级为0
     * @return 分类列表
     */
    @Override
    public List<CategoryVO> listAllChildren(Long parentId) {
        // 从缓存取所有的分类
        List<CategoryDO> list = (List<CategoryDO>) cache.get(getCacheKey());
        if (list == null) {

            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryVO> topCatList = new ArrayList<CategoryVO>();

        for (CategoryDO cat : list) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentId) == 0) {
                //获取该分类的子分类
                List<CategoryVO> children = this.getChildren(list, cat.getCategoryId());
                categoryVo.setChildren(children);
                topCatList.add(categoryVo);
            }
        }
        return topCatList;
    }

    /**
     * 查询分类树
     *
     * @return 分类树
     */
    @Override
    public List<CategoryVO> listAllSellerChildren() {

        //当前卖家
        Seller seller = UserContext.getSeller();

        //获取店铺信息
        ShopVO shop = shopClient.getShop(seller.getSellerId());

        //获取店铺经营类目
        String goodsManagementCategory = shop.getGoodsManagementCategory();

        //该店铺经营的所有分类
        String[] categoryId = goodsManagementCategory.split(",");

        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("category_order");

        //查询该店铺经营的所有分类，包括子分类
        if (categoryId.length > 0) {
            for (String category : categoryId) {
                wrapper.or().likeRight("category_path", "0|" + category);
            }
        }
        List<CategoryDO> list = this.categoryMapper.selectList(wrapper);

        //获取父分类为一级分类的分类
        List<CategoryVO> vos = new ArrayList<>();
        for (CategoryDO categoryDO : list) {
            if (categoryDO.getParentId() == 0) {
                vos.add(new CategoryVO(categoryDO));
            }
        }
        vos = reSet(list, vos);

        return vos;
    }

    protected List<CategoryVO> reSet(List<CategoryDO> list, List<CategoryVO> vos) {
        //递归遍历，获取每个分类的子分类
        for (CategoryVO vo : vos) {
            List<CategoryVO> child = new ArrayList<>();
            for (CategoryDO cat : list) {
                if (cat.getParentId().equals(vo.getCategoryId())) {
                    CategoryVO categoryVo = new CategoryVO(cat);
                    child.add(categoryVo);
                }
            }
            if (child.size() > 0) {
                vo.setChildren(child);
                this.reSet(list, child);
            }
        }
        return vos;
    }

    /**
     * 得到当前分类的子孙
     *
     * @param catList  分类集合
     * @param parentid 父分类id
     * @return 带子分类的集合
     */
    protected List<CategoryVO> getChildren(List<CategoryDO> catList, Long parentid) {
        List<CategoryVO> children = new ArrayList<CategoryVO>();
        for (CategoryDO cat : catList) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentid) == 0) {
                categoryVo.setChildren(this.getChildren(catList, cat.getCategoryId()));
                children.add(categoryVo);
            }
        }
        return children;
    }

    /**
     * 获取卖家经营类目对应的分类
     *
     * @param categoryId 分类id
     * @return 分类列表
     */
    @Override
    public List<CategoryDO> getSellerCategory(Long categoryId) {
        String sql = "";
        Seller seller = UserContext.getSeller();

        //店铺详情
        ShopVO shop = shopClient.getShop(seller.getSellerId());

        //店铺经营类目
        String goodsManagementCategory = shop.getGoodsManagementCategory();


        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        //拼接父分类id查询条件
        wrapper.eq("parent_id", categoryId).eq("is_show", CommonStatusEnum.YES.value());
        if (categoryId == 0 && StringUtil.notEmpty(goodsManagementCategory)) {
            wrapper.orderByAsc("category_order");
            //查询店铺经营类目的所有分类
            wrapper.in("category_id", goodsManagementCategory.split(","));
        }
        return this.categoryMapper.selectList(wrapper);

    }

    /**
     * 保存分类绑定的品牌
     *
     * @param categoryId   分类id
     * @param chooseBrands 选择的品牌
     * @return 分类品牌列表
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<CategoryBrandDO> saveBrand(Long categoryId, Long[] chooseBrands) {

        CategoryDO category = this.getModel(categoryId);
        if (category == null) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
        }
        List<CategoryBrandDO> res = new ArrayList<>();
        if (chooseBrands == null) {

            Map map = new HashMap<>();
            map.put("category_id", categoryId);
            categoryBrandMapper.deleteByMap(map);
            return res;
        }
        //查看所选品牌是否存在
        QueryWrapper<BrandDO> wrapper = new QueryWrapper<>();
        wrapper.in("brand_id", chooseBrands);
        Integer count = brandMapper.selectCount(wrapper);

        if (count < chooseBrands.length) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "品牌传参错误");
        }
        //先删除该分类下所有品牌
        Map map = new HashMap<>();
        map.put("category_id", categoryId);
        this.categoryBrandMapper.deleteByMap(map);

        //再新增品牌
        for (Long brand : chooseBrands) {
            if (brand != null) {
                CategoryBrandDO categoryBrand = new CategoryBrandDO(categoryId, brand);
                this.categoryBrandMapper.insert(categoryBrand);
                res.add(categoryBrand);
            }
        }
        return res;
    }

    /**
     * 保存分类绑定的规格
     *
     * @param categoryId  分类id
     * @param chooseSpecs 选择的规格
     * @return 分类规格列表
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<CategorySpecDO> saveSpec(Long categoryId, Long[] chooseSpecs) {

        //验证分类是否存在
        this.checkExistCat(categoryId, "该分类不存在");

        List<CategorySpecDO> res = new ArrayList<>();
        Map map = new HashMap<>();
        map.put("category_id", categoryId);
        if (chooseSpecs == null) {
            categorySpecMapper.deleteByMap(map);
            return res;
        }

        //查看所选规格是否存在
        QueryWrapper<SpecificationDO> wrapper = new QueryWrapper<>();
        wrapper.in("spec_id", chooseSpecs);
        Integer count = specificationMapper.selectCount(wrapper);
        if (count < chooseSpecs.length) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "规格绑定传参错误:规格数量不一致");
        }

        //先删除该分类下的所有规格
        categorySpecMapper.deleteByMap(map);

        //再添加规格
        for (Long specId : chooseSpecs) {
            CategorySpecDO categorySpec = new CategorySpecDO(categoryId, specId);
            this.categorySpecMapper.insert(categorySpec);
            res.add(categorySpec);
        }
        return res;
    }

    /**
     * 查询分类名称，带父分类>拼接
     *
     * @param categoryId 分类id
     * @return 分类名称
     */
    @Override
    public Map queryCatNameAndIs(Long categoryId) {

        Map map = new HashMap<>();
        //获取分类DO
        CategoryDO category = this.getModel(categoryId);
        //转换为数组
        String[] ids = (category.getCategoryPath().replace("|", ",") + "-1) ").split(",");
        //获取父类（含自己）的分类集合
        List<CategoryDO> list = this.categoryMapper.selectList(new QueryWrapper<CategoryDO>()
                .in("category_id", Arrays.asList(ids))
                .orderByAsc("category_id"));
        //父类（含自己）的分类名
        String categoryName = "";
        //创建id数组
        Long[] catids = new Long[ids.length - 1];
        int i = 0;
        if (StringUtil.isNotEmpty(list)) {
            for (String idString : ids) {
                for (CategoryDO categoryDo : list) {
                    if (idString.equals(categoryDo.getCategoryId().toString())) {
                        if ("".equals(categoryName)) {
                            categoryName = " " + categoryDo.getName();
                        } else {
                            categoryName += ">" + categoryDo.getName() + " ";
                        }
                        catids[i] = categoryDo.getCategoryId();
                        i++;
                    }
                }
            }
        }
        map.put("catName", categoryName);
        map.put("catIds", catids);
        return map;
    }

    /**
     * 检测分类名称是否重复
     *
     * @param name 分类名称
     * @param id   分类id
     */
    private void checkSameName(String name, Long id) {

        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();

        //拼接分类名称查询条件
        wrapper.eq("name", name);

        //如果id不为空，则为编辑，过滤掉该分类
        if (id != null) {
            wrapper.ne("category_id", id);
        }

        List list = categoryMapper.selectList(wrapper);
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类名称已存在");
        }
    }

    /**
     * 验证分类是否存在，并给出提示
     *
     * @param id      分类id
     * @param message 提示信息
     */
    private CategoryDO checkExistCat(Long id, String message) {

        CategoryDO model = this.getModel(id);
        if (model == null) {
            throw new ServiceException(GoodsErrorCode.E300.code(), message);
        }

        return model;
    }

}
