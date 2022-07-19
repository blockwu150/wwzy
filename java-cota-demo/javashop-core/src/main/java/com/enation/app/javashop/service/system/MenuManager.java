package com.enation.app.javashop.service.system;

import com.enation.app.javashop.model.system.dos.Menu;
import com.enation.app.javashop.model.system.vo.MenuVO;
import com.enation.app.javashop.model.system.vo.MenusVO;
import com.enation.app.javashop.framework.database.WebPage;

import java.util.List;

/**
 * 菜单管理业务层
 *
 * @author zh
 * @version v7.0
 * @since v7.0.0
 * 2018-06-19 09:46:02
 */
public interface MenuManager {

    /**
     * 查询菜单管理列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return WebPage
     */
    WebPage list(long page, long pageSize);

    /**
     * 添加菜单管理
     *
     * @param menu 菜单管理
     * @return Menu 菜单管理
     */
    Menu add(MenuVO menu);

    /**
     * 修改菜单管理
     *
     * @param menu 菜单管理
     * @param id   菜单管理主键
     * @return Menu 菜单管理
     */
    Menu edit(Menu menu, Long id);

    /**
     * 删除菜单管理
     *
     * @param id 菜单管理主键
     */
    void delete(Long id);

    /**
     * 获取菜单管理
     *
     * @param id 菜单管理主键
     * @return MenuVO  菜单管理
     */
    Menu getModel(Long id);

    /**
     * 根据id获取菜单集合
     *
     * @param id 菜单的id
     * @return
     */
    List<MenusVO> getMenuTree(Long id);

    /**
     * 获取菜单管理
     *
     * @param identifier 菜单的唯一标识
     * @return MenuVO  菜单管理
     */
    Menu getMenuByIdentifier(String identifier);

}
