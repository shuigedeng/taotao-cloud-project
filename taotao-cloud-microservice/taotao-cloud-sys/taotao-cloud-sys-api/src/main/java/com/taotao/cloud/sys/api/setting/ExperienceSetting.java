package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 经验值设置
 *
 */
@Data
public class ExperienceSetting implements Serializable {

    private static final long serialVersionUID = -4261856614779031745L;
    @Schema(description =  "注册")
    private Integer register;

    @Schema(description =  "每日签到经验值")
    private Integer signIn;

    @Schema(description =  "订单评价赠送经验值")
    private Integer comment;

    @Schema(description =  "分享获取经验值")
    private Integer share;

    @Schema(description =  "购物获取经验值,一元*经验值")
    private Integer money;

}
