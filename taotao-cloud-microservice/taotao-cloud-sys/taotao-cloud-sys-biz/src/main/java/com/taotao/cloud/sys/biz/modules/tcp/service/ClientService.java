package com.taotao.cloud.sys.biz.modules.tcp.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.utils.NetUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.net.HostAndPort;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 同步发送接收
 */
@Service
@Slf4j
@ChannelHandler.Sharable
public class ClientService extends ChannelInboundHandlerAdapter {
    private Channel channel;
    private ChannelPromise channelPromise;
    private ConnectState connectState = new ConnectState();
    @Autowired
    private MessageSupport messageSupport;

    public void open(HostAndPort hostAndPort) throws InterruptedException {
        if (connectState.state){
            log.info("关闭连接:{}",HostAndPort.fromParts(connectState.getHost(), connectState.getPort()));
            try {
                close();
            }catch (Exception e){}
        }
        this.connectState.host = hostAndPort.getHost();
        this.connectState.port = hostAndPort.getPort();
        final boolean hostConnectable = NetUtil.isHostConnectable(hostAndPort.getHost(), hostAndPort.getPort());
        if (!hostConnectable){
            throw new ToolException("连接失败,无法连接");
        }
        log.info("开启新连接:{}",hostAndPort);
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ByteArrayDecoder());
                        pipeline.addLast(new ByteArrayEncoder());
                        pipeline.addLast(ClientService.this);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
        channel = channelFuture.channel();
        connectState.state = true;
    }

    public void close(){
        connectState.state = false;
        if (channel != null) {
            channel.close();
        }
    }

    public ConnectState state(String reciveModel){
        if (StringUtils.isNotBlank(reciveModel)){
            connectState.setReciveModel(reciveModel);
        }
        return connectState;
    }

    public synchronized String sendMessage(String ascii, String hex) throws DecoderException, InterruptedException {
        if (channel == null){
            throw new ToolException("未连接,不能发送指令");
        }
        final byte[] data = messageSupport.calcData(hex, ascii);

        channelPromise = channel.writeAndFlush(data).channel().newPromise();
        channelPromise.await(2, TimeUnit.SECONDS);
        return connectState.reciveData;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] data = (byte[]) msg;
        if ("hex".equals(connectState.reciveModel)) {
            connectState.reciveData = Hex.encodeHexString(data);
        }else {
            connectState.reciveData = new String(data);
        }
        channelPromise.setSuccess();
    }

    @Data
    public static class ConnectState{
        private String host;
        private int port;
        private boolean state;
        // 接收模式 ascii, hex
        private String reciveModel;
        private String reciveData;

        public ConnectState() {
        }

        public ConnectState(String host, int port, boolean state) {
            this.host = host;
            this.port = port;
            this.state = state;
        }
    }

    @PreDestroy
    public void destory(){
        log.info("清空 TCP 客户端");
        try {
            close();
        }catch (Exception e){}
    }
}
