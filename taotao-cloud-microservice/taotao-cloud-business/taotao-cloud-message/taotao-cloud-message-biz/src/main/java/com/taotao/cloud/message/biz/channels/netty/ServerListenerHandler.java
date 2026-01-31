package com.taotao.cloud.message.biz.channels.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * ServerListenerHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Component
@ChannelHandler.Sharable

public class ServerListenerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(ServerBoot.class);

    static {
        //先初始化出来
        UserConnectPool.getChannelMap();
        UserConnectPool.getChannelGroup();
    }

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, TextWebSocketFrame msg ) throws Exception {
        String content = msg.text();
        /**获取客户端传过来的消息*/
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        assert dataContent != null;
        Integer action = dataContent.getAction();
        Channel channel = ctx.channel();
        /**
         * 根据消息类型对其进行处理，我们这里只做两个事情
         * 1. 注册用户
         * 2. 心跳在线
         * */
        if (Objects.equals(action, MessageActionEnum.CONNECT.type)) {
            /**
             * 2.1 当websocket 第一次 open 的时候，
             * 初始化channel，把用的 channel 和 userid 关联起来
             * */
            String userid = dataContent.getUserid();
            AttributeKey<String> key = AttributeKey.valueOf("userId");
            ctx.channel().attr(key).setIfAbsent(userid);
            UserConnectPool.getChannelMap().put(userid, channel);
            UserConnectPool.output();

        } else if (Objects.equals(action, MessageActionEnum.KEEPALIVE.type)) {
            /**
             * 心跳包的处理
             * */

            LogUtils.info("收到来自channel 为[" + channel + "]的心跳包" + dataContent);
            channel.writeAndFlush(
                    new TextWebSocketFrame(
                            JsonUtils.objectToJson(R.ok("返回心跳包").
                                    put("type", MessageActionEnum.KEEPALIVE.type))
                    )
            );
            LogUtils.info("已返回消息");

        }

    }

    @Override
    public void handlerAdded( ChannelHandlerContext ctx ) throws Exception {
        //接收到请求
        log.info("有新的客户端链接：[{}]", ctx.channel().id().asLongText());
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        ctx.channel().attr(key).setIfAbsent("temp");
        UserConnectPool.getChannelGroup().add(ctx.channel());
    }

    @Override
    public void handlerRemoved( ChannelHandlerContext ctx ) throws Exception {
        String chanelId = ctx.channel().id().asShortText();
        log.info("客户端被移除：channel id 为：" + chanelId);
        removeUserId(ctx);
        UserConnectPool.getChannelGroup().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        causLogUtils.error(e);
        //发生了异常后关闭连接，同时从channelgroup移除
        ctx.channel().close();
        removeUserId(ctx);
        UserConnectPool.getChannelGroup().remove(ctx.channel());

    }

    /**
     * 删除用户与channel的对应关系
     */
    private void removeUserId( ChannelHandlerContext ctx ) {
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(key).get();
        UserConnectPool.getChannelMap().remove(userId);
    }
}
