
package com.taotao.cloud.operation.api.dto;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.modules.page.entity.enums.PageEnum;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼层装修数据DTO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDataDTO {

    @Schema(description =  "值")
    private String num;

    /**
     * @see PageEnum
     */
    @Schema(description =  "页面类型", allowableValues = "INDEX,STORE,SPECIAL")
    private String pageType;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description =  "客户端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String pageClientType;

    public PageDataDTO(String pageType) {
        this.pageType = pageType;
    }
}
