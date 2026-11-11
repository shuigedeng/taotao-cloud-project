package com.taotao.cloud.rpc.remoting;

import com.taotao.cloud.rpc.remoting.RpcClient;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC 客户端 Holder
 * 缓存 (host, port) -> RpcClient 实例，避免重复连接
 * 提供统一的 invoke(Invocation, URL) 接口
 */
public class RpcClientHolder {

    private static final Logger log = LogManager.getLogger(RpcClientHolder.class);

    private static final Map<String, RpcClient> CLIENTS = new ConcurrentHashMap<>();

    private RpcClientHolder() {}

    public static RpcClient get(String host, int port) {
        String key = host + ":" + port;
        return CLIENTS.computeIfAbsent(key, k -> new RpcClient(host, port));
    }

    /**
     * 通过 Netty 客户端发起一次远程调用
     */
    public static Result invoke(Invocation invocation, URL url) {
        String host = url.getHost();
        int port = url.getPort();
        RpcClient client = get(host, port);
        return client.invoke(invocation, url);
    }
}
