package com.taotao.cloud.iot.biz.service;

import com.taotao.cloud.iot.biz.entity.IotDeviceEntity;
import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandResponseDTO;
import com.taotao.cloud.iot.biz.query.IotDeviceQuery;
import com.taotao.cloud.iot.biz.communication.service.BaseCommunication;
import com.taotao.cloud.iot.biz.vo.DeviceCommandResponseAttributeDataVO;
import com.taotao.cloud.iot.biz.vo.DeviceCommandVO;
import com.taotao.cloud.iot.biz.vo.DeviceReportAttributeDataVO;
import com.taotao.cloud.iot.biz.vo.IotDeviceVO;

import java.util.List;

/**
 * 设备表
 *
 * @author 
 */
public interface IotDeviceService extends BaseService<IotDeviceEntity> {

    PageResult<IotDeviceVO> page(IotDeviceQuery query);

    void save(IotDeviceVO vo);

    void update(IotDeviceVO vo);

    void delete(List<Long> idList);

    /**
     * 根据设备的协议类型获取发送服务
     * @param device 设备
     * @return
     */
    BaseCommunication getSendService(IotDeviceEntity device);

    /**
     * 根据协议类型获取发送服务
     * @param protocolType
     * @return
     */
    BaseCommunication getSendService(String protocolType);

    /**
     * 根据设备ID获取发送服务
     * @param deviceId
     * @return
     */
    BaseCommunication getSendService(Long deviceId);

    /**
     * 对设备下发指令-同步响应模式
     *
     * @param vo
     */
    DeviceCommandResponseDTO syncSendCommand(DeviceCommandVO vo);

    /**
     * 对设备下发指令-同步响应模式-调试
     *
     * @param vo
     */
    DeviceCommandResponseDTO syncSendCommandDebug(DeviceCommandVO vo);

    /**
     * 对设备下发指令-异步响应模式
     *
     * @param vo
     */
    void asyncSendCommand(DeviceCommandVO vo);

    /**
     * 模拟设备属性数据上报
     */
    void simulateDeviceReportAttributeData(DeviceReportAttributeDataVO vo);

    /**
     * 模拟设备服务指令响应数据
     */
    void simulateDeviceCommandResponseAttributeData(DeviceCommandResponseAttributeDataVO vo);
}
