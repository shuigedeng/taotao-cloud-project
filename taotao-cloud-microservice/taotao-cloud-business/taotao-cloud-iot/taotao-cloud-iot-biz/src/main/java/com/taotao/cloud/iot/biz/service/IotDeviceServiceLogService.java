package com.taotao.cloud.iot.biz.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.iot.entity.IotDeviceServiceLogEntity;
import net.maku.iot.enums.DeviceCommandEnum;
import net.maku.iot.query.IotDeviceServiceLogQuery;
import net.maku.iot.vo.IotDeviceServiceLogVO;

import java.util.List;

/**
 * 设备服务日志
 *
 * @author LSF maku_lsf@163.com
 */
public interface IotDeviceServiceLogService extends BaseService<IotDeviceServiceLogEntity> {

    PageResult<IotDeviceServiceLogVO> page(IotDeviceServiceLogQuery query);

    void save(IotDeviceServiceLogVO vo);

    void update(IotDeviceServiceLogVO vo);

    void delete(List<Long> idList);

    /**
     * 创建设备服务日志
     *
     * @param deviceId 设备ID
     * @param tenantId 租户ID
     * @param command  服务类型
     * @param eventUid 事件UID
     * @param payload  事件数据
     * @return 设备事件
     */
    IotDeviceServiceLogEntity createDeviceServiceLog(Long deviceId, Long tenantId, DeviceCommandEnum command,
                                                     String eventUid, Object payload);

    /**
     * 创建设备服务日志并保存
     *
     * @param deviceId 设备ID
     * @param tenantId 租户ID
     * @param command  服务类型
     * @param eventUid 事件UID
     * @param payload  事件数据
     * @return 设备事件
     */
    void createAndSaveDeviceServiceLog(Long deviceId, Long tenantId, DeviceCommandEnum command,
                                       String eventUid, Object payload);
}
