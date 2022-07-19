package com.enation.app.javashop.model.nft.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * nft批量分发参数实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-12-06
 */
@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BatchMintParam {

    @ApiModelProperty(value = "地址列表", name = "addresses", required = true)
    private String[] addresses;


    @ApiModelProperty(value = "collectionId", name = "collection_id")
    private Long collectionId;

    @ApiModelProperty(value = "pwd", name = "pwd")
    private String pwd;


    public BatchMintParam() {
    }

    public String[] getAddresses() {
        return addresses;
    }

    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "BatchMintParam{" +
                "addresses='" + addresses + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
