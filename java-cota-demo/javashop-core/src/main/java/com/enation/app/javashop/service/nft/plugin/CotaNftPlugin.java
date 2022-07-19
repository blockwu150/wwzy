package com.enation.app.javashop.service.nft.plugin;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import org.nervos.ckb.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Keys;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.ckb.utils.address.AddressTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class CotaNftPlugin implements IBlockChain {
    public static final String NODE_URL = "http://101.32.12.139:3030";
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JavashopConfig cfg;
    public  HashMap<String, String> newAccount() throws Exception {
        HashMap map = new HashMap();
        ECKeyPair keyPair = Keys.createEcKeyPair();
        Script script = Script.generateSecp256K1Blake160SignhashAllScript(keyPair);
        Address address = new Address(script, Network.MAINNET);
        map.put("privateKey", Numeric.toHexString(keyPair.getEncodedPrivateKey()));
        map.put("address",address.encode());
        return map;
    }

    public String toAddress(String privateKey) {
        return exec(getAddress(privateKey));
    }

    public String getCotaNfts(Integer pageNo, Integer pageSize, String cotaId, String cotaAddress) {
        String ret = exec(getCotaNftCmd(cotaAddress,pageNo.toString(),pageSize.toString()));
        logger.debug(ret);
        if(ret.indexOf("blockNumber") >0 )
            ret = ret.substring(ret.indexOf("blockNumber")-2,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return ret;
    }
    public String getWithdrawCotaNfts(Integer pageNo, Integer pageSize, String cotaId, String cotaAddress) {
        if(cotaId == null)cotaId="null";
        String ret = exec(getWithdrawCotaNftCmd(cotaAddress,cotaId, pageNo.toString(),pageSize.toString()));
        logger.debug(ret);
        if(ret.indexOf("blockNumber") >0 )
            ret = ret.substring(ret.indexOf("blockNumber")-2,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return ret;
    }

    public String getMintCotaNfts(Integer pageNo, Integer pageSize, String ckbAddress) {
        String ret = exec(getMintCotaNftCmd(ckbAddress,pageNo.toString(),pageSize.toString()));
        logger.debug(ret);
        if(ret.indexOf("blockNumber") >0 )
            ret = ret.substring(ret.indexOf("blockNumber")-2,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return ret;
    }



    public JSONObject define(String address, String privateKey, String name, String description, String image, Integer num) {
        String ret = exec(getDefineCmd(address,privateKey,name,description,image,num));
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }
    public JSONObject defineInfo(String cotaId) {
        String ret = exec(getDefineInfoCmd(cotaId));
        logger.debug(ret);
        if(ret.indexOf("audio") >0 )
            ret = ret.substring(ret.indexOf("audio")-2,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }



    public JSONObject mint(String address, String privateKey, JSONObject mintJson) {
        String ret = exec(getMintCmd(mintJson.toString(),address,privateKey));
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }
    public JSONObject withdrawal(String toAddress, String fromAddress, String fromPrivateKey, String cotaId,String tokenIndex ) {
        String ret = exec(getWithdrawCmd(toAddress,fromAddress,fromPrivateKey,cotaId,tokenIndex));
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }
    public JSONObject registry(String address, String privateKey,String unregistries) {
        String ret = exec(getRegistryCmd(address,privateKey,unregistries));
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "合约问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }

    public JSONObject claim(String toAddress, String toPrivateKey, String fromAddress, JSONArray claims) {
        String ret = exec(getClaimCmd(toAddress,toPrivateKey,fromAddress,claims));
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }

    @Override
    public HashMap<String, String> genAddress(String memo) throws Exception {
        HashMap map = new HashMap();
//        AddressTools.AddressGenerateResult result = AddressTools.generateAddress(Network.MAINNET);
//                map.put("privateKey", "0x"+result.privateKey);
//        map.put("address",result.address);
//        map.put("memo",memo);
        return map;
    }

    @Override
    public BigDecimal checkAmount(String address) throws Exception {
        return null;
    }

    @Override
    public String setHotWallet(String privateKey) {
        return null;
    }

    @Override
    public String withdrawal(String address, BigDecimal fee, BigDecimal amount) throws IOException {
        return null;
    }

    @Override
    public Map<String, BigInteger> estimateInFee(String address, BigDecimal amount) throws IOException {
        return null;
    }

    @Override
    public Map<String, BigInteger> estimateOutFee(String address, BigDecimal amount) throws IOException {
        return null;
    }

    @Override
    public String chargeFee(String address, BigInteger amount) throws IOException {
        return null;
    }

    @Override
    public String gather(String address, BigDecimal amount, String privateKey, Map<String, BigInteger> feeMap) throws IOException {
        return null;
    }

    @Override
    public boolean isTxFinished(String txid) throws IOException {
        return false;
    }
    final static String DISTINCT="/Users/ygg/vs/ts3/dist/testnet/";
    public static String exec(String sdkcmd) {
        String ret ="";
        try{
            Process p=null;
            String line = null;
            BufferedReader stdout = null;
            BufferedReader stderr = null;
            p = Runtime.getRuntime().exec(sdkcmd);
            stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = stdout.readLine()) != null) {
                ret = ret + line;
            }
            stdout.close();
            if(ret.length() > 0) return ret;

            stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            ret =ret + "err:/n";
            while ((line = stderr.readLine()) != null) {
                ret = ret + line;
            }
            stderr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ret.startsWith("err:")) {
            System.err.println(ret);
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        }
        return ret;
    }
    public String getAddress(String privateKey) {
        return "node "+ cfg.getCotasdk()
                + File.separator + "getAddress "+privateKey;
    }
    String getCotaNftCmd(String address,String page,String pageSize) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"getCotaNft "+address +" " + page +" "+pageSize;
    }
    private String getWithdrawCotaNftCmd(String address,String cotaId, String page, String pageSize) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"getWithdrawCotaNft "+address+" " + cotaId +" " + page +" "+pageSize;
    }
    private String getMintCotaNftCmd(String ckbAddress, String page, String pageSize) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"getMintCotaNft "+ckbAddress +" " + page +" "+pageSize;
    }
    String getRegistryCmd(String address,String privateKey,String unregistries) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"registry "+address+" "+privateKey+" "+unregistries;
     }
    String getDefineCmd(String address,String privateKey,String name,String description, String image,Integer num) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"define "+address+" "+privateKey+" "+name+" "+description+" "+image + " " + num;
    }
    /**
     *   const claims: Claim[] = [
     *     {
     *       cotaId: '0xb066e0f068aa8be6548063a18d811c489a9e2141',
     *       tokenIndex: '0x00000002',
     *     },
     *   ]
     * @param toAddress
     * @param toPrivateKey
     * @param fromAddress
     * @param claims
     * @return
     */
    String getClaimCmd(String toAddress, String toPrivateKey, String fromAddress, JSONArray claims ) {

        String encoded = encodeURIComponent(claims.toString());
        return "node "+ cfg.getCotasdk()
                + File.separator +"claim "+toAddress +" " +toPrivateKey+" "+fromAddress+" "+encoded;
    }

    private String getDefineInfoCmd(String cotaId) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"getDefineInfo "+cotaId;
    }

    /**
     *
     * @param mintJson
     *                  {
     *       cotaId: '0xb066e0f068aa8be6548063a18d811c489a9e2141',
     *       mints:[
     *       {
     *         tokenIndex: '0x00000000',
     *         state: '0x00',
     *         characteristic: '0x0505050505050505050505050505050505050505',
     *         receiverAddress: 'ckt1qyqdcu8n8h5xlhecrd8ut0cf9wer6qnhfqqsnz3lw9',
     *       }
     *       ,
     *       {
     *         tokenIndex: '0x00000001',
     *         state: '0x00',
     *         characteristic: '0x0505050505050505050505050505050505050505',
     *         receiverAddress: 'ckt1qyqdcu8n8h5xlhecrd8ut0cf9wer6qnhfqqsnz3lw9',
     *       },
     *     ]};
     *
     *     {"mints":[
     *     [
     *
     *      {"tokenIndex":["0x00001388"],
     *          "characteristic":["0x0505050505050505050505050505050505050505"],
     *          "receiverAddress":["ckt1qyqdcu8n8h5xlhecrd8ut0cf9wer6qnhfqqsnz3lw9"],
     *          "state":["0x00"]}]],
     *          "cotaId":["0x1b813d4e175e955632f15b0c658b582ecbe40dc6"]
     *
     *
     *     }
     *
     *
     * @param address
     * @param privateKey
     * @return
     */
    String getMintCmd(String mintJson,String address,String privateKey) {
        mintJson = JSONUtil.parseObj(mintJson).toString();
        String encoded = encodeURIComponent(mintJson);
        return "node "+ cfg.getCotasdk()
                + File.separator +"mint "+encoded +" " +address+" "+privateKey;
    }

    /**
     * @param toAddress
     * @param toPrivateKey
     * @param fromAddress
     * @param
     * @return
     */
    String getWithdrawCmd(String fromAddress, String toAddress, String toPrivateKey, String cotaId, String tokenIndex ) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"withdraw "+fromAddress +" " +toAddress+" "+toPrivateKey+" "+cotaId+" "+tokenIndex;
    }

    public static String encodeURIComponent(String s) {
        String result = null;
        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%22")
                    .replaceAll("\\!", "%21")
                    .replaceAll("\\'", "%27")
                    .replaceAll("\\(", "%28")
                    .replaceAll("\\)", "%29")
                    .replaceAll("\\~", "%7E");
        }
        // This exception should never occur.
        catch ( UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

    /**
     * 产生numSize位16进制随机数
     *
     * @param numSize
     * @return java.lang.String
     * @author zxzhang
     * @date 2019/11/9
     */
    public static String getRandomValue(int numSize) {
        String str = "";
        for (int i = 0; i < numSize; i++) {
            char temp = 0;
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    temp = (char) (Math.random() * 10 + 48);//产生随机数字
                    break;
                case 1:
                    temp = (char) (Math.random() * 6 + 'a');//产生a-f
                    break;
                default:
                    break;
            }
            str = str + temp;
        }
        return str;
    }

