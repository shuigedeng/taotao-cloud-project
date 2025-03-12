package com.taotao.cloud.iot.biz.dto;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import com.taotao.cloud.iot.biz.entity.IotDeviceEntity;
import com.taotao.cloud.iot.biz.enums.DeviceTypeEnum;

import java.util.List;

/**
 * 设备客户端信息
 *
 * @author 
 */
@Data
@Accessors(chain=true)
public class DeviceClientDTO {
    /**
     * 格式 ： deviceType_tenantId_deviceId_uid
     */
    private static final String CLIENT_ID_TEMPLATE = "{}_{}_{}_{}";
    private static final String INVALID_CLIENT_ID = "无效的设备clientId:{}";
    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备唯一标识
     */
    private String uid;

    /**
     * 生成clientId
     *
     * @return
     */
    public String buildClientId() {
        if (StrUtil.isBlank(uid)) {
            return null;
        }
        return StrUtil.format(CLIENT_ID_TEMPLATE, deviceType, tenantId, deviceId, uid);
    }

    /**
     * 从clientId解析设备client信息
     *
     * @param clientId
     * @return
     */
    public static DeviceClientDTO parse(String clientId) {
        List<String> clientIdParts = StrUtil.split(clientId, "_");
        Assert.isTrue(clientIdParts.size() > 3, INVALID_CLIENT_ID, clientId);
        return DeviceClientDTO.builder()
                .deviceType(clientIdParts.get(0))
                .tenantId(Long.valueOf(clientIdParts.get(1)))
                .deviceId(Long.valueOf(clientIdParts.get(2)))
                .uid(clientIdParts.get(3))
                .build();
    }

    /**
     * 从Device创建deviceClient
     *
     * @param device
     * @return
     */
    public static DeviceClientDTO from(IotDeviceEntity device) {
        return DeviceClientDTO.builder()
                .deviceType(DeviceTypeEnum.parse(device.getType().toString()).name().toLowerCase())
                .tenantId(device.getTenantId() == null ? 0 : device.getTenantId())
                .deviceId(device.getId())
                .uid(device.getUid())
                .build();
    }
}
