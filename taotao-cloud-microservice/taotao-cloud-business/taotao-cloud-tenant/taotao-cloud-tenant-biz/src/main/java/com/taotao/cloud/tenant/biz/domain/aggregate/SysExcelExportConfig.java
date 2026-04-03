package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Excel导出配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_excel_export_config")
public class SysExcelExportConfig extends BaseEntity {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 配置键（唯一标识）
     */
    private String configKey;

    /**
     * 导出名称
     */
    private String exportName;

    /**
     * Sheet名称
     */
    private String sheetName;

    /**
     * 文件名模板
     */
    private String fileNameTemplate;

    /**
     * 数据源Bean名称
     */
    private String dataSourceBean;

    /**
     * 数据查询方法名
     */
    private String queryMethod;

    /**
     * 是否自动翻译字典
     */
    private Boolean autoTrans;

    /**
     * 是否分页查询
     */
    private Boolean pageable;

    /**
     * 最大导出条数
     */
    private Integer maxRows;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortOrder;

    /**
     * 状态（1-启用，0-禁用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
