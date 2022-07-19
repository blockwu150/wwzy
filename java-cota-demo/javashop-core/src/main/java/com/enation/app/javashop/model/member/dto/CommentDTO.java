package com.enation.app.javashop.model.member.dto;

import com.enation.app.javashop.model.member.validator.GradeType;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 评论VO
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel(description = "会员评论vo")
public class CommentDTO  implements Serializable {

	@ApiModelProperty(name = "content", value = "评论内容", required = false)
	@Length(max = 500,message = "评论内容不能超过500字符")
	private String content;

	@ApiModelProperty(name = "grade", value = "好中差评", required = true,allowableValues = "good,neutral,bad")
	@GradeType
	private String grade;

	@ApiModelProperty(value = "会员评论的图片")
	private List<String> images;

	@ApiModelProperty(value = "会员评论商品规格id",name = "sku_id", required = true)
    private Long skuId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}


	@Override
	public String toString() {
		return "CommentDTO{" +
				"content='" + content + '\'' +
				", grade='" + grade + '\'' +
				", images=" + images +
				", skuId=" + skuId +
				'}';
	}
}
