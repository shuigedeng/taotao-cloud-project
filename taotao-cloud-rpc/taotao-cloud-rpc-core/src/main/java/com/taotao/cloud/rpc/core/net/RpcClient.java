package com.taotao.cloud.rpc.core.net;


import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;

public interface RpcClient {

	Object sendRequest(RpcRequest rpcRequest) throws RpcException;
}
