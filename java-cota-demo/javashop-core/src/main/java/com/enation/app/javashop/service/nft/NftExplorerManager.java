package com.enation.app.javashop.service.nft;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.member.vo.MemberDepositeVO;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.member.DepositeManager;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CKBAccountUtil;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nervos.ckb.type.Script;

import java.io.IOException;
import java.util.*;

@Service
public class NftExplorerManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private LockHashMapper lockHashMapper;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private NftSellMapper nftSellMapper;
    @Autowired
    private NftObjectMapper nftObjectMapper;
    @Autowired
    private UploadApplyMapper uploadApplyMapper;
    @Autowired
    private NftPointMapper nftPointMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftAlbumMapper nftAlbumMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private NftFragmentMapper nftFragmentMapper;
    @Autowired
    private NftOrderMapper nftOrderMapper;
    @Autowired
    private NftFragmentManager nftFragmentManager;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private NftJobManager nftJobManager;
    @Autowired
    private NftStateManager nftStateManager;
    @Autowired
    private DepositeManager depositeManager;
    @Autowired
    private DepositeClient depositeClient;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;



    public String listWithdrewCotaNfts(Integer pageNo, Integer pageSize, String ckbAddress,String cotaId) {
        return cotaNftPlugin.getWithdrawCotaNfts(pageNo + 1,pageSize,cotaId,ckbAddress);
    }

    public String listMintCotaNfts(Integer pageNo, Integer pageSize, String ckbAddress) {
        return cotaNftPlugin.getMintCotaNfts(pageNo + 1 ,pageSize,ckbAddress);
    }
    //        echo '{
