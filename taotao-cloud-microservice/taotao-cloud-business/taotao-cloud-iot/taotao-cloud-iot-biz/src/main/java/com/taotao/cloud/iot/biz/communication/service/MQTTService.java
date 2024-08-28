package com.taotao.cloud.iot.biz.communication.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.framework.common.exception.ServerException;
import net.maku.iot.communication.dto.CommandResponseChan;
import net.maku.iot.communication.dto.DeviceCommandDTO;
import net.maku.iot.communication.dto.DeviceCommandResponseDTO;
import net.maku.iot.communication.mqtt.MqttGateway;
import net.maku.iot.dto.DeviceClientDTO;
import net.maku.iot.entity.IotDeviceEntity;
import net.maku.iot.enums.DeviceCommandEnum;
import net.maku.iot.enums.DeviceTopicEnum;
import net.maku.iot.service.IotDeviceServiceLogService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * MQTT协议服务类
 *
 * @author LSF maku_lsf@163.com
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MQTTService implements BaseCommunication {

    private final MqttGateway mqttGateway;
    private final IotDeviceServiceLogService iotDeviceEventLogService;

    /**
     * 异步发送命令，返回命令id
     *
     * @param device
     * @param command
     * @param payload
     * @return
     */
    @Override
    public String asyncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload) {
        return asyncSendCommand(device, command, payload, Boolean.FALSE);
    }

    /**
     * 异步发送命令，返回命令id
     *
     * @param device
     * @param command
     * @param payload
     * @param retained
     * @return
     */
    public String asyncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload, boolean retained) {
        // 构建命令对象
        String commandId = StrUtil.replaceChars(UUID.randomUUID().toString(), "-", "");
        DeviceCommandDTO commandDTO = new DeviceCommandDTO();
        commandDTO.setCommand(command);
        commandDTO.setId(commandId);
        commandDTO.setPayload(payload);
        String commandTopic = DeviceTopicEnum.COMMAND.buildTopic(DeviceClientDTO.from(device));

        // 发送命令到设备命令主题
        try {
            mqttGateway.sendToMqtt(commandTopic, retained, JSONUtil.toJsonStr(commandDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException(StrUtil.format("发送'{}'命令:{} 到设备:{}-{}, Topic:{} 失败",
                    command.getTitle(), commandId, device.getCode(), device.getName(), commandTopic));
        }
        log.info("发送'{}'命令:{} 到设备:{}-{}, Topic:{} 成功", command.getTitle(), commandId, device.getCode(), device.getName(), commandTopic);
        iotDeviceEventLogService.createAndSaveDeviceServiceLog(device.getId(), device.getTenantId(), command, commandId, payload);
        return commandId;
    }

    /**
     * 同步发送命令并返回响应结果
     *
     * @param device
     * @param command
     * @param payload
     * @return
     */
    public DeviceCommandResponseDTO syncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload) {
        return syncSendCommand(device, command, payload, Boolean.FALSE);
    }

    /**
     * 发送命令并返回响应结果
     *
     * @param device
     * @param command
     * @param payload
     * @param retained
     * @return
     */
    public DeviceCommandResponseDTO syncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload, boolean retained) {
        // 构建并发送命令
        String commandId = asyncSendCommand(device, command, payload, retained);
        // 等待返回结果
        Object receiver = CommandResponseChan.getInstance(commandId, true).get(commandId);
        if (receiver == null) {
            throw new ServerException(StrUtil.format("{}设备没有回复", device.getName()));
        }
        return (DeviceCommandResponseDTO) receiver;
    }

    /**
     * 发送命令并返回响应结果，模拟设备响应
     *
     * @param device
     * @param command
     * @param payload
     * @return
     */
    public DeviceCommandResponseDTO syncSendCommandDebug(IotDeviceEntity device, DeviceCommandEnum command, String payload) {
        // 构建并发送命令
        String commandId = asyncSendCommand(device, command, payload);

        // 2秒后模拟设备响应
        new Thread(() -> {
            try {
                //模拟设备正常响应
                Thread.sleep(2000);
                //模拟设备超时响应
                //Thread.sleep(15000);
                DeviceCommandResponseDTO simulateResponseDto = new DeviceCommandResponseDTO();
                simulateResponseDto.setCommandId(commandId);
                simulateResponseDto.setResponsePayload(command.getTitle() + ",设备执行成功！");
                simulateResponseDto.setCommand(command);
                simulateDeviceCommandResponseAttributeData(device, JSONUtil.toJsonStr(simulateResponseDto));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("模拟设备响应线程被中断", e);
            }
        }).start();

        // 等待返回结果
        Object receiver = CommandResponseChan.getInstance(commandId, true).get(commandId);
        if (receiver == null) {
            throw new ServerException(StrUtil.format("{}设备没有回复", device.getName()));
        }
        return (DeviceCommandResponseDTO) receiver;
    }

    /**
     * 模拟设备属性上报
     */
    @Override
    public void simulateDeviceReportAttributeData(IotDeviceEntity device, String payload) {
        // 封装 设备属性上报的 topic
        String commandTopic = DeviceTopicEnum.PROPERTY.buildTopic(DeviceClientDTO.from(device));
        try {
            mqttGateway.sendToMqtt(commandTopic, payload);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException(StrUtil.format("模拟设备:{}-{},模拟属性上报失败！ Topic:{} ",
                    device.getCode(), device.getName(), commandTopic));
        }
    }


    /**
     * 模拟设备服务指令响应数据
     *
     * @param device
     * @param payload
     */
    @Override
    public void simulateDeviceCommandResponseAttributeData(IotDeviceEntity device, String payload) {
        // 封装 设备命令执行结果的 topic
        String commandTopic = DeviceTopicEnum.COMMAND_RESPONSE.buildTopic(DeviceClientDTO.from(device));
        try {
            mqttGateway.sendToMqtt(commandTopic, payload);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException(StrUtil.format("模拟设备:{}-{},模拟命令执行结果上报失败！ Topic:{} ",
                    device.getCode(), device.getName(), commandTopic));
        }
    }

    /**
     * 设备命令响应处理，把设备响应结果放入通道中
     *
     * @param commandResponse 设备命令响应
     */
    public void commandReplied(DeviceCommandResponseDTO commandResponse) {
        CommandResponseChan commandResponseChan = CommandResponseChan.getInstance(commandResponse.getCommandId(), false);
        commandResponseChan.put(commandResponse);
    }


}
