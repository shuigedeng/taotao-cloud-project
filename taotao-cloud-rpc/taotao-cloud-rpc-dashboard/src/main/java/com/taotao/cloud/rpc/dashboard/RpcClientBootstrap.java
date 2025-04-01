package com.taotao.cloud.rpc.dashboard;

import com.taotao.cloud.rpc.client.RpcClient;

public class RpcClientBootstrap {

	public static void main(String[] args) {
		new RpcClient().start();
	}
}