//    "id":2,
//    "jsonrpc":"2.0",
//    "method":"parse_witness",
//    "params":{
//        "witness":"0x8107000010000000550000008107000041000000f6b9acf8267464b7aaa7729c26fcf4ce1ff16582b4bb40ea122498b8d89de2ef67e8ee44517ce204681f1ed4435d147db2b6ce345731791311ae7246987a67df00280700000227070000200000003a0000004700000054000000bc000000a6010000230700000100000081001e23dc506c1b15f286c9db84a4d12a453266097501000000000000640000006200010000000000006400000064000200000081021e23dc506c1b15f286c9db84a4d12a453266097500000062dc6fb71f2caea8949e8a7a88f53ed031589ed65d0000000081021e23dc506c1b15f286c9db84a4d12a453266097500000063dc6fb71f2caea8949e8a7a88f53ed031589ed65d00000000ea0000000c0000007b0000006f0000000c000000220000000000050505050505050505050505050505050505050549000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000dc70f33de86fdf381b4fc5bf092bb23d027748016f0000000c000000220000000000050505050505050505050505050505050505050549000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000dc70f33de86fdf381b4fc5bf092bb23d02774801790500004c4fa95091a4c55d156aa9299b3094733123a9ccbdf20a4ca4ea5414e0212bf6202c2dfc50edfd0724f0e61e58169515abe0ad9d58622b6c11cb8e6538333190672f6de23c51ab643619549f2d589d230daf6feebcce7385fc4efbee4499523133294ceaa7dbf68100e477dc008e244d26dc87a97f6bda123292ff0a0000000000000000000000504ba8433d54e0b8940b1f58a7ae1f58c0b360eccac36a67f6fdc5738c5ce3e3eb5011238f883e0519d2627e7f8ae405ade0ad769ab84afe70520f9c7d62d26cf842500874023b2277b101808e0faeb2a65864049e781265be50ff1d060c596d00042350fb4076fdd2e1218bddcfa8cd5e932412565814bc3544ca867c5268373ea68f294f185118f7e7780fdee42f2bcff16c1cf50c159440ce34ad34984105b0d0a08a490b28da000000000000000000000000000000000000000000000000000000000000000051c9373c9f4e89ce45af216c2ca83a19be7f9963ee92a60930f793e25f9f24c6aeb28101b066e0f068aa8be6548063a18d811c489a9e2141000000000000000000004f2951f32bb62c965b80ae2351946062f857a05740d1007cfdfcd79571395bc095d1bc8b6fc899e5a5fce9b5a62297bbffa6803530bac3715640a8f2728ff4c171e2060051f486a090928af903f712a376fd1d45c2b0bad9b3f31fcd3137816f08656fdd554a95d27744b861422abdfafeb0e40ba8c793fab8dad90cf7ab5bdc9772194d090050e73032d84ca4d732a8c3d7239c9ffc2413c8abbba6f66e03575d13ffa9bc145251f6fc114bc93ecc14ce0d584ae36703ce6fc71774cdf1755ce6cab6ce67b7801f32ab98df4af1a44b6a88b5849550431abba0ab2cfe4b3cb93bb0230f02108e3d0051f722628b8d83acb29ed79f1929328f5f2d36513215426edc610a613d23f0035139a9180f58b189c5d5cdf1114384cca860bdd49a1c88b67a670800aed1869d100050cf4bcb3dddb4f33d22dd690c5ef46f1e1819ae866b1b38df2de14d8064bc2a88500cc298e2f2db864c99ba8ba85f1cb94e6d0516dc88cfc71969fa4aed1e7e5a21501a5eb3e7d7687ee2450a884147091cda21c59c780b22c22a6426a71ccbf372b550a58d80132aa023b05c079dd276c7335a5f5512bd50cea98fd1c1f6ba788eb13750de3d22b1a7725911d0eb142dae4947b1ebd68c220471fa0c7eab6d70d60674e550e54e5228a6f740816a3538f72281bee3a5ba681f92688b4832c977574c183372501d496836b0d38dc322857e736adf869f5b57b423ec78c7089bd642f95f6998634c4ff7501d47608687cba32d95e1519b28374c681e3fc30ef6ec88174fdade1f4bb268715039e1b23de083aa42de52ad9d891a3a2c141fbe2d821411e00d16488bcbb2cb33501dc1f382523fc46c6f9f6c0e3a77c96fb473d57b4bac4037f38683c363bd8ec750e476d6204d69099795d0e7300074949377000027bb3837423917d3d5b4ea906c50ab9b35f4aad5594a87d7f68193de993fcb62e46ff985464dd251b2936b2264cb4c4ff65068a05a45b4faf89a9402b24c04b3a46fc2e18a93c12da126e11d3c8f036cd11251f712c1e6b1a0d27af3d1a72acbc92105b08626248ef932c94260accb9502afef22442d4e0cf6ab7f36e0a9834a9f338d198065a66d8d2412603188b5278b865b0050e737a5fade47517f44b6a8d5f720609a1053bd9e1428a76d96ec1e4fa3d3cacb50a766c1da702421881cd1085a83e67ca17a3dca02badecb69738f78ced905fd2950f9e79576dc135d92368a94edcd45b4cfb9dc4e6abc20411defa216c332dd486350a8dfe6c905c799e319f0f8fcdbe6f9e4bb7c57bbe85f848c6167b3a2cca345184850cf2365392df4f78464069bbcaddc3fbd5849b7d946a97a82d718c3d2416b43d850bd380a2a20fccede15c4a38e827ecb81c35d8f282ed73f4492c0d79e39c6f6564800000000",
//        "version": "1"
//    }
//}' \
//| tr -d '\n' \
//| curl -H 'content-type: application/json' -d @- \
//http://127.0.0.1:3030
    public static String analysisTx(String txHash) throws IOException {
        List<byte[]> witnesses = CKBAccountUtil.getTxWitnesses(txHash);
        String cotaWitness = Numeric.toHexString(witnesses.get(0));
        String json = "{\n" +
                "    \"id\":2,\n" +
                "    \"jsonrpc\":\"2.0\",\n" +
                "    \"method\":\"parse_witness\",\n" +
                "    \"params\":{\n" +
                "        \"witness\":\"" + cotaWitness + "\",\n" +
                "        \"version\": \"1\"\n" +
                "    }\n" +
                "}";
        String result = HttpUtil.post(CotaNftPlugin.NODE_URL, json);
        return result;
    }

    public static void main(String[] args) {
        try {
//            System.out.println(analysisTx("0xdee2ba046a5e22b0d7278149c51955262efe3ff46bb9966ac61b19553b7ffb69"));
            JSONObject txJson = JSONUtil.parseObj(analysisTx("0x4903252ea1790d700478870993831223b0457173c51121ef787d296824a5b7bb"));
            JSONObject cota = txJson.getJSONObject("result").getJSONObject("cota");
            JSONArray withdralKeys = txJson.getJSONObject("result").getJSONObject("cota").getJSONArray("withdrawal_values");
            JSONArray withdrawValues = cota.getJSONArray("withdrawal_values");
            JSONObject withdrawValue = (JSONObject)withdrawValues.get(0);
            JSONObject toLock = (JSONObject) withdrawValue.get("to_lock");
            System.out.println(txJson);
            System.out.println(toLock);
            System.out.println(toLock.get("code_hash"));
            System.out.println(toLock.get("args"));
        Script script = new Script(Numeric.hexStringToByteArray(toLock.get("code_hash").toString()),Numeric.hexStringToByteArray(toLock.get("args").toString()), Script.HashType.TYPE);
            System.out.println(script.computeHash());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
