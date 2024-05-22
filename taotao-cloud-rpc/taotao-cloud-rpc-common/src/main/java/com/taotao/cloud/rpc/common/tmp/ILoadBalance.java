package com.taotao.cloud.rpc.common.tmp;

public class ILoadBalance<T extends IServer> {

	public <T extends IServer> T select(LoadBalanceContext<T> loadBalanceContext) {
		return null;
	}
}
