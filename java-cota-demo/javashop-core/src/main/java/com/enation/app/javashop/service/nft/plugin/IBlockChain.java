package com.enation.app.javashop.service.nft.plugin;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ygg on 2020/11/4.
 */
public interface IBlockChain {
//    HashMap<String,String> genAddress() throws Exception;
    HashMap<String,String> genAddress(String memo) throws Exception;
    BigDecimal checkAmount(String address) throws Exception;
    String setHotWallet(String privateKey);
    String withdrawal(String address,BigDecimal fee,BigDecimal amount) throws IOException;

    Map<String, BigInteger> estimateInFee(String address, BigDecimal amount) throws IOException;
    Map<String, BigInteger> estimateOutFee(String address, BigDecimal amount) throws IOException;

    String chargeFee(String address, BigInteger amount) throws IOException;

    String gather(String address, BigDecimal amount,String privateKey, Map<String,BigInteger> feeMap) throws IOException;
    boolean isTxFinished(String txid) throws IOException;

//    public static String genBip39(String path) {
//        HashMap map = new HashMap();
//        Bip39Wallet wallet = null;
//        // 创建一个存放keystore的文件夹
//        try {
//            // 创建钱包
//            System.out.println(path);
//            wallet = WalletUtils.generateBip39Wallet("qwe", new File(path));
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("创建钱包失败");
//        }
//        // 获取助记词
//        String mnemonic = wallet.getMnemonic();
//        return mnemonic;
//    }
}
