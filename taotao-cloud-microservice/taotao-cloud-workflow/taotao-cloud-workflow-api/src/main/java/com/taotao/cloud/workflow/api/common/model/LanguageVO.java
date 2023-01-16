package com.taotao.cloud.workflow.api.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class LanguageVO {
    @Schema(description =  "语言编码")
    private String encode;
    @Schema(description =  "语言名称")
    private String fullName;
}
