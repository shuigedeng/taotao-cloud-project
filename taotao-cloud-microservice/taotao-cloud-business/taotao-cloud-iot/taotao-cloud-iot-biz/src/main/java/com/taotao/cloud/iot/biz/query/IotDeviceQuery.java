package com.taotao.cloud.iot.biz.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;

/**
 * 设备表查询
 *
 * @author LSF maku_lsf@163.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "设备表查询")
public class IotDeviceQuery extends Query {

    @Schema(description = "状态，0禁用，1正常")
    private Integer status;

    @Schema(description = "运行状态，0.离线状态 1.在线状态 2.正常待机 3.用户使用中 4.OTA升级中")
    private Integer runningStatus;

    @Schema(description = "设备类型，1.手持设备，2.柜体，3传感设备")
    private Integer type;
}
