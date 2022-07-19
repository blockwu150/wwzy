package com.enation.app.javashop.model.nft.dos;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.mapper.nft.NftAlbumMapper;
import com.enation.app.javashop.mapper.nft.NftCollectionMapper;
import com.enation.app.javashop.mapper.nft.NftMemberMapper;
import com.enation.app.javashop.service.nft.NftMemberManager;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Map;

/**
 * @author ygg
 * @version v7.2.2
 * @Description NFT动作
 * @ClassName NftJobAction
 * @since v7.2.2 下午2:43 2022/4/21
 */
@TableName(value = "es_nft_action")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftAction implements Serializable {
    private static final long serialVersionUID = 3843085335219735353L;
    /**
     * 区块链行为
     */
    public final static Integer ACTION_REGISTRY = 1;//注册
    public final static Integer ACTION_DEFINE = 2;//定义collection
    public final static Integer ACTION_MINT = 3;//定义Mint给别人
    public final static Integer ACTION_WITHDRAWAL = 4;//转给别人
    public final static Integer ACTION_CLAIM = 5;//声明持有
    public final static Integer ACTION_CHARGE = 6;//充值
    public final static Integer ACTION_CHECK_TX = 7;//检查交易状态

    /**
     * 后台行为
     */
    public final static Integer ACTION_BE_COLLECTION = 10;//定义完成修改收藏品状态
    public final static Integer ACTION_BE_REGISTRY = 11;//定义完成修改收藏品状态


    public final static Integer STATUS_BEGIN = 0;//开始
    public final static Integer STATUS_DOING = 1;//进行中
    public final static Integer STATUS_DONE = 2;//完成

    /**
     * 主键ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    /**
     * 动作
     */
    @ApiModelProperty(name = "action",value = "动作")
    private Integer action;

    /**
     * 费用
     */
    @ApiModelProperty(name = "fee",value = "费用")
    private Double fee;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status;

    /**
     * 完成时间
     */
    @ApiModelProperty(name = "complete_time",value = "完成时间")
    private Long completeTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Long createTime;

    /**
     * 来源
     */
    @ApiModelProperty(name = "job_id",value = "来源")
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public NftAction(Long jobId, Integer action) {
        this.jobId = jobId;
        this.action = action;
        this.status=STATUS_BEGIN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if(status.equals(STATUS_DOING)) {
            this.createTime = System.currentTimeMillis()/1000;
        }
        if(status.equals(STATUS_DONE)) {
            this.completeTime = System.currentTimeMillis()/1000;
        }
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean exec(Map context, Map services) {
        if(action==ACTION_REGISTRY)
            return registry(context,services);
        if(action==ACTION_DEFINE)
            return define(context,services);
        if(action==ACTION_CHECK_TX)
            return check(context,services);
        if(action==ACTION_BE_COLLECTION)
            return doCollection(context,services);
        if(action==ACTION_BE_REGISTRY)
            return doRegistry(context,services);
        if(action==ACTION_MINT)
            return mint(context,services);
        if(action==ACTION_WITHDRAWAL)
            return withdrawal(context,services);
        if(action==ACTION_CLAIM)
            return claim(context,services);
        return false;
    }

    private boolean registry(Map context, Map services) {
        //context memberId long 出让人Id
//        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
//        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");
//
//        Map cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("memberId").toString()));
//        String address = cipher.get("cota_address").toString();;
//        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(),cipher.get("private_key").toString());
//        JSONObject ret =cotaNftPlugin.registry(address,privateKey);
//        context.put("txHash",ret.get("txHash").toString());
        return true;
    }

    private boolean withdrawal(Map context, Map services) {
        //context memberId long 出让人Id
        //context receiverId long 受让人Id
        //context cotaId string cotaId
        //context tokenIndex string 16进制 tokenIndex
        //context txHash string
        //context claims JSONArray
        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");

        Map cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("receiverId").toString()));

        String toAddress =  cipher.get("cota_address").toString();
        cipher = nftMemberMapper.getCipher(Long.valueOf( context.get("memberId").toString()));
        String fromAddress =  cipher.get("cota_address").toString();
        String fromPrivateKey = DESUtil.decrypt(cipher.get("uname").toString(),cipher.get("private_key").toString());
        JSONObject ret =cotaNftPlugin.withdrawal(toAddress,fromAddress,fromPrivateKey, context.get("cotaId").toString(),context.get("tokenIndex").toString());
        context.put("txHash",ret.get("txHash").toString());
        JSONArray claimJsons = new JSONArray();
        JSONObject claim = new JSONObject();
        claim.putOnce("tokenIndex",context.get("tokenIndex").toString());
        claim.putOnce("cotaId",context.get("cotaId").toString());
        claimJsons.add(claim);
        context.put("claims",claimJsons);
        return true;
    }

    private boolean doRegistry(Map context, Map services) {
        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");
        NftMemberDO m = nftMemberMapper.selectById(Long.valueOf(context.get("memberId").toString()));
        m.setRegistry(true);
        nftMemberMapper.updateById(m);
        return true;
    }
    private boolean doCollection(Map context, Map services) {
        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
        NftAlbumMapper nftAlbumMapper = (NftAlbumMapper) services.get("nftAlbumMapper");

        JSONObject a = (JSONObject)context.get("collection");
        NftCollection collection = a.toBean(NftCollection.class);
        collection.setStatus(NftCollection.STATUS_SUCCESS);
        nftCollectionMapper.updateById(collection);
        NftAlbum album = nftAlbumMapper.selectById(collection.getAlbumId());
        if(album != null){
            album.setNum(album.getNum() + 1);
            nftAlbumMapper.updateById(album);
        }
        return true;
    }

    private boolean check(Map context,Map services) {
        if(System.currentTimeMillis()/1000 - this.createTime < 30)
            return false;
        return true;
    }


    //context memberId 会员Id
    //context collection NftCollection  收藏品对象
    //context -- cotaNftPlugin nft插件
    //context -- nftCollectionMapper 收藏品mapper
    public boolean define(Map context,Map services) {
        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");
        Map cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("memberId").toString()));
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(),cipher.get("private_key").toString());
        String address =  cipher.get("cota_address").toString();

