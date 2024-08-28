package com.taotao.cloud.iot.biz.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.framework.common.exception.ServerException;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.iot.convert.IotDeviceConvert;
import net.maku.iot.dao.IotDeviceDao;
import net.maku.iot.entity.IotDeviceEntity;
import net.maku.iot.enums.*;
import net.maku.iot.communication.dto.DeviceCommandResponseDTO;
import net.maku.iot.communication.dto.DevicePropertyDTO;
import net.maku.iot.communication.mqtt.handler.DeviceCommandResponseHandler;
import net.maku.iot.communication.mqtt.handler.DevicePropertyChangeHandler;
import net.maku.iot.query.IotDeviceQuery;
import net.maku.iot.communication.service.BaseCommunication;
import net.maku.iot.communication.service.CommunicationServiceFactory;
import net.maku.iot.service.IotDeviceEventLogService;
import net.maku.iot.service.IotDeviceService;
import net.maku.iot.vo.DeviceCommandResponseAttributeDataVO;
import net.maku.iot.vo.DeviceCommandVO;
import net.maku.iot.vo.DeviceReportAttributeDataVO;
import net.maku.iot.vo.IotDeviceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备服务类
 *
 * @author LSF maku_lsf@163.com
 */
@Service
@Slf4j
@AllArgsConstructor
public class IotDeviceServiceImpl extends BaseServiceImpl<IotDeviceDao, IotDeviceEntity>
        implements IotDeviceService, DevicePropertyChangeHandler, DeviceCommandResponseHandler {

    private final CommunicationServiceFactory communicationService;
    private final IotDeviceEventLogService deviceEventLogService;

    @Override
    public PageResult<IotDeviceVO> page(IotDeviceQuery query) {
        IPage<IotDeviceEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(IotDeviceConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<IotDeviceEntity> getWrapper(IotDeviceQuery query) {
        LambdaQueryWrapper<IotDeviceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(query.getStatus() != null, IotDeviceEntity::getStatus, query.getStatus());
        wrapper.eq(query.getType() != null, IotDeviceEntity::getType, query.getType());
        wrapper.eq(query.getRunningStatus() != null, IotDeviceEntity::getRunningStatus, query.getRunningStatus());
        return wrapper;
    }

    @Override
    public void save(IotDeviceVO vo) {
        IotDeviceEntity entity = IotDeviceConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(IotDeviceVO vo) {
        IotDeviceEntity entity = IotDeviceConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public BaseCommunication getSendService(IotDeviceEntity device) {
        if (device != null) {
            return getSendService(device.getProtocolType());
        }
        return null;
    }

    @Override
    public BaseCommunication getSendService(String protocolType) {
        return communicationService.getProtocol(protocolType);
    }

    @Override
    public BaseCommunication getSendService(Long deviceId) {
        IotDeviceEntity device = getById(deviceId);
        if (device != null) {
            return getSendService(device.getProtocolType());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceCommandResponseDTO syncSendCommand(DeviceCommandVO vo) {

        IotDeviceEntity device = getById(vo.getDeviceId());
        Assert.notNull(device, "未注册的设备:{}", vo.getDeviceId());

        DeviceCommandEnum commandEnum = DeviceCommandEnum.parse(vo.getCommand());
        try {
            return getSendService(device).syncSendCommand(getById(vo.getDeviceId()), commandEnum, vo.getPayload());
        } catch (ServerException e) {
            if (DeviceCommandEnum.parse(vo.getCommand()).getEventType() != null
                    && StrUtil.contains(e.getMessage(), DeviceServiceEnum.COMMAND_ID.getValue())) {
                //指令异常事件记录
                String commandId = e.getMessage().substring(e.getMessage().indexOf("<") + 1, e.getMessage().indexOf(">"));
                deviceEventLogService.createAndSaveDeviceEvent(
                        vo.getDeviceId(), device.getTenantId(),
                        commandEnum.getEventType(), commandId, vo.getPayload());
            }
            throw e;
        }
    }

    @Override
    public DeviceCommandResponseDTO syncSendCommandDebug(DeviceCommandVO vo) {
        IotDeviceEntity device = getById(vo.getDeviceId());
        DeviceCommandEnum commandEnum = DeviceCommandEnum.parse(vo.getCommand());
        return getSendService(device).syncSendCommandDebug(device, commandEnum, vo.getPayload());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncSendCommand(DeviceCommandVO vo) {
        IotDeviceEntity device = getById(vo.getDeviceId());
        getSendService(device).asyncSendCommand(device, DeviceCommandEnum.parse(vo.getCommand()), vo.getPayload());
    }

    @Override
    public void simulateDeviceReportAttributeData(DeviceReportAttributeDataVO vo) {
        IotDeviceEntity device = getById(vo.getDeviceId());
        vo.setDeviceId(vo.getDeviceId());
        getSendService(device).simulateDeviceReportAttributeData(device, JSONUtil.toJsonStr(vo));
    }

    @Override
    public void simulateDeviceCommandResponseAttributeData(DeviceCommandResponseAttributeDataVO vo) {
        IotDeviceEntity device = getById(vo.getDeviceId());
        vo.setDeviceId(vo.getDeviceId());
        getSendService(device).simulateDeviceCommandResponseAttributeData(device, JSONUtil.toJsonStr(vo));
    }

    /**
     * 设备状态上报处理
     *
     * @param topic
     * @param deviceProperty
     */
    @Override
    public void handle(String topic, DevicePropertyDTO deviceProperty) {
        DeviceTopicEnum.DeviceTopicContext topicContext = null;
        try {
            topicContext = DeviceTopicEnum.parseContext(topic);
        } catch (Exception e) {
            log.warn("无效设备主题:{}，忽略设备状态上报消息:{}", topic, deviceProperty);
            return;
        }
        if (log.isDebugEnabled()) {
            log.warn("处理设备状态上报消息，Topic:{}, TopicContext:{}", topic, topicContext);
        }
        Long deviceId = topicContext.getClient().getDeviceId();
        IotDeviceEntity device = super.getById(deviceId);
        if (device == null) {
            log.warn("无效设备id:{}，忽略设备状态上报消息:{}", deviceId, deviceProperty);
            return;
        }
        switch (deviceProperty.getPropertyType()) {
            case RUNNING_STATUS:
                handleRunningStatus(device, deviceProperty, topicContext);
                break;
            case APP_VERSION:
                handleAppVersion(device, deviceProperty, topicContext);
                break;
            case BATTERY_PERCENT:
                handleBatteryPercent(device, deviceProperty, topicContext);
                break;
            case TEMPERATURE:
                handleTemperature(device, deviceProperty, topicContext);
                break;
            default:
                log.warn("未知设备属性类型:{}，忽略设备状态上报消息:{}", deviceProperty.getPropertyType(), deviceProperty);
                break;
        }
    }

    private void handleRunningStatus(IotDeviceEntity device, DevicePropertyDTO deviceProperty, DeviceTopicEnum.DeviceTopicContext topicContext) {
        DeviceRunningStatusEnum oldStatus = DeviceRunningStatusEnum.parse(device.getRunningStatus().toString());
        DeviceRunningStatusEnum newStatus = DeviceRunningStatusEnum.parse(deviceProperty.getPayload());

        device.setRunningStatus(newStatus.getValue());
        if (DeviceRunningStatusEnum.ONLINE.equals(newStatus)) {
            device.setUpTime(LocalDateTime.now());
        } else if (DeviceRunningStatusEnum.OFFLINE.equals(newStatus)) {
            device.setDownTime(LocalDateTime.now());
        }
        device.setUpdateTime(LocalDateTime.now());
        getBaseMapper().updateById(device);

        if (newStatus.getEventType() != null) {
            deviceEventLogService.createAndSaveDeviceEvent(device.getId(), topicContext.getClient().getTenantId(),
                    newStatus.getEventType(), null, StrUtil.format("设备上报运行状态 ： {}", newStatus.getTitle()));
        }

        if (log.isInfoEnabled()) {
            log.info("租户：{}，设备：{}-{}，运行状态：{}-{} -> {}-{}", topicContext.getClient().getTenantId(), device.getId(), device.getName(), oldStatus.getValue(), oldStatus.getTitle(), newStatus.getValue(), newStatus.getTitle());
        }
    }

    private void handleAppVersion(IotDeviceEntity device, DevicePropertyDTO deviceStatus, DeviceTopicEnum.DeviceTopicContext topicContext) {
        if (CharSequenceUtil.equals(device.getAppVersion(), deviceStatus.getPayload())) {
            return;
        }
        device.setAppVersion(deviceStatus.getPayload());
        device.setUpdateTime(LocalDateTime.now());
        getBaseMapper().updateById(device);

        deviceEventLogService.createAndSaveDeviceEvent(device.getId(), topicContext.getClient().getTenantId(),
                DeviceEventTypeEnum.APP_VERSION_REPORT, null,
                StrUtil.format(" {}:{}", DeviceEventTypeEnum.APP_VERSION_REPORT.getTitle(), deviceStatus.getPayload()));

        if (log.isInfoEnabled()) {
            log.info("租户：{}，设备：{}-{}，App版本：{} -> {}", topicContext.getClient().getTenantId(), device.getId(), device.getName(), device.getAppVersion(), deviceStatus.getPayload());
        }
    }

    private void handleBatteryPercent(IotDeviceEntity device, DevicePropertyDTO deviceStatus, DeviceTopicEnum.DeviceTopicContext topicContext) {
        String batteryPercent = deviceStatus.getPayload();
        if (batteryPercent.equals(device.getBatteryPercent())) {
            return;
        }
        device.setBatteryPercent(batteryPercent);
        device.setUpdateTime(LocalDateTime.now());
        getBaseMapper().updateById(device);

        deviceEventLogService.createAndSaveDeviceEvent(device.getId(), topicContext.getClient().getTenantId(),
                DeviceEventTypeEnum.BATTERY_PERCENT_REPORT, null,
                StrUtil.format(" {}:{}", DeviceEventTypeEnum.BATTERY_PERCENT_REPORT.getTitle(), deviceStatus.getPayload()));

        if (log.isInfoEnabled()) {
            log.info("租户：{}，设备：{}-{}，电池电量百分比：{} -> {}", topicContext.getClient().getTenantId(), device.getId(), device.getName(), device.getBatteryPercent(), batteryPercent);
        }
    }

    private void handleTemperature(IotDeviceEntity device, DevicePropertyDTO deviceStatus, DeviceTopicEnum.DeviceTopicContext topicContext) {
        String temperature = deviceStatus.getPayload();
        if (temperature.equals(device.getTemperature())) {
            return;
        }
        device.setTemperature(temperature);
        device.setUpdateTime(LocalDateTime.now());
        getBaseMapper().updateById(device);

        deviceEventLogService.createAndSaveDeviceEvent(device.getId(), topicContext.getClient().getTenantId(),
                DeviceEventTypeEnum.TEMPERATURE_REPORT, null,
                StrUtil.format(" {}:{}", DeviceEventTypeEnum.TEMPERATURE_REPORT.getTitle(), deviceStatus.getPayload()));

        if (log.isInfoEnabled()) {
            log.info("租户：{}，设备：{}-{}，温度：{} -> {}", topicContext.getClient().getTenantId(), device.getId(), device.getName(), device.getTemperature(), temperature);
        }
    }

    /**
     * 设备命令响应处理
     *
     * @param topic
     * @param commandResponse
     */
    @Override
    public void handle(String topic, DeviceCommandResponseDTO commandResponse) {
        DeviceTopicEnum.DeviceTopicContext topicContext = null;
        try {
            topicContext = DeviceTopicEnum.parseContext(topic);
        } catch (Exception e) {
            log.warn("无效设备主题:{}，忽略设备命令响应消息:{}", topic, commandResponse);
            return;
        }
        if (log.isDebugEnabled()) {
            log.warn("处理设备设备命令响应消息，Topic:{}, TopicContext:{}", topic, topicContext);
        }

        Long deviceId = topicContext.getClient().getDeviceId();
        IotDeviceEntity device = super.getById(deviceId);
        if (device == null) {
            log.warn("无效设备id:{}，忽略设备命令响应消息:{}", deviceId, commandResponse);
            return;
        }
        switch (commandResponse.getCommand()) {
            case LOCK, UNLOCK, OTA_UPGRADE: {
                //记录处理设备事件
                deviceEventLogService.createAndSaveDeviceEvent(device.getId(),
                        topicContext.getClient().getTenantId(),
                        commandResponse.getCommand().getEventType(),
                        commandResponse.getCommandId(), commandResponse.getResponsePayload());
                break;
            }
            default:
                log.warn("未知设备命令类型:{}，忽略设备命令响应消息:{}", commandResponse.getCommand(), commandResponse);
                break;

        }


    }
}
