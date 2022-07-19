package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author ygg
 * @version v7.2.2
 * @Description 消息
 * @ClassName NftAlbum
 * @since v7.2.2 下午2:43 2022/4/20
 */
@TableName(value = "es_nft_msg")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Message implements Serializable {
    private static final long serialVersionUID = 4624054495142651977L;
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_GRAPH = 1;
    /**
     * 主键ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "member_id", value = "会员ID")
    private Long memberId;

    @ApiModelProperty(name = "conversation_id", value = "会话id")
    private Long conversationId;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type",value = "类型")
    private Integer type;

    /**
     * 内容
     */
    @ApiModelProperty(name = "content",value = "内容")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Long createTime=System.currentTimeMillis()/1000;

    public Message() {
    }

    public Message(Long memberId, Long conversationId, Integer type, String content) {
        this.memberId = memberId;
        this.conversationId = conversationId;
        this.type = type;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
