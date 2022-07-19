package com.enation.app.javashop.model.system.dto;

import com.enation.app.javashop.model.system.dos.AdminUser;
import io.swagger.annotations.ApiModelProperty;

/**
 * 管理员对象 用于管理员列表显示
 *
 * @author zh
 * @version v7.0
 * @date 18/6/27 下午2:42
 * @since v7.0
 */

public class AdminUserDTO extends AdminUser {

    @ApiModelProperty(name = "role_name", value = "角色名称", required = false)
    private String roleName;

    public String getRoleName() {
        if (this.getFounder().equals(1)) {
            roleName = "超级管理员";
        }
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    @Override
    public String toString() {
        return "AdminUserDTO{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
