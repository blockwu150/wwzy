package com.enation.app.javashop.model.base.message;

import java.io.Serializable;

/**
 * 商品变化消息
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月22日 上午10:05:19
 */
public class GoodsChangeMsg implements Serializable{

	private static final long serialVersionUID = 3352769927238407770L;

	/**
	 * 变更资源，商品id集合
	 */
	private Long[] goodsIds;

	/**
	 * 操作类型
	 */
	private Integer operationType;

	/**
	 * 添加
	 */
	public final static int ADD_OPERATION = 1;

	/**
	 * 修改
	 */
	public final static int UPDATE_OPERATION = 2;

	/**
	 * 删除
	 */
	public final static int DEL_OPERATION = 3;

	/**
	 * 下架
	 */
	public final static int UNDER_OPERATION = 4;

	/**
	 * 还原
	 */
	public final static int REVERT_OPERATION = 5;

	/**
	 * 放入回收站
	 */
	public final static int INRECYCLE_OPERATION = 6;

	/**
	 * 商品成功审核
	 */
	public final static int GOODS_VERIFY_SUCCESS = 7;

	/**
	 * 商品失败审核
	 */
	public final static int GOODS_VERIFY_FAIL = 8;

	/**
	 * 商品优先级变更
	 */
	public final static int GOODS_PRIORITY_CHANGE = 9;

	private boolean delPromotion = false;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 商品名称是否改变
	 */
	private boolean nameChange;

	public GoodsChangeMsg(Long[] goodsIds, Integer operationType) {
		super();
		this.goodsIds = goodsIds;
		this.operationType = operationType;
	}

	public GoodsChangeMsg(Long[] goodsIds, Integer operationIype,String message) {
		super();
		this.goodsIds = goodsIds;
		this.operationType = operationIype;
		this.message = message;
	}

	public Long[] getGoodsIds() {
		return goodsIds;
	}

	public void setGoodsIds(Long[] goodsIds) {
		this.goodsIds = goodsIds;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public boolean getDelPromotion() {
		return delPromotion;
	}

	public void setDelPromotion(boolean delPromotion) {
		this.delPromotion = delPromotion;
	}

	public boolean isNameChange() {
		return nameChange;
	}

	public void setNameChange(boolean nameChange) {
		this.nameChange = nameChange;
	}
}
