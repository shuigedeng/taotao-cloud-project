package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import com.mdframe.forge.starter.trans.annotation.DictTrans;
import com.mdframe.forge.starter.trans.annotation.TransField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@DictTrans
public class SysConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @TransField(dictType = "yes_no")
    private String configType;
    
    @TableField(exist = false)
    private String configTypeName;

    /**
     * 参数描述
     */
    private String configDesc;

    /**
     * 排序
     */
    private Integer sort;
}
