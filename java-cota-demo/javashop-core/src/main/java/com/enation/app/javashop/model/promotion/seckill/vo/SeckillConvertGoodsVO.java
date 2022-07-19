package com.enation.app.javashop.model.promotion.seckill.vo;

/**
 * 注释
 *
 * @author Snow create in 2018/3/20
 * @version v2.0
 * @since v7.0.0
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class SeckillConvertGoodsVO {

    private String goodsName;
    private Double price;
    private String thumbnail;
    private Long goodsId;
    private Integer soldNum;

    @Override
    public String toString() {
        return "SeckillConvertGoodsVO{" +
                "goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", thumbnail='" + thumbnail + '\'' +
                ", goodsId=" + goodsId +
                ", soldNum=" + soldNum +
                '}';
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(Integer soldNum) {
        this.soldNum = soldNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
