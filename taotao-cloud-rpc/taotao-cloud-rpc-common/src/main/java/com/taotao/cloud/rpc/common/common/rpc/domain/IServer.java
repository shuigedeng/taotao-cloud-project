package com.taotao.cloud.rpc.common.common.rpc.domain;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.util.IpUtils;

public interface IServer {
	public String url() ;

	public int weight();
}
