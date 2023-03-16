package com.taotao.cloud.rpc.base;


import com.taotao.cloud.rpc.common.protocol.RpcRequest;

public class Test {

	public static void main(String[] args) {

		RpcRequest rpcRequest = new RpcRequest();
		rpcRequest.setInterfaceName("userService");
		System.out.println(rpcRequest.getInterfaceName());
		System.out.println(rpcRequest.getGroup());

	}
}
