package com.taotao.cloud.iot.biz.communication.tcp.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import net.maku.framework.common.utils.JsonUtils;
import net.maku.iot.communication.dto.DevicePropertyDTO;
import net.maku.iot.communication.dto.TcpMsgDTO;
import net.maku.iot.dto.DeviceClientDTO;
import net.maku.iot.entity.IotDeviceEntity;
import net.maku.iot.enums.DevicePropertyEnum;
import net.maku.iot.enums.DeviceRunningStatusEnum;
import net.maku.iot.enums.DeviceTopicEnum;
import net.maku.iot.service.IotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备模拟器配置，用于启动模拟设备，方便调试，默认启动系统所有的TCP设备
 *
 * @author LSF maku_lsf@163.com
 */
@Slf4j
@Configuration
public class DeviceEmulatorConfig {

    @Autowired
    private IotDeviceService deviceService;

    @Bean
    public Bootstrap nettyClient() {
        Bootstrap nettyClient = new Bootstrap();
        nettyClient.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)  // 设置为长连接
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60*1000);  // 设置连接超时时间
        return nettyClient;
    }

    public void configureBootstrap(Bootstrap bootstrap) {
        List<IotDeviceEntity> devices = deviceService.list(new LambdaQueryWrapper<IotDeviceEntity>().eq(IotDeviceEntity::getProtocolType, "TCP"));
        for (IotDeviceEntity device : devices) {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new SimpleChannelInboundHandler<String>() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            //模拟设备认证
                            Map authenticateMap = new HashMap();
                            authenticateMap.put("authenticate", device.getId().toString());
                            String authenticateMapJson = JsonUtils.toJsonString(authenticateMap);
                            log.info("------------------------> 发送认证信息到服务端: {}", authenticateMapJson);
                            ctx.writeAndFlush(authenticateMapJson);
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                            log.info("设备 {} 接收到服务端消息: {}", device.getId(), msg);
                            //模拟属性上报
                            if(msg.contains("authenticate passed")){
                                String commandTopic = DeviceTopicEnum.PROPERTY.buildTopic(DeviceClientDTO.from(device));

                                DevicePropertyDTO devicePropertyDTO = new DevicePropertyDTO();
                                devicePropertyDTO.setDeviceId(device.getId().toString());
                                devicePropertyDTO.setPropertyType(DevicePropertyEnum.RUNNING_STATUS);
                                devicePropertyDTO.setPayload(String.valueOf(DeviceRunningStatusEnum.ONLINE.getValue()));

                                TcpMsgDTO tcpMsgDTO = new TcpMsgDTO();
                                tcpMsgDTO.setTopic(commandTopic);
                                tcpMsgDTO.setMsg(devicePropertyDTO);

                                String runningStatusjson = JsonUtils.toJsonString(tcpMsgDTO);
                                log.info("------------------------> 设备发送上线文本：{}",runningStatusjson);
                                ctx.writeAndFlush(runningStatusjson);
                            }
                        }
                    });
                }
            });
        }
    }
}
