package com.taotao.cloud.iot.biz.communication.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.iot.biz.communication.dto.CommandResponseChan;
import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandDTO;
import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandResponseDTO;
import com.taotao.cloud.iot.biz.communication.dto.DevicePropertyDTO;
import com.taotao.cloud.iot.biz.communication.tcp.TcpGateway;
import com.taotao.cloud.iot.biz.dto.DeviceClientDTO;
import com.taotao.cloud.iot.biz.entity.IotDeviceEntity;
import com.taotao.cloud.iot.biz.enums.DeviceCommandEnum;
import com.taotao.cloud.iot.biz.enums.DeviceTopicEnum;
import com.taotao.cloud.iot.biz.service.IotDeviceServiceLogService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * TCP协议服务类
 *
 * @author 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TCPService implements BaseCommunication {

    private final TcpGateway tcpGateway;
    private final IotDeviceServiceLogService iotDeviceEventLogService;

    @Override
    public String asyncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload) {
        // 构建命令对象
        String commandId = StrUtil.replaceChars(UUID.randomUUID().toString(), "-", "");
        DeviceCommandDTO commandDTO = new DeviceCommandDTO();
        commandDTO.setCommand(command);
        commandDTO.setId(commandId);
        commandDTO.setDeviceId(String.valueOf(device.getId()));
        commandDTO.setPayload(payload);
        String commandTopic = DeviceTopicEnum.COMMAND.buildTopic(DeviceClientDTO.from(device));

        // 发送命令到设备命令主题
        try {
            tcpGateway.sendCommandToDevice(device.getId(),commandTopic, JSONUtil.toJsonStr(commandDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException(StrUtil.format("发送'{}'命令:{} 到设备:{}-{}, Topic:{} 失败,原因:{}",
                    command.getTitle(), commandId, device.getCode(), device.getName(), commandTopic,e.getMessage()));
        }
        log.info("发送'{}'命令:{} 到设备:{}-{}, Topic:{} 成功", command.getTitle(), commandId, device.getCode(), device.getName(), commandTopic);
        iotDeviceEventLogService.createAndSaveDeviceServiceLog(device.getId(), device.getTenantId(), command, commandId, payload);
        return commandId;
    }

    @Override
    public DeviceCommandResponseDTO syncSendCommand(IotDeviceEntity device, DeviceCommandEnum command, String payload) {
        // 构建并发送命令
        String commandId = asyncSendCommand(device, command, payload);
        // 等待返回结果
        Object receiver = CommandResponseChan.getInstance(commandId, true).get(commandId);
        if (receiver == null) {
            throw new ServerException(StrUtil.format("{}设备没有回复", device.getName()));
        }
        return (DeviceCommandResponseDTO) receiver;
    }

    @Override
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
                simulateResponseDto.setDeviceId(String.valueOf(device.getId()));
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

    @Override
    public void simulateDeviceReportAttributeData(IotDeviceEntity device,  String payload) {
        // 封装 设备属性上报的 topic
        String commandTopic = DeviceTopicEnum.PROPERTY.buildTopic(DeviceClientDTO.from(device));
        try {
            tcpGateway.simulateDeviceReport(device.getId(), commandTopic, payload, DevicePropertyDTO.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException(StrUtil.format("模拟设备:{}-{},模拟属性上报失败！ Topic:{} ",
                    device.getCode(), device.getName(), commandTopic));
        }
    }

    @Override
    public void simulateDeviceCommandResponseAttributeData(IotDeviceEntity device, String payload) {
        // 封装 设备命令执行结果的 topic
        String commandTopic = DeviceTopicEnum.COMMAND_RESPONSE.buildTopic(DeviceClientDTO.from(device));
        try {
            tcpGateway.simulateDeviceReport(device.getId(), commandTopic, payload, DeviceCommandResponseDTO.class);
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
