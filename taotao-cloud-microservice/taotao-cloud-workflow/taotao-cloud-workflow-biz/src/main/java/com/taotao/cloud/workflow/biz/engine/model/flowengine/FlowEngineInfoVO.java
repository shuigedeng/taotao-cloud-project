package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:17
 */
@Data
public class FlowEngineInfoVO {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "排序码")
    private Long sortCode;
    @ApiModelProperty(value = "流程编码")
    private String enCode;
    @ApiModelProperty(value = "流程名称")
    private String fullName;
    @ApiModelProperty(value = "流程类型")
    private Integer type;
    @ApiModelProperty(value = "流程分类")
    private String category;
    @ApiModelProperty(value = "可见类型 0-全部可见、1-指定经办")
    private Integer visibleType;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "图标背景色")
    private String iconBackground;
    @ApiModelProperty(value = "流程版本")
    private String version;
    @ApiModelProperty(value = "流程模板")
    private String flowTemplateJson;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "有效标志")
    private Integer enabledMark;
    @ApiModelProperty(value = "表单字段")
    private String formData;
    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @ApiModelProperty(value = "关联表")
    private String tables;
    @ApiModelProperty(value = "数据连接")
    private String dbLinkId;
    @ApiModelProperty(value = "app表单路径")
    private String appFormUrl;
    @ApiModelProperty(value = "pc表单路径")
    private String formUrl;
}
