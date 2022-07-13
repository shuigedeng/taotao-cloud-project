package com.taotao.cloud.im.biz.platform.modules.shake.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NearVo01 {

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private Double longitude;
    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private Double latitude;

}
