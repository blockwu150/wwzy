package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ygg
 * @version v7.2.2
 * @Description NFT会员
 * @ClassName NftMemberDO
 * @since v7.2.2 下午2:43 2020/8/5
 */
@TableName(value = "es_nft_member")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftMemberDO implements Serializable {
    private static final long serialVersionUID = 8590805008509737018L;
    /**
     * 会员ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(name = "member_id",value = "会员id")
    private Long memberId;

    /**
     * Cota 地址
     */
    @ApiModelProperty(name = "cota_address",value = "Cota 地址")
    private String cotaAddress;
    /**
     * 私钥
     */
    @ApiModelProperty(name = "private_key",value = "私钥")
    private String privateKey;
    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    private String remark;

    /**
     * 是否注册
     */
    @ApiModelProperty(name = "is_registry",value = "是否注册")
    private Boolean isRegistry;

    /**
     * 是否充值
     */
    @ApiModelProperty(name = "is_charged",value = "是否充值")
    private Boolean isCharged;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getCotaAddress() {
        return cotaAddress;
    }

    public void setCotaAddress(String cotaAddress) {
        this.cotaAddress = cotaAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getRegistry() {
        return isRegistry;
    }

    public void setRegistry(Boolean registry) {
        isRegistry = registry;
    }

    public Boolean getCharged() {
        return isCharged;
    }

    public void setCharged(Boolean charged) {
        isCharged = charged;
    }

    /*
     *richmodel
     */
    transient NftMemberDO referer;

    transient List<NftMemberDO> children =new LinkedList<NftMemberDO>();

    public NftMemberDO getReferer() {
        return referer;
    }

    public void setReferer(NftMemberDO referer) {
        this.referer = referer;
    }

    public List<NftMemberDO> getChildren() {
        return children;
    }

    public void setChildren(List<NftMemberDO> children) {
        this.children = children;
    }

}
