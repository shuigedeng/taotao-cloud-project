package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
public class SysPost extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 岗位编码（租户内唯一）
     */
    private String postCode;

    /**
     * 所属组织ID（关联sys_org.id）
     */
    private Long orgId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位状态（0-禁用，1-正常）
     */
    private Integer postStatus;

    /**
     * 岗位类型（1-管理岗，2-技术岗，3-业务岗，4-其他）
     */
    private Integer postType;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;
}
