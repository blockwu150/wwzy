package com.enation.app.javashop.service.passport.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.framework.auth.Token;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.TokenManager;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.EmojiCharacterUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.mapper.member.ConnectMapper;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.message.MemberRegisterMsg;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.ConnectDO;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.member.dto.LoginUserDTO;
import com.enation.app.javashop.model.member.dto.WeChatUserDTO;
import com.enation.app.javashop.model.member.enums.ConnectTypeEnum;
import com.enation.app.javashop.model.member.vo.Auth2Token;
import com.enation.app.javashop.model.member.vo.MemberLoginMsg;
import com.enation.app.javashop.model.member.vo.MemberVO;
import com.enation.app.javashop.service.member.ConnectManager;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.passport.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 三方登陆服务
 * @author cs
 * 2020/11/02
 */
@Service
public class LoginManagerImpl implements LoginManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConnectMapper connectMapper;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private Cache cache;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private TokenManager tokenManager;


    /**
     * 根据UnionId登陆
     * @param loginUserDTO
     * @return
     */
    @Override
    public Map loginByUnionId(LoginUserDTO loginUserDTO){
        Map res = new HashMap(16);
        //通过unionid查找会员(es_connect表)
        ConnectDO connectDO= findConnectByUnionId(loginUserDTO.getUnionid(),loginUserDTO.getUnionType());
        Member member = null;
        if(connectDO==null){
            //没找到注册一个
            member = register(loginUserDTO);
        }else{
            member=findMemberById(connectDO.getMemberId());
            //查看当前登陆终端该账户openid是否已记录，如果未记录则新增记录
            if (loginUserDTO.getOpenType()!=null){
                ConnectDO aDo = connectMapper.selectOne(new QueryWrapper<ConnectDO>().eq("member_id", member.getMemberId()).eq("union_type", loginUserDTO.getOpenType().value()));
                if (aDo==null){
                    ConnectDO connect = new ConnectDO();
                    connect.setMemberId(member.getMemberId());
                    connect.setUnionId(loginUserDTO.getOpenid());
                    connect.setUnionType(loginUserDTO.getOpenType().value());
                    connectMapper.insert(connect);
                }
            }
        }
        //存储uuid和unionId的关系
        Auth2Token auth2Token = new Auth2Token();
        if (null == loginUserDTO.getOpenType()){
            auth2Token.setType(loginUserDTO.getUnionType().value());
        }else{
            auth2Token.setType(loginUserDTO.getOpenType().value());
        }
        auth2Token.setUnionid(loginUserDTO.getUnionid());
        if (!StringUtil.isEmpty(loginUserDTO.getOpenid())){
            auth2Token.setOpneId(loginUserDTO.getOpenid());
        }
        logger.debug("三方登录openId为：" + loginUserDTO.getOpenid()+";unionid为"+loginUserDTO.getUnionid());
        cache.put(CachePrefix.CONNECT_LOGIN.getPrefix() + loginUserDTO.getUuid(), auth2Token);
        MemberVO memberVO = this.connectWeChatLoginHandle(member, loginUserDTO.getUuid(),loginUserDTO.getTokenOutTime(),loginUserDTO.getRefreshTokenOutTime());
        res.put("access_token", memberVO.getAccessToken());
        res.put("refresh_token", memberVO.getRefreshToken());
        res.put("uid", memberVO.getUid());
        return res;
    }

    /**
     * 根据unionId查询connect
     * @param unionId
     * @return
     */
    private ConnectDO findConnectByUnionId(String unionId,ConnectTypeEnum unionType) {
        return connectMapper.selectOne(new QueryWrapper<ConnectDO>().eq("union_id",unionId).eq("union_type", unionType.value()));
    }

    /**
     * 注册
     * @param loginUserDTO
     * @return
     */
    private Member  register(LoginUserDTO loginUserDTO) {
        String usernamePrefix = "m_";
        String usernameSuffix = UUID.fastUUID().toString(true).substring(0,9);
        String username = usernamePrefix+usernameSuffix;
        Member memberByName = memberManager.getMemberByName(username);
        while (memberByName!=null){
            usernameSuffix = UUID.fastUUID().toString(true).substring(0,9);
            username = usernamePrefix+usernameSuffix;
            memberByName = memberManager.getMemberByName(username);
        }
        Member member = new Member();
        member.setUname(username);
        member.setSex(loginUserDTO.getSex());
        long dateline = DateUtil.getDateline();
        member.setCreateTime(dateline);
        member.setFace(loginUserDTO.getHeadimgurl());
        member.setRegisterIp(ThreadContextHolder.getHttpRequest().getRemoteAddr());
        member.setLoginCount(1);
        member.setLastLogin(dateline);
        member.setDisabled(0);
        String  nickname = loginUserDTO.getNickName();
        nickname = StringUtil.isEmpty(nickname)?username: EmojiCharacterUtil.encode(nickname);
        member.setNickname(nickname);
        member.setProvince(loginUserDTO.getProvince());
        member.setCity(loginUserDTO.getCity());
        //设置会员等级积分为0
        member.setGradePoint(0L);
        //设置会员消费积分为0
        member.setConsumPoint(0L);
        //设置会员是否开启店铺 0：否，1：是
        member.setHaveShop(0);
        //设置会员是否完善了个人信息 0：否，1：是
        member.setInfoFull(0);
        memberMapper.insert(member);
        addConnect(loginUserDTO, member);
        //组织数据结构发送会员注册消息
        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(member);
        memberRegisterMsg.setUuid(ThreadContextHolder.getHttpRequest().getHeader("uuid"));
        this.messageSender.send(new MqMessage(AmqpExchange.MEMEBER_REGISTER, AmqpExchange.MEMEBER_REGISTER + "_ROUTING", memberRegisterMsg));
        return member;
    }

    private void addConnect(LoginUserDTO loginUserDTO, Member member) {
        //写入UnionId
        ConnectDO connect = new ConnectDO();
        connect.setMemberId(member.getMemberId());
        connect.setUnionType(loginUserDTO.getUnionType().value());
        connect.setUnionId(loginUserDTO.getUnionid());
        connectMapper.insert(connect);
        if (!StringUtil.isEmpty(loginUserDTO.getOpenid())){
            //写入openId
            connect = new ConnectDO();
            connect.setMemberId(member.getMemberId());
            connect.setUnionType(loginUserDTO.getOpenType().value());
            connect.setUnionId(loginUserDTO.getOpenid());
            connectMapper.insert(connect);
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param memberId
     * @return
     */
    private Member findMemberById(Long memberId) {
        Member member = memberManager.getModel(memberId);
        return member;
    }

    /**
     * 生成member的token
     *
     * @param member
     * @param uuid
     * @return
     */
    private MemberVO convertWechatMember(Member member, String uuid,Integer tokenOutTime,Integer refreshTokenOutTime) {
        //校验当前账号是否被禁用
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
        }

        //新建买家用户角色对象
        Buyer buyer = new Buyer();
        //设置用户ID
        buyer.setUid(member.getMemberId());
        //设置用户名称
        buyer.setUsername(member.getUname());
        //设置uuid
        buyer.setUuid(uuid);
        //创建Token
        Token token = tokenManager.create(buyer,tokenOutTime,refreshTokenOutTime);
        //获取访问Token
        String accessToken = token.getAccessToken();
        //获取刷新Token
        String refreshToken = token.getRefreshToken();
        //组织返回数据
        MemberVO memberVO = new MemberVO(member, accessToken, refreshToken);
        return memberVO;
    }


    public MemberVO connectWeChatLoginHandle(Member member, String uuid,Integer tokenOutTime,Integer refreshTokenOutTime) {
        //初始化会员信息
        MemberVO memberVO = this.convertWechatMember(member, uuid,tokenOutTime,refreshTokenOutTime);
        //发送登录消息
        this.sendMessage(member, 1);
        return memberVO;
    }

    /**
     * 发送登录消息
     *
     * @param member
     * @param memberOrSeller
     */
    private void sendMessage(Member member, Integer memberOrSeller) {
        MemberLoginMsg loginMsg = new MemberLoginMsg();
        loginMsg.setLastLoginTime(member.getLastLogin());
        loginMsg.setMemberId(member.getMemberId());
        loginMsg.setMemberOrSeller(memberOrSeller);
        this.messageSender.send(new MqMessage(AmqpExchange.MEMEBER_LOGIN, AmqpExchange.MEMEBER_LOGIN + "_ROUTING", loginMsg));
    }

}
