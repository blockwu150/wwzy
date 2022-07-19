package com.enation.app.javashop.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.client.system.EmailClient;
import com.enation.app.javashop.client.system.SmsClient;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.SceneType;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.member.MemberSecurityManager;
import com.enation.app.javashop.service.passport.PassportManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员安全业务实现
 *
 * @author zh
 * @version v7.0
 * @date 18/4/23 下午3:24
 * @since v7.0
 */
@Service
public class MemberSecurityManagerImpl implements MemberSecurityManager {

    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SmsClient smsClient;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private Cache cache;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private EmailClient emailClient;

    @Override
    public void sendBindSmsCode(String mobile) {
        //校验手机号码格式
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member != null) {
            throw new ServiceException(MemberErrorCode.E111.code(), "此手机号码已经绑定其他用户");
        }
        //发送(发送手机短信)消息
        smsClient.sendSmsMessage("手机绑定操作", mobile, SceneType.BIND_MOBILE);
    }

    @Override
    public void sendBindEmailCode(String email) {
        //校验电子邮箱格式
        if (!Validator.isEmail(email)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "电子邮箱格式不正确");
        }

        //校验当前电子邮箱是否已经绑定了其他会员
        Buyer buyer = UserContext.getBuyer();
        this.validEmailBindMember(email, buyer.getUid());
        //发送(发送邮箱验证码)消息
        emailClient.sendEmailMessage("邮箱绑定操作", email, SceneType.BIND_EMAIL);
    }

    @Override
    public void sendValidateSmsCode(String mobile) {
        //校验手机号格式
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        smsClient.sendSmsMessage("手机验证码验证", mobile, SceneType.VALIDATE_MOBILE);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePassword(Long memberId, String password) {
        //校验是否经过手机验证而进行此步骤
        Member member = memberManager.getModel(memberId);
        //校验当前会员是否存在
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //校验当前会员是否被禁用
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用");
        }
        //将密码进行md5加密
        String newPassword = StringUtil.md5(password + member.getUname().toLowerCase());
        //新建会员对象
        Member updateMember = new Member();
        //设置会员密码
        updateMember.setPassword(newPassword);
        //设置会员ID
        updateMember.setMemberId(memberId);
        //修改会员密码
        memberMapper.updateById(updateMember);
        //清除步骤标记缓存
        passportManager.clearSign(member.getMobile(), SceneType.VALIDATE_MOBILE.name());
    }

    @Override
    public void bindMobile(String mobile) {
        Buyer buyer = UserContext.getBuyer();
        //校验手机号码是否已经被占用
        Member member = memberManager.getModel(buyer.getUid());
        if (member != null && !StringUtil.isEmpty(member.getMobile())) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前会员已经绑定手机号");
        }

        //新建查询条件包装器
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        //以会员手机号码为查询条件
        wrapper.eq("mobile", mobile);
        //查询会员集合
        List list = memberMapper.selectList(wrapper);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前手机号已经被占用");
        }

        //新建会员对象
        Member updateMember = new Member();
        //设置会员手机号码
        updateMember.setMobile(mobile);
        //设置会员ID
        updateMember.setMemberId(member.getMemberId());
        //修改会员手机号码
        memberMapper.updateById(updateMember);
    }

    @Override
    public void bindEmail(String email) {
        //获取当前登录会员信息
        Buyer buyer = UserContext.getBuyer();
        //校验会员邮箱
        this.validEmailBindMember(email, buyer.getUid());

        //新建会员对象
        Member updateMember = new Member();
        //设置会员邮箱
        updateMember.setEmail(email);
        //设置会员ID
        updateMember.setMemberId(buyer.getUid());
        //修改会员邮箱
        memberMapper.updateById(updateMember);
    }

    @Override
    public void changeBindMobile(String mobile) {
        //获取当前登录会员信息
        Buyer buyer = UserContext.getBuyer();
        //如果当前登录会员信息不为空
        if (buyer != null) {
            //校验是否经过手机验证而进行此步骤
            Member member = memberManager.getModel(buyer.getUid());
            String str = StringUtil.toString(cache.get(CachePrefix.MOBILE_VALIDATE.getPrefix() + "_" + SceneType.VALIDATE_MOBILE.name() + "_" + member.getMobile()));
            //当前用户已绑定手机号才做验证，未绑定则不验证
            if (!StringUtil.isEmpty(member.getMobile())&&StringUtil.isEmpty(str)) {
                throw new ServiceException(MemberErrorCode.E115.code(), "对已绑定手机校验失效");
            }

            //新建查询条件包装器
            QueryWrapper<Member> wrapper = new QueryWrapper<>();
            //以会员手机号为查询条件
            wrapper.eq("mobile", mobile);
            //查询会员信息集合
            List list = memberMapper.selectList(wrapper);
            if (list.size() > 0) {
                throw new ServiceException(MemberErrorCode.E111.code(), "当前手机号已经被占用");
            }

            //新建会员对象
            Member updateMember = new Member();
            //设置会员手机号码
            updateMember.setMobile(mobile);
            //设置会员ID
            updateMember.setMemberId(member.getMemberId());
            //修改会员手机号码
            memberMapper.updateById(updateMember);

            //清除步骤标记缓存
            passportManager.clearSign(member.getMobile(), SceneType.VALIDATE_MOBILE.name());
        }
    }

    /**
     * 判断电子邮箱是否已被绑定(不包含当前登录会员)
     * @param email
     * @param memberId
     */
    private void validEmailBindMember(String email, Long memberId) {
        //新建查询条件包装器
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        //以电子邮箱为查询条件
        wrapper.eq("email", email);
        //以会员ID为查询条件
        wrapper.eq("member_id", memberId);
        //查询会员信息集合
        List list = memberMapper.selectList(wrapper);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前电子邮箱已经被绑定");
        }
    }
}
