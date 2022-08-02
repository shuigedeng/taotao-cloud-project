package com.taotao.cloud.sys.biz.modules.tcp.service;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import com.taotao.cloud.sys.biz.modules.core.utils.NetUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.net.HostAndPort;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ChannelHandler.Sharable
public class ServerService extends ChannelInboundHandlerAdapter  {
    private Map<ClientKey, Channel> channelMap = new ConcurrentHashMap<>();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;
    private ServerConnectState connectState;
    private Environment environment;



    public ServerService(Environment environment) {
        final String port = environment.getProperty("server.port");
        final List<String> localIPs = NetUtil.getLocalIPs();
        final String ip = localIPs.get(0);
        HostAndPort server = HostAndPort.fromParts(ip, NumberUtils.toInt(port));
        connectState = new ServerConnectState(server);
    }

    @Autowired
    private MessageSupport messageSupport;

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        final InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        final HostAndPort hostAndPort = HostAndPort.fromParts(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());

        channelMap.put(new ClientKey(hostAndPort,new Date()),channel);

        Map<String,Object> message = new HashMap<>();
        message.put("client",hostAndPort);
        message.put("type","addclient");
        final String strMessage = JSON.toJSONString(message);

        webSocketService.sendMessage(strMessage);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        final InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        final HostAndPort hostAndPort = HostAndPort.fromParts(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());

        channelMap.remove(new ClientKey(hostAndPort,new Date()));

        Map<String,Object> message = new HashMap<>();
        message.put("client",hostAndPort);
        message.put("type","delclient");
        final String strMessage = JSON.toJSONString(message);

        webSocketService.sendMessage(strMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte [] data = (byte[]) msg;
        String dataString = "";
        if ("hex".equals(connectState.getReciveModel())) {
            dataString = Hex.encodeHexString(data);
        }else{
            dataString = new String(data);
        }

        final InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        final HostAndPort hostAndPort = HostAndPort.fromParts(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());

        Map<String,Object> message = new HashMap<>();
        message.put("client",hostAndPort);
        message.put("msg",dataString);
        message.put("type","data");
        final String strMessage = JSON.toJSONString(message);

        webSocketService.sendMessage(strMessage);
    }

    public void open(int port) throws SocketException, InterruptedException {
        if (channel != null){
            log.info("关闭上次打开的端口:{}, 并重新监听新端口:{}",connectState.getPort(),port);
            close();
        }
        connectState.setPort(port);

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>(){

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(ServerService.this);
            }
        })
                .option(ChannelOption.SO_BACKLOG, 10 * 1024) // 用于临时存放三次握手的请求的队列的最大长度
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024) // 写的缓冲区高水位线（64*1024=65536）
                .childOption(ChannelOption.SO_RCVBUF, 64 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 64 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        channel = b.bind(port).sync().channel();
        log.info("服务端开始监听TCP端口： " + port + " ，等待客户端（设备）连接...");
        connectState.setState(true);
    }

    public void close(){
        this.connectState.setState(false);
        this.channelMap.clear();
        if (channel != null) {
            channel.close();
        }
    }

    public ServerConnectState state(String reciveModel){
        if (StringUtils.isNotBlank(reciveModel)){
            connectState.setReciveModel(reciveModel);
        }
        return connectState;
    }

    public List<ClientKey> clients(){
        final Set<ClientKey> clientKeys = channelMap.keySet();
        final ArrayList<ClientKey> list = new ArrayList<>(clientKeys);
        Collections.sort(list);
        return list;
    }

    public void sendCommand(String ascii,String hex) throws DecoderException {
        final byte[] data = messageSupport.calcData(hex, ascii);
        final Iterator<Channel> iterator = channelMap.values().iterator();
        while (iterator.hasNext()){
            final Channel channel = iterator.next();
            try {
                channel.writeAndFlush(data);
            }catch (Exception e){
                log.error("写入客户端时异常:{}",e.getMessage(),e);
            }
        }
    }

    public void sendCommand(Set<HostAndPort> clients,String ascii,String hex) throws DecoderException {
        final byte[] data = messageSupport.calcData(hex, ascii);

        List<Channel> channels = new ArrayList<>();
        for (HostAndPort client : clients) {
            channels.add(channelMap.get(new ClientKey(client)));
        }
        for (Channel currentChannel : channels) {
            try {
                channel.writeAndFlush(data);
            }catch (Exception e){
                log.error("写入客户端时异常:{}",e.getMessage(),e);
            }
        }
    }

    @Data
    @ToString(callSuper = true)
    public final class ServerConnectState extends ClientService.ConnectState{
        private HostAndPort server;

        public ServerConnectState(HostAndPort server) {
            this.server = server;
        }

        public ServerConnectState(HostAndPort server,String host, int port, boolean state) {
            super(host, port, state);
            this.server = server;
        }
    }

    @Data
    public static final class ClientKey implements Comparable, Serializable {
        private HostAndPort hostAndPort;
        private Date connectTime;

        public ClientKey() {
        }

        public ClientKey(HostAndPort hostAndPort) {
            this.hostAndPort = hostAndPort;
        }

        public ClientKey(HostAndPort hostAndPort, Date connectTime) {
            this.hostAndPort = hostAndPort;
            this.connectTime = connectTime;
        }

        @Override
        public boolean equals(Object other){
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof ClientKey)) {
                return false;
            }
            ClientKey clientKey = (ClientKey) other;
            if (clientKey.hostAndPort == hostAndPort) {
                return true;
            }
            if (clientKey.hostAndPort == null || hostAndPort == null) {
                return false;
            }

            return hostAndPort.equals(clientKey.hostAndPort);
        }

        @Override
        public int hashCode() {
            return hostAndPort != null ? hostAndPort.hashCode() : 0 ;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof ClientKey) || o == null){
                return -1;
            }
            ClientKey other = (ClientKey) o;
            return this.connectTime.compareTo(other.connectTime);
        }
    }

    @PreDestroy
    public void destory(){
        log.info("清空 TCP 服务端");
        try {
            close();
        }catch (Exception e){}
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("tcp").name("tcpclient").author("sanri").logo("tcp.jpg").desc("客户端调试工具").envs("default").build());
//        pluginManager.register(PluginDto.builder().module("tcp").name("tcpserver").author("sanri").logo("tcp.jpg").desc("服务端调试工具").envs("default").build());
//    }
}
