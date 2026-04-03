package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 上级字典编码
     */
    private Long parentDictCode;

    /**
     * 关联的字典类型
     */
    private String linkedDictType;

    /**
     * 关联的字典值
     */
    private String linkedDictValue;

    /**
     * 字典状态（0-禁用，1-正常）
     */
    private Integer dictStatus;

    /**
     * 备注
     */
    private String remark;
}
