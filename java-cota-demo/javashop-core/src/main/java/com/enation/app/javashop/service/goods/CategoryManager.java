package com.enation.app.javashop.service.goods;

import com.enation.app.javashop.model.goods.dos.CategoryBrandDO;
import com.enation.app.javashop.model.goods.dos.CategoryDO;
import com.enation.app.javashop.model.goods.dos.CategorySpecDO;
import com.enation.app.javashop.model.goods.vo.CategoryVO;

import java.util.List;
import java.util.Map;

/**
 * 商品分类业务层
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
public interface CategoryManager {

    /**
     * 添加商品分类
     *
     * @param category 商品分类
     * @return Category 商品分类
     */
    CategoryDO add(CategoryDO category);

    /**
     * 修改商品分类
     *
     * @param category 商品分类
     * @param id       商品分类主键
     * @return Category 商品分类
     */
    CategoryDO edit(CategoryDO category, Long id);

    /**
     * 删除商品分类
     *
     * @param id 商品分类主键
     */
    void delete(Long id);

    /**
     * 获取商品分类
     *
     * @param id 商品分类主键
     * @return Category 商品分类
     */
    CategoryDO getModel(Long id);

    /**
     * 查询某分类下的子分类
     *
     * @param parentId 父id，顶级为0
     * @param format   类型，如果值是plugin则查询插件使用的格式数据
     * @param isShow   是否显示 YES：是，NO：否
     * @return 分类列表
     */
    List list(Long parentId, String format, String isShow);

    /**
     * 获取卖家经营类目对应的分类
     *
     * @param categoryId 分类id
     * @return 分类列表
     */
    List<CategoryDO> getSellerCategory(Long categoryId);

    /**
     * 查询所有的分类，父子关系
     *
     * @param parentId 父id，顶级为0
     * @return 分类列表
     */
    List<CategoryVO> listAllChildren(Long parentId);


    /**
     * 查询分类树
     *
     * @return 分类树
     */
    List<CategoryVO> listAllSellerChildren();

    /**
     * 保存分类绑定的品牌
     *
     * @param categoryId   分类id
     * @param chooseBrands 选择的品牌
     * @return 分类品牌列表
     */
    List<CategoryBrandDO> saveBrand(Long categoryId, Long[] chooseBrands);

    /**
     * 保存分类绑定的规格
     *
     * @param categoryId  分类id
     * @param chooseSpecs 选择的规格
     * @return 分类规格列表
     */
    List<CategorySpecDO> saveSpec(Long categoryId, Long[] chooseSpecs);

    /**
     * 查询分类名称和ids，带父分类>拼接
     *
     * @param categoryId 分类id
     * @return Map 分类名称和ids
     */
    Map queryCatNameAndIs(Long categoryId);
}
