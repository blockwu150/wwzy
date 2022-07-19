package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.nervos.ckb.utils.address.AddressTools;

import java.io.Serializable;

/**
 * @author ygg
 * @version v7.2.2
 * @Description ckb lock hash
 * @ClassName LockHash
 * @since v7.2.2 下午2:43 2022/4/21
 */
@TableName(value = "es_nft_state")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftState implements Serializable {

    private static final long serialVersionUID = 324595847609143309L;

    public final static Integer TYPE_GENESIS = 1;//创世
    public final static Integer TYPE_COMPOND = 2;//高合

    /**
     * 主键ID
     */
    @TableId(type= IdType.INPUT)
    @ApiModelProperty(name = "nft_key",value = "nft key (cotaId + tokenIndex)")
    private String nftKey;
    /**
     * 类型
     */
    @ApiModelProperty(name = "type",value = "类型")
    private Integer type;

    /**
     * 定义次数
     */
    @ApiModelProperty(name = "define_num",value = "定义次数")
    private Integer defineNum;
    /**
     * 挂单次数
     */
    @ApiModelProperty(name = "sell_num",value = "挂单次数")
    private Integer sellNum;


    public NftState() {
    }
    public NftState(String cotaId,String tokenIndex,int type) {
        setNftKey(cotaId+tokenIndex);
        setType(type);
    }

    public String getNftKey() {
        return nftKey;
    }

    public void setNftKey(String nftKey) {
        this.nftKey = nftKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDefineNum() {
        return defineNum;
    }

    public void setDefineNum(Integer defineNum) {
        this.defineNum = defineNum;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

}
