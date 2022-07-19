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
 * @Description Nft配置
 * @ClassName NftConfig
 * @since v7.2.2 下午2:43 2020/8/5
 */
@TableName(value = "es_nft_config")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftConfig implements Serializable {

    private static final long serialVersionUID = -3229683091168548917L;
    /**
     * 配置ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(name = "id",value = "id")
    private Long id = 1l;
    /**
     * app版本
     */
    @ApiModelProperty(name = "wap_version",value = "wap版本")
    private String wapVersion = "0.0.0";
    /**
     * 管理账户Id
     */
    @ApiModelProperty(name = "manager_id",value = "管理账户Id")
    private Long managerId;

    /**
     * nft兑换管理账户Id
     */
    @ApiModelProperty(name = "changable_manager_id",value = "nft兑换管理账户Id")
    private Long changableManagerId;

    /**
     * 合成收藏品Id
     */
    @ApiModelProperty(name = "merge_gift_collection_id",value = "合成收藏品Id")
    private Long mergeGiftCollectionId;
    /**
     * 客户自定义专辑Id
     */
    @ApiModelProperty(name = "custom_album_id",value = "客户自定义专辑Id")
    private Long customAlbumId;

    /**
     *创世cotaId
     */
    @ApiModelProperty(name = "genesis_cota_id",value = "创世cotaId")
    private String genesisCotaId;

    /**
     *创世定义次数
     */
    @ApiModelProperty(name = "genesis_define_num",value = "创世定义次数")
    private Integer genesisDefineNum;
    /**
     *创世挂卖次数
     */
    @ApiModelProperty(name = "genesis_sell_num",value = "创世挂卖次数")
    private Integer genesisSellNum;

    /**
     *高合cotaId
     */
    @ApiModelProperty(name = "comp_cota_id",value = "高合cotaId")
    private String compCotaId;

    /**
     *高合定义次数
     */
    @ApiModelProperty(name = "comp_define_num",value = "高合定义次数")
    private Integer compDefineNum;
    /**
     *高合挂卖次数
     */
    @ApiModelProperty(name = "comp_sell_num",value = "高合挂卖次数")
    private Integer compSellNum;
    /**
     *定义价格
     */
    @ApiModelProperty(name = "define_price",value = "定义价格")
    private Integer definePrice;
    /**
     *挂卖价格
     */
    @ApiModelProperty(name = "sell_price",value = "挂卖价格")
    private Integer sellPrice;

    /**
     *创世定义价格
     */
    @ApiModelProperty(name = "genesis_define_price",value = "创世定义价格")
    private Integer genesisDefinePrice;
    /**
     *创世挂卖价格
     */
    @ApiModelProperty(name = "genesis_sell_price",value = "创世挂卖价格")
    private Integer genesisSellPrice;

    /**
     *init ckb amt
     */
    @ApiModelProperty(name = "init_ckb_amt",value = "初始化账户金额")
    private Integer initCkbAmt;

    /**
     *init deposite
     */
    @ApiModelProperty(name = "init_deposite",value = "初始化充值金额")
    private Integer initDeposite;

    /**
     *只购一次专辑Id
     */
    @ApiModelProperty(name = "only_once_album_id",value = "只购一次专辑Id")
    private Long onlyOnceAlbumId;



    public NftConfig() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWapVersion() {
        return wapVersion;
    }

    public void setWapVersion(String wapVersion) {
        this.wapVersion = wapVersion;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getChangableManagerId() {
        return changableManagerId;
    }

    public void setChangableManagerId(Long changableManagerId) {
        this.changableManagerId = changableManagerId;
    }

    public Long getMergeGiftCollectionId() {
        return mergeGiftCollectionId;
    }

    public void setMergeGiftCollectionId(Long mergeGiftCollectionId) {
        this.mergeGiftCollectionId = mergeGiftCollectionId;
    }

    public Long getCustomAlbumId() {
        return customAlbumId;
    }

    public void setCustomAlbumId(Long customAlbumId) {
        this.customAlbumId = customAlbumId;
    }



    public Integer getGenesisDefineNum() {
        return genesisDefineNum;
    }

    public void setGenesisDefineNum(Integer genesisDefineNum) {
        this.genesisDefineNum = genesisDefineNum;
    }

    public Integer getGenesisSellNum() {
        return genesisSellNum;
    }

    public void setGenesisSellNum(Integer genesisSellNum) {
        this.genesisSellNum = genesisSellNum;
    }

    public String getGenesisCotaId() {
        return genesisCotaId;
    }

    public void setGenesisCotaId(String genesisCotaId) {
        this.genesisCotaId = genesisCotaId;
    }

    public String getCompCotaId() {
        return compCotaId;
    }

    public void setCompCotaId(String compCotaId) {
        this.compCotaId = compCotaId;
    }

    public Integer getCompDefineNum() {
        return compDefineNum;
    }

    public void setCompDefineNum(Integer compDefineNum) {
        this.compDefineNum = compDefineNum;
    }

    public Integer getCompSellNum() {
        return compSellNum;
    }

    public void setCompSellNum(Integer compSellNum) {
        this.compSellNum = compSellNum;
    }

    public Integer getDefinePrice() {
        return definePrice;
    }

    public void setDefinePrice(Integer definePrice) {
        this.definePrice = definePrice;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getGenesisDefinePrice() {
        return genesisDefinePrice;
    }

    public void setGenesisDefinePrice(Integer genesisDefinePrice) {
        this.genesisDefinePrice = genesisDefinePrice;
    }

    public Integer getGenesisSellPrice() {
        return genesisSellPrice;
    }

    public void setGenesisSellPrice(Integer genesisSellPrice) {
        this.genesisSellPrice = genesisSellPrice;
    }

    public Integer getInitCkbAmt() {
        return initCkbAmt;
    }

    public void setInitCkbAmt(Integer initCkbAmt) {
        this.initCkbAmt = initCkbAmt;
    }

    public Integer getInitDeposite() {
        return initDeposite;
    }

    public void setInitDeposite(Integer initDeposite) {
        this.initDeposite = initDeposite;
    }

    public Long getOnlyOnceAlbumId() {
        return onlyOnceAlbumId;
    }

    public void setOnlyOnceAlbumId(Long onlyOnceAlbumId) {
        this.onlyOnceAlbumId = onlyOnceAlbumId;
    }

    public String[] getGenesisCotaIds() {
        return genesisCotaId.split(",");
    }
}
