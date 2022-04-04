package com.taotao.cloud.operation.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 专题活动
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("li_special")
@ApiModel(value = "专题活动")
public class Special extends BaseEntity {

    @Schema(description =  "专题活动名称")
    private String specialName;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description =  "楼层对应连接端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String clientType;

    @Schema(description =  "页面ID")
    private String pageDataId;
}
