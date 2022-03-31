package com.taotao.cloud.operation.api.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼层装修数据VO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDataVO {

    @Schema(description =  "页面数据")
    private String pageData;
}
