package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.nervos.ckb.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
//import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressTools;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author ygg
 * @version v7.2.2
 * @Description ckb lock hash
 * @ClassName LockHash
 * @since v7.2.2 下午2:43 2022/4/21
 */
@TableName(value = "es_nft_lock_hash")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LockHash implements Serializable {

    private static final long serialVersionUID = -3222222681687007416L;
    /**
     * 主键ID
     */
    @TableId(type= IdType.INPUT)
    @ApiModelProperty(name = "lock_hash",value = "锁定HASH 0x6016f5a6327c0acba1c09629b24b748bd30c29a3fa868ac6570a780e7bb72a46")
    private String lockHash;
    /**
     * 地址
     */
    @ApiModelProperty(name = "address",value = "地址")
    private String address;

    public LockHash() {
    }
    public LockHash(String address) {
        setAddress(address);
        Address sender = Address.decode(address);
        Script lock = sender.getScript();
        setLockHash(Numeric.toHexString(lock.computeHash()));

//        setLockHash(AddressTools.parse(address).script.computeHash());
//        try {
//            AddressTools.AddressGenerateResult a = AddressTools.generateAddress(Network.MAINNET);
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
    }

    public String getLockHash() {
        return lockHash;
    }

    public void setLockHash(String lockHash) {
        this.lockHash = lockHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
