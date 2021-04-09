package com.taotao.cloud.java.concurrent.nio;

import java.io.IOException;

public class TimeServer {

    /**
     * @param args
     * @throws IOException
     * @author blackcoder
     */
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length < 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
