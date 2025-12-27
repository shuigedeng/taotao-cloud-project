package com.taotao.cloud.iot.biz.communication.tcp.handler;

import cn.hutool.core.util.StrUtil;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandResponseDTO;
import com.taotao.cloud.iot.biz.communication.mqtt.factory.DeviceCommandResponseHandlerFactory;
import com.taotao.cloud.iot.biz.communication.service.TCPService;
import com.taotao.cloud.iot.biz.enums.DeviceTopicEnum;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 设备命令响应处理器
 *
 * @author 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCommandResponseTCPMessageHandler implements TCPMessageHandler {

    private final DeviceCommandResponseHandlerFactory deviceCommandResponseHandlerFactory;

    private final TCPService deviceTCPService;
    @Override
    public boolean supports(String topic) {
        return DeviceTopicEnum.startsWith(topic, DeviceTopicEnum.COMMAND_RESPONSE.getTopic());
    }

    @Override
    public void handle(String topic, Object message) {
        DeviceCommandResponseDTO commandResponseDTO = parseCommandReplyMessage(topic, message);
        Optional.ofNullable(commandResponseDTO.getCommand())
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format("缺失指令类型! 主题:'{}',消息:{}", topic, message)));
        Optional.ofNullable(commandResponseDTO.getCommandId())
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format("缺失指令ID! 主题:'{}',消息:{}", topic, message)));
        Optional.ofNullable(commandResponseDTO)
                .ifPresent(responseDTO -> {
                    // 调用设备命令执行器的命令响应处理逻辑
                    try {
                        deviceTCPService.commandReplied( responseDTO);
                    } catch (Exception e) {
                        log.error(StrUtil.format("调用设备命令执行器响应处理方法出错，topic:{}, message:{}", topic, message), e);
                    }
                    // 调用自定义命令响应处理器
                    try {
                        deviceCommandResponseHandlerFactory.getHandlers().forEach(h -> h.handle(topic, responseDTO));
                    } catch (Exception e) {
                        log.error(StrUtil.format("调用设备命令响应响应处理器出错，topic:{}, message:{}", topic, message), e);
                    }
                });
    }

    private DeviceCommandResponseDTO parseCommandReplyMessage(String topic, Object message) {
        try {
            JsonMapper mapper = JsonMapper.builder().build();
            DeviceCommandResponseDTO commandResponse =  mapper.convertValue(message, DeviceCommandResponseDTO.class);
            if (StrUtil.isBlank(commandResponse.getCommandId())) {
                log.error(StrUtil.format("主题'{}'的消息,缺失指令ID", topic));
                return null;
            }
            return commandResponse;

        } catch (Exception e) {
            log.error(StrUtil.format("将主题'{}'的消息解析为设备命令响应对象失败", topic), e);
            return null;
        }
    }
}
