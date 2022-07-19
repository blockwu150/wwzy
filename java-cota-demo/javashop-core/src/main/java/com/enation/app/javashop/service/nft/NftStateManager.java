package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NftStateManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NftStateMapper nftStateMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;

    public Map getDefinePayload(Long memberId) {
        Map ret = new HashMap();
        NftConfig cfg = nftConfigMapper.selectById(1l);
        List<NftState> genesis = getGenesisNft(memberId);
        int genfreenum = genesis.size() * cfg.getGenesisDefineNum();
        int genused = 0;
        for (NftState genstate : genesis) {
            genused += genstate.getDefineNum();
            if (ret.get("state") == null && genstate.getDefineNum() < cfg.getGenesisDefineNum()) {
                ret.put("state", genstate);
            }
        }
        List<NftState> comps = getCompNft(memberId);
        int compfreenum = comps.size() * cfg.getCompDefineNum();
        int compused = 0;
        for (NftState compstate : comps) {
            compused += compstate.getDefineNum();
            if (ret.get("state") == null && compstate.getDefineNum() < cfg.getCompDefineNum()) {
                ret.put("state", compstate);
            }
        }
        if (genfreenum - genused <= 0 && compfreenum - compused <= 0) {
            if (genesis.size() > 0) {
                ret.put("price", cfg.getGenesisDefinePrice());
            } else {
                ret.put("price", cfg.getDefinePrice());
            }
        } else {
            ret.put("price", 0);
        }
        return ret;
    }

    public Map getSellPayload(Long memberId) {
        Map ret = new HashMap();
        NftConfig cfg = nftConfigMapper.selectById(1l);
        List<NftState> genesis = getGenesisNft(memberId);
        int genfreenum = genesis.size() * cfg.getGenesisSellNum();
        int genused = 0;
        for (NftState genstate : genesis) {
            genused += genstate.getSellNum();
            if (ret.get("state") == null && genstate.getSellNum() < cfg.getGenesisSellNum()) {
                ret.put("state", genstate);
            }
        }
        List<NftState> comps = getCompNft(memberId);
        int compfreenum = comps.size() * cfg.getCompSellNum();
        int compused = 0;
        for (NftState compstate : comps) {
            compused += compstate.getSellNum();
            if (ret.get("state") == null && compstate.getSellNum() < cfg.getCompSellNum()) {
                ret.put("state", compstate);
            }
        }
        if (genfreenum - genused <= 0 && compfreenum - compused <= 0) {
            if (genesis.size() > 0) {
                ret.put("price", cfg.getGenesisSellPrice());
            } else {
                ret.put("price", cfg.getSellPrice());
            }
        } else {
            ret.put("price", 0);
        }
        return ret;
    }

    public List<NftState> getGenesisNft(Long memberId) {
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftMemberDO m = nftMemberMapper.selectById(memberId);
        List<NftState> ret = new ArrayList();
        for (String cotaId : cfg.getGenesisCotaIds()) {
            JSONObject genesisObj = JSONUtil.parseObj(cotaNftPlugin.getWithdrawCotaNfts(0, 1000, cotaId, m.getCotaAddress()));
            int genTotal = (int) genesisObj.get("total");
            if (genTotal > 0) {
                JSONArray nfts = genesisObj.getJSONArray("nfts");

                for (int i = 0; i < nfts.size(); i++) {
                    JSONObject nft = nfts.getJSONObject(i);
                    String key = nft.get("cotaId").toString() + nft.get("tokenIndex");
                    NftState state = nftStateMapper.selectById(key);
                    if (state == null) {
                        state = new NftState();
                        state.setNftKey(key);
                        state.setType(NftState.TYPE_GENESIS);
                        nftStateMapper.insert(state);
                    }
                    ret.add(state);
                }
            }
//            while (nfts.jsonIter().iterator().hasNext()) {
//                JSONObject nft = nfts.jsonIter().iterator().next();
//                String key = nft.get("cotaId").toString()+nft.get("tokenIndex");
//                NftState state = nftStateMapper.selectById(key);
//                if(state==null) {
//                    state = new NftState();
//                    state.setNftKey(key);
//                    state.setType(NftState.TYPE_GENESIS);
//                    nftStateMapper.insert(state);
//                }
//                ret.add(state);
//            }
        }
        return ret;
    }

    public List<NftState> getCompNft(Long memberId) {
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftMemberDO m = nftMemberMapper.selectById(memberId);
        List<NftState> ret = new ArrayList();

        JSONObject compObj = JSONUtil.parseObj(cotaNftPlugin.getWithdrawCotaNfts(0, 1000, cfg.getCompCotaId(), m.getCotaAddress()));
        int compTotal = (int) compObj.get("total");
        if (compTotal > 0) {
            JSONArray nfts = compObj.getJSONArray("nfts");
            for (int i = 0; i < nfts.size(); i++) {
                JSONObject nft = nfts.getJSONObject(i);
                String key = nft.get("cotaId").toString() + nft.get("tokenIndex");
                NftState state = nftStateMapper.selectById(key);
                if (state == null) {
                    state = new NftState();
                    state.setNftKey(key);
                    state.setType(NftState.TYPE_COMPOND);
                    nftStateMapper.insert(state);
                }
                ret.add(state);
            }
        }

        return ret;
    }


    public void increaseDefine(NftState state) {
        state.setDefineNum(state.getDefineNum() + 1);
        nftStateMapper.updateById(state);
    }

    public void increaseSell(NftState state) {
        state.setSellNum(state.getSellNum() + 1);
        nftStateMapper.updateById(state);
    }

    public void reduceSell(String nftKey) {
        NftState state = nftStateMapper.selectById(nftKey);
        if (state.getSellNum() > 1) {
            state.setSellNum(state.getSellNum() - 1);
            nftStateMapper.updateById(state);
        }
    }


    public JSONObject getNfts(String address,String cotaId) {
        List<NftState> ret = new ArrayList();
        JSONObject compObj = JSONUtil.parseObj(cotaNftPlugin.getWithdrawCotaNfts(0, 1000, cotaId, address));
        return compObj;
    }

}
