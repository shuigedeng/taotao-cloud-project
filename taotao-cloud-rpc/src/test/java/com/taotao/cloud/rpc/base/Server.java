package com.taotao.cloud.rpc.base;


import com.taotao.cloud.rpc.common.annotation.ServiceScan;
import com.taotao.cloud.rpc.common.enums.SerializerCode;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.core.net.netty.server.NettyServer;

@ServiceScan
public class Server {

	public static void main(String[] args) {
		try {
			NettyServer nettyServer = new NettyServer("192.168.10.1", 8081,
				SerializerCode.KRYO.getCode());
			nettyServer.start();
		} catch (RpcException e) {
			e.printStackTrace();
		}
	}
}
