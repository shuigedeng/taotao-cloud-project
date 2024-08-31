package com.taotao.cloud.iot.biz.communication.tcp;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandDTO;
import com.taotao.cloud.iot.biz.communication.dto.TcpMsgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

/**
 * TCP 网关
 *
 * @author 
 */
@Component
@Slf4j
public class TcpGateway {

    @Autowired
    private final ConcurrentMap<String, Channel> deviceChannels;

    @Autowired
    public TcpGateway(ConcurrentMap<String, Channel> deviceChannels) {
        this.deviceChannels = deviceChannels;
    }

    /**
     * 发送命令到设备
     * @param deviceId 设备ID
     * @param commandTopic 命令主题
     * @param payload 命令内容
     */
    public void sendCommandToDevice(Long deviceId, String commandTopic, String payload) {
        Channel channel = deviceChannels.get(deviceId.toString());
        if (channel != null && channel.isActive()) {
            TcpMsgDTO tcpMsgDTO = new TcpMsgDTO();
            tcpMsgDTO.setTopic(commandTopic);
            DeviceCommandDTO deviceCommandDTO = JsonUtils.parseObject(payload, DeviceCommandDTO.class);
            deviceCommandDTO.setDeviceId(deviceId.toString());
            tcpMsgDTO.setMsg(deviceCommandDTO);

            String commandJson = JsonUtils.toJsonString(tcpMsgDTO);
//            channel.writeAndFlush(commandJson);
            log.info("发送命令到设备 {}: {}", deviceId, payload);
        } else {
            throw new ServerException("设备"+deviceId+"不在线或通道无效");
        }
    }

    public void simulateDeviceReport(Long deviceId, String commandTopic, String payload, Class reportDtoclazz) {
        Channel channel = deviceChannels.get(deviceId.toString());
        if (channel != null && channel.isActive()) {
            try {
                TcpMsgDTO tcpMsgDTO = new TcpMsgDTO();
                tcpMsgDTO.setTopic(commandTopic);
                tcpMsgDTO.setMsg(JsonUtils.parseObject(payload, reportDtoclazz));
                String devicePropertyJson = JsonUtils.toJsonString(tcpMsgDTO);
                // 模拟上报，触发 channelRead 处理
                channel.pipeline().fireChannelRead(devicePropertyJson);
                log.info("模拟设备 {} 上报数据: {}", deviceId, devicePropertyJson);
            } catch (Exception e) {
                log.error("模拟设备上报数据时出现错误", e);
            }
        } else {
            throw new ServerException("设备 " + deviceId + " 不在线或通道无效");
        }
    }


}
