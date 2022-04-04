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
public class PageDataListVO {

    @Schema(description =  "页面ID")
    private String id;
    @Schema(description =  "页面名称")
    private String name;
    /**
     * @see SwitchEnum
     */
    @Schema(description =  "页面开关状态", allowableValues = "OPEN,CLOSE")
    private String pageShow;
}
