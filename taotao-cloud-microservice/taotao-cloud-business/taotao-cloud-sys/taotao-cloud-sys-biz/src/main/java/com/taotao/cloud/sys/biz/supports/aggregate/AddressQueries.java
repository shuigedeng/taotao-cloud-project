package com.taotao.cloud.sys.biz.supports.aggregate;

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 *地址直接性查询对象
 */
@Data
//@ApiModel("(地址)直接性查询对象")
public class AddressQueries {
    /**
     * 用户ID
     */
//    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /**
     * 用户手机号
     */
//    @ApiModelProperty(value = "用户手机号")
    private String phone;
}
