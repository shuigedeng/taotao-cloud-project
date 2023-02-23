package com.taotao.cloud.workflow.biz.app.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * app应用
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-08
 */
@Data
public class AppPositionVO {
    @ApiModelProperty(value = "岗位id")
    private String id;
    @ApiModelProperty(value = "岗位名称")
    private String name;
}