//        String address = (String) context.get("address");
//        String privateKey = (String) context.get("privateKey");
//        String pwd = (String) context.get("pwd");
//        privateKey= DESUtil.decrypt(pwd,privateKey);
        JSONObject a = (JSONObject)context.get("collection");
        NftCollection collection = a.toBean(NftCollection.class);
        JSONObject ret = cotaNftPlugin.define(address, privateKey, collection.getName(), collection.getDescription(), collection.getImage(), collection.getNum());
        collection.setCotaId(ret.get("cotaId").toString());
        nftCollectionMapper.updateById(collection);
        context.put("txHash",ret.get("txHash").toString());
        return true;
    }


    //context isRegistry boolean 是否注册
    //context chargeKey 充值键
    //context chargPwd 充值密码
    //context chargAddress 充值地址
    //context privateKey String  转出私钥
    //context pwd String 密码
    //context address String  地址
    //context toAddress String  提取地址
    //context toPrivateKey String  提取私钥
    //context collection NftCollection  收藏品对象
    //context num Integer 数量
    //context -- cotaNftPlugin nft插件
    //context -- nftCollectionMapper 收藏品mapper
//    public boolean mint(Map context,Map services) {
//        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
//        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
//
//        String address = (String) context.get("address");
//        String privateKey = (String) context.get("privateKey");
//        String pwd = (String) context.get("pwd");
//        privateKey= DESUtil.decrypt(pwd,privateKey);
//
//        JSONObject a = (JSONObject)context.get("collection");
//        NftCollection collection = a.toBean(NftCollection.class);
//        String toAddress = (String) context.get("toAddress");
//        Integer num=(Integer)context.get("num");
//        JSONObject mintJson = new JSONObject();
//        JSONArray claimJsons = new JSONArray();
//        mintJson.append("cotaId",collection.getCotaId());
//        JSONArray mints = new JSONArray();
//
//        for(int i = collection.getNum();i<collection.getNum() + num;i++) {
//            JSONObject mintObj = new JSONObject();
//
//            JSONObject claim = new JSONObject();
//            claim.append("tokenIndex",String.format("0x%08x", i));
//            claim.append("cotaId",collection.getCotaId());
//            claimJsons.add(claim);
//
//            mints.add(mintObj);
//            mintObj.append("tokenIndex",String.format("0x%08x", i));
//            mintObj.append("state","0x00");
//            mintObj.append("characteristic","0x0505050505050505050505050505050505050505");
//            mintObj.append("receiverAddress",toAddress);
//        }
//        collection.setNum(collection.getNum() + num);
//        nftCollectionMapper.updateById(collection);
//        mintJson.append("mints",mints);
//        JSONObject ret = cotaNftPlugin.mint(address, privateKey, mintJson);
//        context.put("txHash",ret.get("txHash").toString());
//        //ready for claim
//        context.put("claims",claimJsons);
//        return true;
//    }

    //context memberId long 出让人Id
    //context receiverId long 受让人Id
    //context collection NftCollection  收藏品对象
    //context num Integer 数量
    //context -- cotaNftPlugin nft插件
    //context -- nftCollectionMapper 收藏品mapper
    //context txHash string
    //context claims JSONArray
    public boolean mint(Map context,Map services) {
        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");
        Map cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("memberId").toString()));
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(),cipher.get("private_key").toString());
        String address =  cipher.get("cota_address").toString();
        cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("receiverId").toString()));
        String toAddress =  cipher.get("cota_address").toString();

        JSONObject a = (JSONObject)context.get("collection");
        NftCollection collection = a.toBean(NftCollection.class);
        Integer num=(Integer)context.get("num");
        JSONObject mintJson = new JSONObject();
        JSONArray claimJsons = new JSONArray();
        mintJson.putOnce("cotaId",collection.getCotaId());
        JSONArray mints = new JSONArray();

        for(int i = collection.getTokenIndex() ;i<collection.getTokenIndex() + num;i++) {

            JSONObject claim = new JSONObject();
            claim.putOnce("tokenIndex",String.format("0x%08x", i));
            claim.putOnce("cotaId",collection.getCotaId());
            claimJsons.add(claim);

            JSONObject mintObj = new JSONObject();
            mintObj.putOnce("tokenIndex",String.format("0x%08x", i));
            mintObj.putOnce("state","0x00");
            mintObj.putOnce("characteristic","0x0505050505050505050505050505050505050505");
            mintObj.putOnce("receiverAddress",toAddress);
            mints.add(mintObj);
        }
        collection.setTokenIndex(collection.getTokenIndex() + num);
        nftCollectionMapper.updateById(collection);
        mintJson.putOnce("mints",mints);
        JSONObject ret = cotaNftPlugin.mint(address, privateKey, mintJson);
        context.put("txHash",ret.get("txHash").toString());
        //ready for claim
        context.put("claims",claimJsons);
        return true;
    }

    //context isRegistry boolean 是否注册
    //context chargeKey 充值键
    //context chargPwd 充值密码
    //context chargAddress 充值地址
    //context privateKey String  转出私钥
    //context pwd String 密码
    //context address String  地址
    //context toAddress String  提取地址
    //context toPrivateKey String  提取私钥
    //context toPwd String  提取私钥
    //context collection NftCollection  收藏品对象
    //context num Integer 数量
    //context claims json 生成的claim
    //context -- cotaNftPlugin nft插件
    //context -- nftCollectionMapper 收藏品mapper
