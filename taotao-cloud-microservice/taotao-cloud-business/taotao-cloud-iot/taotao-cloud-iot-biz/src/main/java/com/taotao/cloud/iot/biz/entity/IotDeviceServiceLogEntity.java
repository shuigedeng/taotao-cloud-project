package com.taotao.cloud.iot.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备服务日志
 *
 * @author 
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("iot_device_service_log")
public class IotDeviceServiceLogEntity extends BaseEntity {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 服务标识id
     */
    private String serviceUid;

    /**
     * 服务数据
     */
    private String servicePayload;

    /**
     * 服务时间
     */
    private LocalDateTime serviceTime;

    /**
     * 租户ID
     */
    private Long tenantId;


}
