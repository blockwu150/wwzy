package com.enation.app.javashop.model.pagedata.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


/**
 * 文章分类实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-11 15:01:32
 */
@Table(name = "es_article_category")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleCategoryVO implements Serializable {

    private static final long serialVersionUID = 5257682283507488L;

    /**
     * 主键
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parent_id", value = "父分类id", required = false)
    private Long parentId;
    /**
     * 分类名称
     */
    @Column(name = "name")
    @ApiModelProperty(name = "name", value = "分类名称", required = false)
    private String name;

    @ApiModelProperty(name = "children", value = "子分类", required = false)
    private List<ArticleCategoryVO> children;

    //文章实体
    private List<ArticleVO> articles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleCategoryVO> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleCategoryVO> children) {
        this.children = children;
    }

    public List<ArticleVO> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleVO> articles) {
        this.articles = articles;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "ArticleCategoryVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", children=" + children +
                ", articles=" + articles +
                '}';
    }
}
