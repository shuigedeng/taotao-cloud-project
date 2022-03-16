package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分签到设置
 *
 */
@Data
public class PointSettingItem implements Comparable<PointSettingItem>, Serializable {


    @Schema(description =  "签到天数")
    private Integer day;


    @Schema(description =  "赠送积分")
    private Integer point;

    public Integer getPoint() {
        if (point == null || point < 0) {
            return 0;
        }
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public int compareTo(PointSettingItem pointSettingItem) {
        //return this.day - pointSettingItem.getDay();
	    return 0;
    }
}
