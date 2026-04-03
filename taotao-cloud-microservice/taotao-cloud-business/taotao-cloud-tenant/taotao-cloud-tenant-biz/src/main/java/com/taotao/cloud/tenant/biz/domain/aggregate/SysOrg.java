package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 组织表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_org")
public class SysOrg extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 父级组织ID（0为顶级）
     */
    private Long parentId;

    /**
     * 祖级编码（逗号分隔，如：1,2,3）
     */
    private String ancestors;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 组织类型（1-公司，2-部门，3-小组）
     */
    private Integer orgType;

    /**
     * 组织状态（0-禁用，1-正常）
     */
    private Integer orgStatus;

    /**
     * 负责人ID（关联sys_user.id）
     */
    private Long leaderId;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 组织联系电话
     */
    private String phone;

    /**
     * 组织地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;
    
    /**
     * 行政区划编码
     */
    private String regionCode;

    /**
     * 子组织列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<SysOrg> children;
}
