package com.taotao.cloud.workflow.biz.common.model.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class UserPositionVO {
    @Schema(description =  "岗位id")
    private String id;
    @Schema(description =  "岗位名称")
    private String name;
}
