package com.enation.app.javashop.model.nft.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * nft浏览器搜索参数实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-12-06
 */
@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchParam {

    public static final int SEARCH_NFTS = 0;
    public static final int SEARCH_MINTS = 1;
    public static final int SEARCH_TX = 2;

    @ApiModelProperty(value = "页码", name = "page_no", required = true)
    private Long pageNo;

    @ApiModelProperty(value = "分页数", name = "page_size", required = true)
    private Long pageSize;

    @ApiModelProperty(value = "地址", name = "address")
    private String address;

    @ApiModelProperty(value = "cotaId", name = "cota_id")
    private String cotaId;

    @ApiModelProperty(value = "txHash", name = "tx_hash")
    private String txHash;

    @ApiModelProperty(value = "searchType", name = "search_type")
    private int searchType;

    public SearchParam() {
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCotaId() {
        return cotaId;
    }

    public void setCotaId(String cotaId) {
        this.cotaId = cotaId;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", address='" + address + '\'' +
                ", cotaId='" + cotaId + '\'' +
                ", txHash='" + txHash + '\'' +
                ", searchType='" + searchType + '\'' +
                '}';
    }
}
