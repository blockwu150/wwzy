package com.enation.app.javashop.service.passport.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.app.javashop.framework.auth.Token;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.TokenManager;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.mapper.member.ConnectMapper;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.message.MemberRegisterMsg;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.ConnectDO;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.member.dto.LoginUserDTO;
import com.enation.app.javashop.model.member.dto.WeChatMiniLoginDTO;
import com.enation.app.javashop.model.member.dto.WeChatUserDTO;
import com.enation.app.javashop.model.member.enums.ConnectTypeEnum;
import com.enation.app.javashop.model.member.vo.Auth2Token;
import com.enation.app.javashop.model.member.vo.MemberLoginMsg;
import com.enation.app.javashop.model.member.vo.MemberVO;
import com.enation.app.javashop.model.payment.enums.WechatTypeEnmu;
import com.enation.app.javashop.service.member.ConnectManager;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.passport.LoginManager;
import com.enation.app.javashop.service.passport.LoginWeChatManager;
import com.enation.app.javashop.service.passport.signaturer.WechatSignaturer;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
/**
 * 微信统一登陆服务实现
 * @author cs
 * @since v1.0
 * @version 7.2.2
 * 2020/09/24
 */
@Service
public class LoginWeChatManagerImpl implements LoginWeChatManager {


    @Autowired
    private WechatSignaturer wechatSignaturer;
    @Autowired
    private ConnectMapper connectMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ConnectManager connectManager;

    @Autowired
    private Cache cache;

    @Autowired
    private LoginManager loginManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String WX_MINI_SESSIONKEY="{WX}{MINI}{SESSION_KEY}_";

    private static final Integer WX_TOKEN_VAILD_TIME_APP = 60*60*24*90;

    private static final Integer WX_TOKEN_VAILD_TIME_MINI = 60*60*24*2;


    @Override
    public String getLoginUrl(String redirectUri) {
        Map<String, String> connectConfig = wechatSignaturer.getConnectConfig(WechatTypeEnmu.WAP.name());
        String appId = connectConfig.get("app_id");
        StringBuffer loginBuffer = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?");
        loginBuffer.append("appid=").append(appId);
        loginBuffer.append("&redirect_uri=").append(redirectUri);
        loginBuffer.append("&response_type=code&scope=snsapi_userinfo&state=weixin#wechat_redirect");
        return loginBuffer.toString();
    }

    @Override
    public Map wxWapLogin(String code,String uuid) {
        JSONObject accessTokenJson = this.getAccessToken(code);
        logger.debug("accessTokenJson==="+accessTokenJson.toString());
        String access_token = accessTokenJson.getString("access_token");
        String openid = accessTokenJson.getString("openid");
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUuid(uuid);
        loginUserDTO.setTokenOutTime(null);
        loginUserDTO.setRefreshTokenOutTime(null);
        loginUserDTO.setOpenid(openid);
        loginUserDTO.setOpenType(ConnectTypeEnum.WECHAT_OPENID);
        loginUserDTO = this.getWechatInfo(loginUserDTO, access_token, openid);
        loginUserDTO.setUnionType(ConnectTypeEnum.WECHAT);
        if (StringUtil.isEmpty(loginUserDTO.getUnionid())){
            throw new ServiceException("403","请将公众号绑定到微信开放平台");
        }
        return loginManager.loginByUnionId(loginUserDTO);
    }

