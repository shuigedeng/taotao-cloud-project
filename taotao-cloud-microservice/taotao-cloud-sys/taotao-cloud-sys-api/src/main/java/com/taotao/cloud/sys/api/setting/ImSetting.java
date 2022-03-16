package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * IM设置
 *
 */
@Data
public class ImSetting implements Serializable {


    @Schema(description =  "平台地址")
    private String httpUrl;


}