//    public boolean claim(Map context,Map services) {
//        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
//
//        String toAddress = (String) context.get("toAddress");
//        String toPrivateKey = (String) context.get("toPrivateKey");
//        String pwd = (String) context.get("toPwd");
//        String privateKey= DESUtil.decrypt(pwd,toPrivateKey);
//        String fromAddress = (String) context.get("address");
//        JSONArray claims = (JSONArray) context.get("claims");
//
//        JSONObject ret = cotaNftPlugin.claim(toAddress, privateKey,fromAddress,claims);
//        context.put("txHash",ret.get("txHash").toString());
//
//
//        return true;
//    }

    //context memberId long 出让人Id
    //context receiverId long 受让人Id
    //context collection NftCollection  收藏品对象
    //context num Integer 数量
    //context -- cotaNftPlugin nft插件
    //context -- nftCollectionMapper 收藏品mapper
    //context txHash string
    //context claims JSONArray
    public boolean claim(Map context,Map services) {
        CotaNftPlugin cotaNftPlugin = (CotaNftPlugin) services.get("cotaNftPlugin");
        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
        NftMemberMapper nftMemberMapper = (NftMemberMapper) services.get("nftMemberMapper");

        Map cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("memberId").toString()));
        String fromAddress =  cipher.get("cota_address").toString();
        cipher = nftMemberMapper.getCipher(Long.valueOf(context.get("receiverId").toString()));
        String toAddress =  cipher.get("cota_address").toString();
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(),cipher.get("private_key").toString());
        JSONArray claims = (JSONArray) context.get("claims");
        JSONObject ret = cotaNftPlugin.claim(toAddress, privateKey,fromAddress,claims);
        context.put("txHash",ret.get("txHash").toString());
        return true;
    }
}
