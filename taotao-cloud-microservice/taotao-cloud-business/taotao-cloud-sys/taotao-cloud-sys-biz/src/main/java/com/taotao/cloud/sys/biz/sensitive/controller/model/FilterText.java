package com.taotao.cloud.sys.biz.sensitive.controller.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author yweijian
 * @date 2022年9月20日
 * @version 2.0.0
 * @description
 */
@ApiModel(value = "敏感词文本参数")
@Data
public class FilterText implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name = "text", value = "待检测文本", required = true)
    private String text;
}
