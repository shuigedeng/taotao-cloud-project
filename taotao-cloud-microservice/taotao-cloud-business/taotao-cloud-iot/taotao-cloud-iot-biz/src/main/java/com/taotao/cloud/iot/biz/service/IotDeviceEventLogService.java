package com.taotao.cloud.iot.biz.service;

import com.taotao.cloud.iot.biz.entity.IotDeviceEventLogEntity;
import com.taotao.cloud.iot.biz.enums.DeviceEventTypeEnum;
import com.taotao.cloud.iot.biz.query.IotDeviceEventLogQuery;
import com.taotao.cloud.iot.biz.vo.IotDeviceEventLogVO;

import java.util.List;

/**
 * 设备事件日志
 *
 * @author 
 */
public interface IotDeviceEventLogService extends BaseService<IotDeviceEventLogEntity> {

    PageResult<IotDeviceEventLogVO> page(IotDeviceEventLogQuery query);

    void save(IotDeviceEventLogVO vo);

    void update(IotDeviceEventLogVO vo);

    void delete(List<Long> idList);

    /**
     * 创建设备事件
     *
     * @param deviceId  设备ID
     * @param tenantId  租户ID
     * @param eventType 事件类型
     * @param eventUid  事件UID
     * @param payload   事件数据
     * @return 设备事件
     */
    IotDeviceEventLogEntity createDeviceEvent(Long deviceId, Long tenantId, DeviceEventTypeEnum eventType,
                                              String eventUid, Object payload);

    /**
     * 创建设备事件并保存
     *
     * @param deviceId  设备ID
     * @param tenantId  租户ID
     * @param eventType 事件类型
     * @param eventUid  事件UID
     * @param payload   事件数据
     * @return 设备事件
     */
    void createAndSaveDeviceEvent(Long deviceId, Long tenantId, DeviceEventTypeEnum eventType,
                                  String eventUid, Object payload);
}