    @Override
    public Map wxAppLogin(String uuid,WeChatUserDTO weChatUserDTO) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        BeanUtils.copyProperties(weChatUserDTO,loginUserDTO);
        loginUserDTO = this.getWechatInfo(loginUserDTO,weChatUserDTO.getAccessToken(), weChatUserDTO.getOpenid());
        loginUserDTO.setUuid(uuid);
        loginUserDTO.setTokenOutTime(WX_TOKEN_VAILD_TIME_APP);
        loginUserDTO.setRefreshTokenOutTime(WX_TOKEN_VAILD_TIME_APP);
        loginUserDTO.setOpenType(ConnectTypeEnum.WECHAT_APP);
        loginUserDTO.setUnionType(ConnectTypeEnum.WECHAT);
        return loginManager.loginByUnionId(loginUserDTO);
    }

    @Override
    public Map miniLogin(WeChatMiniLoginDTO weChatMiniLoginDTO) {
        Map res = new HashMap(16);
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUuid(weChatMiniLoginDTO.getUuid());
        loginUserDTO.setTokenOutTime(WX_TOKEN_VAILD_TIME_MINI);
        loginUserDTO.setRefreshTokenOutTime(WX_TOKEN_VAILD_TIME_MINI);
        String content = wxMiniAutoCode(weChatMiniLoginDTO.getCode());
        logger.debug("miniLogin==content==="+content);
        if (StringUtil.isEmpty(content)){
            res.put("autologin", "fail");
            res.put("reason", "auth_code_fail");
            return res;
        }
        JSONObject json = JSONObject.fromObject(content);
        logger.debug("miniLogin==json==="+json.toString());
        String openid = json.getString("openid");
        loginUserDTO.setOpenid(openid);
        loginUserDTO.setOpenType(ConnectTypeEnum.WECHAT_MINI);
        // 获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        cache.put(WX_MINI_SESSIONKEY+openid,sessionKey,60*60*24*2);
        //获取不到unionid
        JSONObject userInfoJson = connectManager.getUserInfo(weChatMiniLoginDTO.getEdata(), sessionKey, weChatMiniLoginDTO.getIv());
        logger.debug("miniLogin==userInfoJson==="+userInfoJson.toString());
        if (userInfoJson.get("unionId")==null){
            res.put("autologin", "fail");
            res.put("reason", "fail_to_get_unionid");
            return res;
        }
        String unionId = userInfoJson.getString("unionId");
        loginUserDTO.setUnionid(unionId);
        loginUserDTO.setUnionType(ConnectTypeEnum.WECHAT);
        loginUserDTO.setHeadimgurl(userInfoJson.get("avatarUrl")==null?null:userInfoJson.getString("avatarUrl"));
        loginUserDTO.setNickName(userInfoJson.get("nickName")==null?null:userInfoJson.getString("nickName"));
        loginUserDTO.setSex(userInfoJson.getInt("gender")==1?1:0);
        return loginManager.loginByUnionId(loginUserDTO);
    }

    @Override
    public JSONObject getAccessToken(String code) {
        Map<String, String> connectConfig = wechatSignaturer.getConnectConfig(WechatTypeEnmu.WAP.name());
        String appId = connectConfig.get("app_id");
        String secret = connectConfig.get("app_key");
        StringBuffer accessTokenBuffer = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
        accessTokenBuffer.append("appid=").append(appId);
        accessTokenBuffer.append("&secret=").append(secret);
        accessTokenBuffer.append("&code=").append(code);
        accessTokenBuffer.append("&grant_type=authorization_code");
        String access_token_back = HttpUtils.doGet(accessTokenBuffer.toString(),"UTF-8", 1000, 1000);
        JSONObject jsonObject = JSONObject.fromObject(access_token_back);
        logger.debug("getAccessToken==jsonObject==="+jsonObject.toString());
        return jsonObject;
    }


    private LoginUserDTO getWechatInfo(LoginUserDTO loginUserDTO,String accessToken, String openId) {
        StringBuffer wechatInfoBuffer = new StringBuffer("https://api.weixin.qq.com/sns/userinfo?");
        wechatInfoBuffer.append("access_token=").append(accessToken);
        wechatInfoBuffer.append("&openid=").append(openId);
        wechatInfoBuffer.append("&lang=zh_CN");
        String user_info_back = HttpUtils.doGet(wechatInfoBuffer.toString(),"UTF-8", 1000, 1000);
        JSONObject wechatInfoJson = JSONObject.fromObject(user_info_back);
        logger.debug("getWechatInfo==wechatInfoJson==="+wechatInfoJson.toString());
        loginUserDTO.setHeadimgurl(wechatInfoJson.getString("headimgurl"));
        loginUserDTO.setNickName(wechatInfoJson.getString("nickname"));
        if (wechatInfoJson.get("unionid")!=null){
            loginUserDTO.setUnionid(wechatInfoJson.getString("unionid"));
        }
        loginUserDTO.setSex(wechatInfoJson.getInt("sex")==1?1:0);
        return loginUserDTO;
    }


    private String  wxRefreshToken(String appId,String refresh_token){
        StringBuffer accessTokenBuffer = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
        accessTokenBuffer.append("appid=").append(appId);
        accessTokenBuffer.append("&grant_type=refresh_token");
        accessTokenBuffer.append("&refresh_token=").append(refresh_token);
        String refresh_token_back = HttpUtils.doGet(accessTokenBuffer.toString(),"UTF-8", 1000, 1000);
        JSONObject jsonObject = JSONObject.fromObject(refresh_token_back);
        if (jsonObject.get("access_token")==null){
            return null;
        }
        return jsonObject.getString("access_token");
    }

    @Override
    public String getMiniOpenid(String code) {
        String content = wxMiniAutoCode(code);
        if (StringUtil.isEmpty(content)){
            return "";
        }
        JSONObject json = JSONObject.fromObject(content);
        logger.debug("getMiniOpenid==json==="+json.toString());
        String openid = json.getString("openid");
        return openid;
    }

    @Override
    public Map miniBindPhone(String encryptedData,String iv) {
        Map res = new HashMap(16);
        Long uid = UserContext.getBuyer().getUid();
        ConnectDO connectDO = connectMapper.selectOne(new QueryWrapper<ConnectDO>().eq("member_id", uid).eq("union_type", ConnectTypeEnum.WECHAT_MINI));
        if (null == connectDO){
            res.put("bindPhone","fail");
            res.put("reason","fail_to_get_openid");
            return res;
        }
        String openId = connectDO.getUnionId();
        String sessionKey = (String) cache.get(WX_MINI_SESSIONKEY + openId);
        if (StringUtil.isEmpty(sessionKey)){
            res.put("bindPhone","fail");
            res.put("reason","fail_to_get_sessionKey");
            return res;
        }
        try {
            String decryptPhoneStr = decrypt(Base64.decode(sessionKey),Base64.decode(iv),Base64.decode(encryptedData));
            JSONObject jsonObject = JSONObject.fromObject(decryptPhoneStr);
            if (jsonObject.get("phoneNumber")!=null){
                String mobile = jsonObject.getString("phoneNumber");
                memberMapper.update(null,new UpdateWrapper<Member>().set("mobile",mobile).eq("member_id",uid));
                res.put("bindPhone","success");
                res.put("phone",mobile);
                return res;
            }else{
                res.put("bindPhone","fail");
                res.put("reason","fail_to_find_mobile_from_decrypt_data");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("bindPhone","fail");
            res.put("reason",e.getMessage());
            return res;
        }
    }

    public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData),"UTF-8");
    }


    private String wxMiniAutoCode(String code) {
        Map<String, String> connectConfig = wechatSignaturer.getConnectConfig(WechatTypeEnmu.MINI.name());
        String appId = connectConfig.get("app_id");
        String appKey = connectConfig.get("app_key");
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + appId + "&" +
                "secret=" + appKey + "&" +
                "js_code=" + code + "&" +
                "grant_type=authorization_code";
        return HttpUtils.doGet(url, "UTF-8", 100, 1000);
    }

}
