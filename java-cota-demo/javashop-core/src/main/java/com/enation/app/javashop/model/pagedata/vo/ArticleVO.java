package com.enation.app.javashop.model.pagedata.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 文章实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-12 10:43:18
 */
@Table(name = "es_article")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = 5105404520203401L;

    /**
     * 主键
     */
    @Id(name = "article_id")
    @ApiModelProperty(hidden = true)
    private Long articleId;
    /**
     * 文章名称
     */
    @Column(name = "article_name")
    @ApiModelProperty(name = "article_name", value = "文章名称")
    private String articleName;

    /**
     * 分类id
     */
    @Column(name = "category_id")
    @ApiModelProperty(name = "category_id", value = "分类id", required = false)
    private Long categoryId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ArticleVO{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
