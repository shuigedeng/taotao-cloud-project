package com.taotao.cloud.workflow.api.common.model.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class LoginVO {
    @Schema(description =  "token")
    private String token;
    @Schema(description =  "主题")
    private String theme;
}
