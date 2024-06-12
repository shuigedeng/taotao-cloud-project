package com.taotao.cloud.rpc.client;


import com.taotao.cloud.rpc.common.common.RpcReponse;
import com.taotao.cloud.rpc.common.common.RpcRequest;
import java.lang.reflect.Proxy;
import java.util.UUID;
/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class RpcProxy {

	private String serverAddress;
//    private ServiceDiscovery serviceDiscovery;

	public RpcProxy(String serverAddress) {
		this.serverAddress = serverAddress;
	}

//    public RpcProxy(ServiceDiscovery serviceDiscovery) {
//        this.serviceDiscovery = serviceDiscovery;
//    }

	/**
	 * 创建客户端代理
	 *
	 * @param interfaceClass interfaceClass
	 * @return T
	 * @author shuigedeng
	 * @date 2024.06
	 */
	public <T> T create(Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(
			interfaceClass.getClassLoader(),
			new Class<?>[]{interfaceClass},
			(proxy, method, args) -> {
				RpcRequest rpcRequest = new RpcRequest();
				rpcRequest.setRequestId(UUID.randomUUID().toString());
				rpcRequest.setClassName(method.getDeclaringClass().getName());
				rpcRequest.setMethodName(method.getName());
				rpcRequest.setParameterTypes(method.getParameterTypes());
				rpcRequest.setParameters(args);

//				if (null != serviceDiscovery) {
//					serverAddress = serviceDiscovery.discover();
//				}

				String[] array = serverAddress.split(":");
				String ip = array[0];
				int port = Integer.parseInt(array[1]);

				RpcClient client = new RpcClient(ip, port);
				RpcReponse rpcReponse = client.send(rpcRequest);

				if (rpcReponse.isError()) {
					throw rpcReponse.getErrorMsg();
				}
				else {
					return rpcReponse.getResult();
				}
			}
		);
	}
}
