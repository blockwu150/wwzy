package com.enation.app.javashop.model.system.vo;

import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;

import java.io.Serializable;
import java.util.List;


/**
 * 菜单VO 用于读取所有的菜单
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-19 11:13:31
 */
public class MenusVO implements Serializable {

    /**
     * 菜单id
     */
    private Long id;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单url
     */
    private String url;
    /**
     * 菜单唯一标识
     */
    private String identifier;
    /**
     * 权限表达式
     */
    private String authRegular;
    /**
     * 删除标记
     */
    private Integer deleteFlag;
    /**
     * 子菜单
     */
    private List<MenusVO> children;

    @PrimaryKeyField
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAuthRegular() {
        return authRegular;
    }

    public void setAuthRegular(String authRegular) {
        this.authRegular = authRegular;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<MenusVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenusVO> children) {
        this.children = children;
    }


    @Override
    public String toString() {
        return "MenusVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", identifier='" + identifier + '\'' +
                ", authRegular='" + authRegular + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", children=" + children +
                '}';
    }
}
