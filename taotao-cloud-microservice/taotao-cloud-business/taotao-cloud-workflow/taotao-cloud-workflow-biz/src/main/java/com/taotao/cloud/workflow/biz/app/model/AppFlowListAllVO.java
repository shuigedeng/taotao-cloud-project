package com.taotao.cloud.workflow.biz.app.model;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * app常用数据
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-08
 */
@Data
public class AppFlowListAllVO {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "图标")
    private String fullName;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "图标背景色")
    private String iconBackground;
    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @ApiModelProperty(value = "编码")
    private String enCode;
    @ApiModelProperty(value = "是否常用")
    private Boolean isData;
    @ApiModelProperty(value = "子节点")
    private List<AppFlowListAllVO> children;
}
