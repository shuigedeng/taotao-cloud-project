package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分设置
 *
 */
@Data
public class PointSetting implements Serializable {

    private static final long serialVersionUID = -4261856614779031745L;
    @Schema(description =  "注册")
    private Integer register;

    @Schema(description =  "消费1元赠送多少积分")
    private Integer consumer;

    @Schema(description =  "积分付款X积分=1元")
    private Integer money;

    @Schema(description =  "每日签到积分")
    private Integer signIn;

    @Schema(description =  "订单评价赠送积分")
    private Integer comment;

    @Schema(description =  "积分具体设置")
    private List<PointSettingItem> pointSettingItems = new ArrayList<>();

    public Integer getRegister() {
        if (register == null || register < 0) {
            return 0;
        }
        return register;
    }

    public Integer getMoney() {
        if (money == null || money < 0) {
            return 0;
        }
        return money;
    }

    public Integer getConsumer() {
        if (consumer == null || consumer < 0) {
            return 0;
        }
        return consumer;
    }

    public Integer getSignIn() {
        if (signIn == null || signIn < 0) {
            return 0;
        }
        return signIn;
    }

    public Integer getComment() {
        if (comment == null || comment < 0) {
            return 0;
        }
        return comment;
    }
}
