package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
public class SysDictType extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型（租户内唯一）
     */
    private String dictType;

    /**
     * 字典状态（0-禁用，1-正常）
     */
    private Integer dictStatus;

    /**
     * 备注
     */
    private String remark;
}
