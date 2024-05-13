package com.taotao.cloud.tx.server.nettyTxManager;

// 事务管理者的启动类
public class Main {
    public static void main(String[] args){
        // 这个是自定义的一个服务端
        NettyServer nettyServer = new NettyServer();
        // 为其绑定IP和端口号
        nettyServer.start("localhost", 8080);
        System.out.println("\n>>>>>>事务管理者启动成功<<<<<<\n");
    }
}
