package com.taotao.cloud.sys.biz.aggregate;

import lombok.Data;

/**
 * 地址模糊查询对象
 */
@Data
//@ApiModel("(地址)模糊查询对象")
public class AddressFuzzyQueries {
    /**
     * 用户手机号
     */
//    @ApiModelProperty(value = "用户手机号(可模糊)")
    private String phone;
    /**
     * 用户姓名
     */
//    @ApiModelProperty(value = "用户姓名(可模糊)")
    private String name;
    /**
     * 详细地址
     */
//    @ApiModelProperty(value = "详细地址(可模糊)")
    private String address;
}
