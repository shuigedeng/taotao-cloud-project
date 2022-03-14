package com.taotao.cloud.sys.api.setting;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * IM设置
 *
 */
@Data
public class ImSetting implements Serializable {


    @ApiModelProperty(value = "平台地址")
    private String httpUrl;


}
