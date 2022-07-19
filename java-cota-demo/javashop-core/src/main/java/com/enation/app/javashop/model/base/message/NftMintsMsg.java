package com.enation.app.javashop.model.base.message;

import com.enation.app.javashop.model.nft.dos.NftMint;

import java.io.Serializable;
import java.util.Set;

/**
 * 邮件内容
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:37:22
 */
public class NftMintsMsg implements Serializable{


	private static final long serialVersionUID = -2914987498245962253L;

	private Long collectionId;

	private Set<NftMint> mints;

	public NftMintsMsg() {
	}

	public NftMintsMsg(Long collectionId, Set<NftMint> mints) {
		this.collectionId = collectionId;
		this.mints = mints;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public Set<NftMint> getMints() {
		return mints;
	}

	public void setMints(Set<NftMint> mints) {
		this.mints = mints;
	}
}
