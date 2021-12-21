package com.taotao.cloud.order.api.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单交易投诉VO
 *
 * 
 * @since 2020/12/4
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreAppealVO {

    @ApiModelProperty(value = "投诉id")
    private String orderComplaintId;

    @ApiModelProperty(value = "申诉商家内容")
    private String appealContent;

    @ApiModelProperty(value = "申诉商家上传的图片")
    private String appealImages;
}
