package com.enation.app.javashop.service.nft.plugin;
//import static org.nervos.ckb.utils.Const.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.type.TransactionWithStatus;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.InputIterator;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;

/**
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class CKBAccountUtil {
    public static final String NODE_URL = "https://mainnet.ckbapp.dev/rpc";
    public static final String CKB_INDEXER_URL = "https://mainnet.ckbapp.dev/indexer";

    public static final BigInteger UnitCKB = new BigInteger("100000000");
    public static final BigInteger MIN_CKB = new BigInteger("6100000000");
    private static Api api;
    private static CkbIndexerApi ckbIndexerApi;
    private static List<String> SendPrivateKeys;
    private static List<String> SendAddresses;
    private static List<String> ReceiveAddresses;

    static {
        api = new Api(NODE_URL, false);
        ckbIndexerApi = new DefaultIndexerApi(CKB_INDEXER_URL, false);
        SendPrivateKeys =
                Arrays.asList("0xe32ea24ac31a6bb2289df371dcce3b7762d0e3806ee94150430b81531d247a42");
        SendAddresses = Arrays.asList("ckb1qyq2094ndyknsdp93tqud2h4nq7llaqj55ssrqkgy6");
        ReceiveAddresses =
                Arrays.asList(
                        "ckb1qyqv8thh86ytmqwfteudhr98wdc6z5gwx89qcggxpr");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getTxStatus("0x800980d2ff509e9b0c11b21522a9ab16a806ea8f29d0b59c6c5e7b75a63b73a0"));
        TransactionWithStatus t = api.getTransaction(Numeric.hexStringToByteArray("0x800980d2ff509e9b0c11b21522a9ab16a806ea8f29d0b59c6c5e7b75a63b73a0"));

        if(t.txStatus.status.equals(TransactionWithStatus.Status.COMMITTED)) {
            System.out.println(true);
            System.out.println(t.txStatus.status.toString());
            System.out.println(t.txStatus.status.name());
        }

        System.out.println(getBalance("ckb1qyq2094ndyknsdp93tqud2h4nq7llaqj55ssrqkgy6"));
    }

    public static TransactionWithStatus.Status getTxStatus(String txHash) throws IOException {
        TransactionWithStatus t = api.getTransaction(Numeric.hexStringToByteArray(txHash));
        return t.txStatus.status;//rejected
    }

    public static List<byte[]> getTxWitnesses(String txHash) throws IOException {
        TransactionWithStatus t = api.getTransaction(Numeric.hexStringToByteArray(txHash));
        return t.transaction.witnesses;
    }

    public static BigInteger getBalance(String address) throws IOException {
        Address addr = Address.decode(address);
        SearchKeyBuilder key = new SearchKeyBuilder();
        key.script(addr.getScript()
        );
        key.scriptType(ScriptType.LOCK);
        CellCapacityResponse capacity = ckbIndexerApi.getCellsCapacity(key.build());
        return BigInteger.valueOf(capacity.capacity).divide(UnitCKB);
    }

    public static String sendCapacity(Map<String, Long> receivers, String changeAddress, String privateKey) throws IOException {
        Iterator<TransactionInput> iterator = new InputIterator(changeAddress);
        CkbTransactionBuilder builder = new CkbTransactionBuilder(iterator, Network.MAINNET);
        for (Map.Entry<String, Long> entry : receivers.entrySet()
        ) {
            builder.addOutput(entry.getKey(), entry.getValue());
        }
        TransactionWithScriptGroups txWithGroups = builder.setFeeRate(1000)
                .setChangeOutput(changeAddress)
                .build();
        TransactionSigner.getInstance(Network.MAINNET)
                .signTransaction(txWithGroups, privateKey);

        Api api = new Api(NODE_URL, false);

        byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
        System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
        return Numeric.toHexString(txHash);

    }

    public static String sendCapacity(Map<String, Long> receivers, String changeAddress, Set<String> privateKeys) throws IOException {
        Network network = Network.MAINNET;
        List<byte[]> keyHashes = new ArrayList<>();
        for (int i = 0; i < privateKeys.size(); i++) {
            keyHashes.add(new BigInteger(160, new Random()).toByteArray());
        }
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
                new Secp256k1Blake160MultisigAllSigner.MultisigScript(0, privateKeys.size(), keyHashes);

        Iterator<TransactionInput> iterator = new InputIterator(changeAddress);

        CkbTransactionBuilder builder = new CkbTransactionBuilder(iterator, network);
        for (Map.Entry<String, Long> entry : receivers.entrySet()
        ) {
            builder.addOutput(entry.getKey(), entry.getValue());
        }
        TransactionWithScriptGroups txWithGroups = builder.setFeeRate(1000)
                .setChangeOutput(changeAddress)
                .build(multisigScript);
        for (String privateKey:privateKeys) {
            TransactionSigner.getInstance(network)
                    .signTransaction(txWithGroups, new HashSet<>(Arrays.asList(new Context(privateKey, multisigScript))));
        }
        Api api = new Api(NODE_URL, false);
        byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
        System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
        return Numeric.toHexString(txHash);
    }


}
