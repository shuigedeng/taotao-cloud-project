package com.taotao.cloud.workflow.biz.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
