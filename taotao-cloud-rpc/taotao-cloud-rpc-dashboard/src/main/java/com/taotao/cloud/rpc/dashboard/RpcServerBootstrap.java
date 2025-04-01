package com.taotao.cloud.rpc.dashboard;

import com.taotao.cloud.rpc.server.RpcServer;

public class RpcServerBootstrap {

	public static void main(String[] args) {
		new RpcServer().start();
	}
}
