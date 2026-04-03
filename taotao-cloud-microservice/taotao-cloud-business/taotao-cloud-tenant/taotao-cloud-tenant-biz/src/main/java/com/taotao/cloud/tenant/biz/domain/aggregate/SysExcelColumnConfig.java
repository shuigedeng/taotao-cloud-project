package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Excel列配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_excel_column_config")
public class SysExcelColumnConfig extends BaseEntity {
    
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关联配置键
     */
    private String configKey;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 列名（表头）
     */
    private String columnName;

    /**
     * 列宽
     */
    private Integer width;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否导出
     */
    private Boolean export;

    /**
     * 日期格式
     */
    private String dateFormat;

    /**
     * 数字格式
     */
    private String numberFormat;

    /**
     * 字典类型
     */
    private String dictType;
}
