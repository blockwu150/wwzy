package com.enation.app.javashop.model.nft.dos;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LongTypeHandler;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ygg
 * @version v7.2.2
 * @Description NFT会员行为
 * @ClassName NftJob
 * @since v7.2.2 下午2:43 2022/4/21
 */
@TableName(value = "es_nft_job")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftJob implements Serializable {
    private static final long serialVersionUID = -9003022590724823978L;
    /**
     * 行为
     */
    public final static Integer JOB_DEFINE = 1;//定义
    public final static Integer JOB_MINT = 2;//定义Mint给别人
    public final static Integer JOB_WITHDRAWAL = 3;//转给别人
    public final static Integer JOB_REGISTRY = 4;//转给别人
//    public final static Integer ACTION_WITHDRAW = 4;//转给别人
//    public final static Integer ACTION_CLAIM = 5;//声明持有
//    public final static Integer ACTION_CHECK_TX = 6;//检查交易状态

    /**
     * 状态
     */
    public final static Integer STATUS_READY = 0;//预备
    public final static Integer STATUS_PROCESSING = 1;//处理中
    public final static Integer STATUS_EXCEPTION = 2;//异常

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(name = "job_id", value = "id")
    private Long jobId;

    /**
     * 会员ID
     */
    @ApiModelProperty(name = "member_id", value = "会员id")
    private Long memberId;

    /**
     * 工作
     */
    @ApiModelProperty(name = "job", value = "工作")
    private Integer job;

    /**
     * 工作状态
     */
    @ApiModelProperty(name = "status", value = "0 ready 1 processing 2 error")
    private Integer status = 0;
    /**
     * 工作状态
     */
    @ApiModelProperty(name = "exception", value = "异常消息")
    private String exception;

    /**
     * 上下文
     */
    @ApiModelProperty(name = "context", value = "上下文")
    private String context;

    /**
     * 完成时间
     */
    @ApiModelProperty(name = "complete_time", value = "完成时间")
    private Long completeTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time", value = "创建时间")
    private Long createTime;

    public NftJob() {
    }

    public NftJob(Long memberId, Integer job) {
        this.memberId = memberId;
        this.job = job;
        this.createTime = System.currentTimeMillis() / 1000;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    /**
     * bussiness method
     */
    transient protected List<NftAction> actions;

    public List<NftAction> getActions() {
        return actions;
    }

    public void setActions(List<NftAction> actions) {
        this.actions = actions;
    }

    public List<NftAction> genActions(Map context) {
        List<NftAction> actions = new LinkedList<>();

        //context isRegistry boolean 是否注册
        //context chargeKey 充值键
        //context chargPwd 充值密码
        //context chargAddress 充值地址
        //context privateKey String  转出私钥
        //context pwd String 密码
        //context address String  地址
        //context collection NftCollection  收藏品对象
        //context -- cotaNftPlugin nft插件
        //context -- nftCollectionMapper 收藏品mapper
        if (job.equals(JOB_DEFINE)) {
            NftAction define = new NftAction(jobId, NftAction.ACTION_DEFINE);
            NftAction checkDefine = new NftAction(jobId, NftAction.ACTION_CHECK_TX);
            NftAction done = new NftAction(jobId, NftAction.ACTION_BE_COLLECTION);
            actions.add(define);
            actions.add(checkDefine);
            actions.add(done);
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
        //context toPwd String  提取码
        //context collection NftCollection  收藏品对象
        //context num Integer 数量
        //context -- cotaNftPlugin nft插件
        //context -- nftCollectionMapper 收藏品mapper
        if (job.equals(JOB_MINT)) {
            actions.add(new NftAction(jobId, NftAction.ACTION_MINT));
            actions.add(new NftAction(jobId, NftAction.ACTION_CHECK_TX));
            actions.add(new NftAction(jobId, NftAction.ACTION_CLAIM));
            actions.add(new NftAction(jobId, NftAction.ACTION_CHECK_TX));
//            actions.get(0).setStatus(NftAction.STATUS_DOING);
        }
        //context memberId long 出让人Id
        //context receiverId long 受让人Id
        //context cotaId string cotaId
        //context tokenIndex string 16进制 tokenIndex
        if (job.equals(JOB_WITHDRAWAL)) {
            actions.add(new NftAction(jobId, NftAction.ACTION_WITHDRAWAL));
            actions.add(new NftAction(jobId, NftAction.ACTION_CHECK_TX));
            actions.add(new NftAction(jobId, NftAction.ACTION_CLAIM));
            actions.add(new NftAction(jobId, NftAction.ACTION_CHECK_TX));
//            actions.get(0).setStatus(NftAction.STATUS_DOING);
        }
        //context memberId long 出让人Id
        if (job.equals(JOB_REGISTRY)) {
            actions.add(new NftAction(jobId, NftAction.ACTION_REGISTRY));
            actions.add(new NftAction(jobId, NftAction.ACTION_CHECK_TX));
            actions.add(new NftAction(jobId, NftAction.ACTION_BE_REGISTRY));
//            actions.get(0).setStatus(NftAction.STATUS_DOING);
        }
        return actions;
    }

    public void process(Map<String, Object> services) {
        Map context = JSONUtil.parseObj(this.context);
        //处理前改成处理中状态
        setStatus(STATUS_PROCESSING);
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getStatus().equals(NftAction.STATUS_DOING) && actions.get(i).exec(context, services)) {
                actions.get(i).setStatus(NftAction.STATUS_DONE);
                if (i + 1 < actions.size()) {
                    actions.get(i + 1).setStatus(NftAction.STATUS_DOING);
                }
            }
        }
        if (actions.get(actions.size() - 1).getStatus().equals(NftAction.STATUS_DONE)) {
            this.completeTime = System.currentTimeMillis() / 1000;
        }
        //处理完成回到ready状态
        if (getStatus().equals(STATUS_PROCESSING)) {
            setStatus(STATUS_READY);
        }
        setContext(JSONUtil.parseObj(context).toString());

    }

}
