package com.taotao.cloud.sys.biz.aggregate;

import lombok.Data;

/**
 * 地址基本条件查询对象
 */
@Data
//@ApiModel("(地址)基本条件查询对象")
public class AddressCondition {
    /**
     * 省
     */
//    @ApiModelProperty(value = "省")
    private String province;
    /**
     * 城市
     */
//    @ApiModelProperty(value = "城市")
    private String city;
    /**
     * 县/区
     */
//    @ApiModelProperty(value = "县/区")
    private String district;
}
