package com.enation.app.javashop.model.statistics.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.framework.database.annotation.Column;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 会员注册数据
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/3/25 下午10:47
 */

@TableName("es_sss_member_register_data")
public class MemberRegisterData implements Serializable {

    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "会员id")
    @Column(name = "member_id")
    private Long memberId;

    @ApiModelProperty(value = "会员名称")
    @Column(name = "member_name")
    private String memberName;

    @ApiModelProperty(value = "创建日期")
    @Column(name = "create_time")
    private Long createTime;

    public MemberRegisterData() {
    }

//    public MemberRegisterData(Map<String, Object> map) {
//        this.setCreate_time(DateUtil.getDateline());
//        this.setMember_id((Integer) map.get("member_id"));
//        this.setMember_name((String) map.get("member_name"));
//    }

    public MemberRegisterData(Member member) {
        this.setCreateTime(member.getCreateTime());
        this.setMemberId(member.getMemberId());
        this.setMemberName(member.getUname());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemberRegisterData that = (MemberRegisterData) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) {
            return false;
        }
        if (memberName != null ? !memberName.equals(that.memberName) : that.memberName != null) {
            return false;
        }
        return createTime != null ? createTime.equals(that.createTime) : that.createTime == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (memberName != null ? memberName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MemberRegisterData{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
