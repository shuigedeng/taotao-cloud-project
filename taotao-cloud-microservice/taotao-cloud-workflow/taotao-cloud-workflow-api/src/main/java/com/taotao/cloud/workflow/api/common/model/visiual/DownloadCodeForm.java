package com.taotao.cloud.workflow.api.common.model.visiual;

import lombok.Data;

/**
 *
 */
@Data
public class DownloadCodeForm {
    /**
     * 所属模块
     */
    private String module;
    /**
     * 主功能名称
     */
    private String className;
    /**
     * 子表名称集合
     */
    private String subClassName;
    /**
     * 主功能备注
     */
    private String description;

    /**
     * 数据源id
     */
    private String dataSourceId;

}
