package com.taotao.cloud.workflow.biz.engine.model.flowengine;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 */
@Data
public class FlowEngineCrForm {
    @NotBlank(message = "流程编码不能为空")
    private String enCode;
    @NotBlank(message = "流程名称不能为空")
    private String fullName;
    @NotNull(message = "流程类型不能为空")
    private int type;
    @NotBlank(message = "流程分类不能为空")
    private String category;
    @NotBlank(message = "流程表单不能为空")
    private String formData;
    @NotNull(message = "流程分类不能为空")
    private int formType;
    private Integer visibleType;
    private String icon;
    private String iconBackground;
    private String version;
    @NotBlank(message = "流程引擎不能为空")
    private String flowTemplateJson;
    private String description;
    private Integer enabledMark;
    @Schema(description = "排序")
    private Long sortCode;
    @Schema(description = "关联表")
    private String tables;
    private String dbLinkId;
    @Schema(description = "app表单路径")
    private String appFormUrl;
    @Schema(description = "pc表单路径")
    private String formUrl;
}
