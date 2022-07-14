package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 */
@Data
public class FlowEngineListVO {
    @ApiModelProperty(value = "编码")
    private String enCode;
    @ApiModelProperty(value = "数量")
    private Integer num;
    @ApiModelProperty(value = "名称")
    private String fullName;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "流程分类")
    private String category;
    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer type;
    @ApiModelProperty(value = "可见类型 0-全部可见、1-部分可见")
    private Integer visibleType;
    @ApiModelProperty(value = "排序码")
    private Long sortCode;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "图标背景色")
    private String iconBackground;
    @ApiModelProperty(value = "创建人")
    private String creatorUser;
    @ApiModelProperty(value = "创建时间")
    private Long creatorTime;
    @ApiModelProperty(value = "有效标志")
    private Integer enabledMark;
    @ApiModelProperty(value = "子节点")
    private List<FlowEngineListVO> children;
}