//    public static String getAddressByLockHash() {
//        String lockHash = "0x6016f5a6327c0acba1c09629b24b748bd30c29a3fa868ac6570a780e7bb72a46";
//        Map params = new HashMap();
//        params.put("q",lockHash);
//        String urls = "https://mainnet-api.explorer.nervos.org/api/v1/suggest_queries";
//
//
////        debugger.log("向mainnet-api.explorer.nervos.org发出请求，请求url为：", urls);
//        // 返回发送结果
//        String result = HttpUtil.get(urls,params);
////        debugger.log("收到返回结果：", result);
//        JSONObject jsonObject = JSONUtil.parseObj(result);
//        Object o = jsonObject.getObj("data.attributes.address_hash");
//        return null;
//    }

    public JSONObject getNftSenderInfo(String address,String cotaId, String tokenIndex) {
        String ret = exec(getNftSenderCmd(address,cotaId,tokenIndex));
        ret = ret.substring(ret.indexOf("blockNumber")-3,ret.length());
        logger.debug(ret);
        return JSONUtil.parseObj(ret);
    }

    public String getNftSenderCmd(String address,String cotaId, String tokenIndex) {
        return "node "+ cfg.getCotasdk()
                + File.separator +"getNftSender "+address +" " +cotaId+" "+tokenIndex;
    }

    public JSONObject transfer(String withdrawAddress, String toAddress, String fromAddress, String fromPrivateKey, String cotaId, String tokenIndex) {
        String ret = exec(getTransferCmd(withdrawAddress,toAddress,fromAddress,fromPrivateKey,cotaId,tokenIndex));
//        ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        logger.debug(ret);
        if(ret.indexOf("txHash") >0 )
            ret = ret.substring(ret.indexOf("txHash")-3,ret.length());
        else
            throw new ServiceException(MemberErrorCode.E110.code(), "网络问题,请稍后再试");
        return JSONUtil.parseObj(ret);
    }
    public String getTransferCmd(String withdrawAddress , String toAddress, String fromAddress, String fromPrivateKey,String cotaId, String tokenIndex)
    {
        return "node "+ cfg.getCotasdk()
                + File.separator +"transfer "+withdrawAddress +" " +toAddress+" "+fromAddress+" "+fromPrivateKey+" "+cotaId+" "+tokenIndex;
    }


}
