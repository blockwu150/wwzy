package com.enation.app.javashop.framework.security.model;

/**
 *
 * 卖家
 * Created by kingapex on 2018/3/11.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/11
 */
public class Seller extends  Buyer {

    /**
     * 卖家id
     */
    private  Long sellerId;

    /**
     * 卖家店铺名称
     */
    private String sellerName;

    /**
     * 是否是自营  0 不是  1是
     */
    private Integer selfOperated;


    public Seller() {
         //seller有 买家的角色和卖宾角色
         add( Role.SELLER.name());
    }


    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }


	public Integer getSelfOperated() {
		return selfOperated;
	}


	public void setSelfOperated(Integer selfOperated) {
		this.selfOperated = selfOperated;
	}


}
