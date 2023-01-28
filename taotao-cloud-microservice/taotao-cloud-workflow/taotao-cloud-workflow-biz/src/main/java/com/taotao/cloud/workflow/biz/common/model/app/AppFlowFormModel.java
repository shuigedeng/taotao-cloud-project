package com.taotao.cloud.workflow.biz.common.model.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class AppFlowFormModel {
    @Schema(description =  "主键id")
    private String id;
    @Schema(description =  "流程名称")
    private String fullName;
    @Schema(description =  "流程分类")
    private String category;
    @Schema(description =  "图标")
    private String icon;
    @Schema(description =  "编码")
    private String enCode;
    @Schema(description =  "图标背景色")
    private String iconBackground;
    @Schema(description =  "表单类型")
    private Integer formType;
    @Schema(description =  "是否常用")
    private Boolean isData;

}
