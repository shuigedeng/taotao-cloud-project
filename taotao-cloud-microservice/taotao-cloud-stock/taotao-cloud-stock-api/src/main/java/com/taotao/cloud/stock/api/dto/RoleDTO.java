package com.taotao.cloud.stock.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色DTO
 *
 * @author shuigedeng
 * @date 2021-02-18
 */
@Data
public class RoleDTO implements Serializable {

    public RoleDTO() {
    }

    public RoleDTO(String id, String roleCode, String roleName, String remarks, List<String> permissionIdList) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.remarks = remarks;
        this.permissionIdList = permissionIdList;
    }

    /**
     * id
     */
    private String id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 权限
     */
    private List<String> permissionIdList;

}
