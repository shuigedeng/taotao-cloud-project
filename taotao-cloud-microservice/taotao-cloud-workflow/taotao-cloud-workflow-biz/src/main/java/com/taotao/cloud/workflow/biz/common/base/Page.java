package com.taotao.cloud.workflow.biz.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class Page {
    @Schema(description =  "关键字")
    private String keyword="";
}
