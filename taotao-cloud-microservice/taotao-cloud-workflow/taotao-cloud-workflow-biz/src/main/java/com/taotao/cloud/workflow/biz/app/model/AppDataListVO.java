package com.taotao.cloud.workflow.biz.app.model;

import io.swagger.annotations.ApiModelProperty;
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
public class AppDataListVO {
    private String id;
    private String objectId;
    @ApiModelProperty(value = "数据")
    private String objectData;
}
