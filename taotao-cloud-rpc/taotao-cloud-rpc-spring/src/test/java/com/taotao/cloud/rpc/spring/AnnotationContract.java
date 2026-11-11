package com.taotao.cloud.rpc.spring;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 测试期工具：向 OS 要一个当前空闲的 TCP 端口。
 * <p>
 * 本仓库多个测试批次会并发跑，任何硬编码端口都会互撞，所以统一走这里。
 */
public final class AnnotationContract {

    private AnnotationContract() {
    }

    /**
     * 取一个刚被释放、大概率空闲的端口。
     * 注意这是"探测后关闭"的模式，理论上存在竞态；本套件里用它构造的对象都不会真的 bind，
     * 只有 {@code RpcServer} 的构造需要端口号，{@code start()} 才真正占用。
     */
    public static int freePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("拿不到空闲端口", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    // 探测用 socket，关不上不影响端口号本身
                }
            }
        }
    }
}
