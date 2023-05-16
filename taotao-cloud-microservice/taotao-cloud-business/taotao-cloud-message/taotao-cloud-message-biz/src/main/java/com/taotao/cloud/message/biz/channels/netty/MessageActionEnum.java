package com.taotao.cloud.message.biz.channels.netty;

public enum MessageActionEnum {

    //定义消息类型

    CONNECT(1,"第一次（或重连）初始化连接"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    KEEPALIVE(4,"客户端保持心跳"),
    PULL_FRIEND(5, "拉取好友"),
    HOLEADUITMSG(6,"审核消息");


    public final Integer type;
    public final String content;
    MessageActionEnum(Integer type,String content) {
        this.type = type;
        this.content = content;
    }

}
