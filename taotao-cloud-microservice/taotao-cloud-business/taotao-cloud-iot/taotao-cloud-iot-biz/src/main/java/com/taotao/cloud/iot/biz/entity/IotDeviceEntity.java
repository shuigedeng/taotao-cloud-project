package com.taotao.cloud.iot.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备表
 *
 * @author 
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("iot_device")
public class IotDeviceEntity extends BaseEntity {

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 设备类型，1.手持设备，2.柜体，3传感设备
     */
    private Integer type;

    /**
     * 设备和服务器通信协议类型
     */
    private String protocolType;

    /**
     * 唯一标识码
     */
    private String uid;

    /**
     * 设备密钥
     */
    private String secret;

    /**
     * App版本号
     */
    private String appVersion;

    /**
     * 电池电量百分比
     */
    private String batteryPercent;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 状态，0禁用，1正常
     */
    private Integer status;

    /**
     * 运行状态，0.离线状态 1.在线状态 2.正常待机 3.用户使用中 4.OTA升级中
     */
    private Integer runningStatus;

    /**
     * 上线时间
     */
    private LocalDateTime upTime;

    /**
     * 下线时间
     */
    private LocalDateTime downTime;

    /**
     * 租户ID
     */
    private Long tenantId;


}
