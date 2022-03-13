package com.taotao.cloud.operation.api.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 楼层装修数据VO
 *
 */
@Data
public class PageDataVO {

    @Schema(description =  "页面数据")
    private String pageData;
}
