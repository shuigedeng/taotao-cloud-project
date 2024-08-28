package com.taotao.cloud.iot.biz.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;

/**
 * 设备事件日志查询
 *
 * @author LSF maku_lsf@163.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "设备事件日志查询")
public class IotDeviceEventLogQuery extends Query {

    @Schema(description = "指令")
    private String eventTypeEnum;

    @Schema(description = "设备id")
    private Long deviceId;
}
