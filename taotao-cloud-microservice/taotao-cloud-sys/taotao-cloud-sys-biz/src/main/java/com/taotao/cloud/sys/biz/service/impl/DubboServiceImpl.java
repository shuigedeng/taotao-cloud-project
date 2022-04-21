/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.service.IDubboService;
import org.springframework.stereotype.Service;

/**
 * DubboServiceImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/03/02 17:19
 */
@Service
public class DubboServiceImpl implements IDubboService {
//	@Autowired
//	private ZookeeperService zookeeperService;
//	@Autowired
//	private ClassloaderService classloaderService;
//	@Autowired
//	private ConnectService connectService;
//
//	/**
//	 * 检查是否存在 dubbo 服务
//	 *
//	 * @param connName
//	 * @return
//	 * @throws IOException
//	 */
//	public boolean checkIsDubbo(String connName) throws IOException {
//		return zookeeperService.exists(connName, "/dubbo");
//	}
//
//	/**
//	 * 这个主要从 zookeeper 上取有哪些服务 依赖于 zookeeper
//	 *
//	 * @param connName
//	 * @return
//	 */
//	public List<String> services(String connName) throws IOException {
//		List<String> childrens = zookeeperService.childrens(connName, "/dubbo");
//		return childrens;
//	}
//
//	/**
//	 * 从 zookeeper 上获取,当前服务有哪些提供者
//	 *
//	 * @param connName
//	 * @param serviceName
//	 * @return
//	 * @throws IOException
//	 */
//	public List<DubboProviderDto> providers(String connName, String serviceName)
//		throws IOException {
//		List<DubboProviderDto> dubboProviderDtos = new ArrayList<>();
//
//		List<String> childrens = zookeeperService.childrens(connName,
//			"/dubbo/" + serviceName + "/providers");
//		for (String children : childrens) {
//			String decode = URLDecoder.decode(children, StandardCharsets.UTF_8.name());
//			URL url = URL.valueOf(decode);
//			String address = url.getAddress();
//			String serviceInterface = url.getServiceInterface();
//			String methods = url.getParameter("methods");
//			String group = url.getParameter("group");
//			String version = url.getParameter("version");
//			String dubbo = url.getParameter("dubbo");
//			long timestamp = url.getParameter("timestamp", System.currentTimeMillis());
//			String application = url.getParameter("application");
//			DubboProviderDto dubboProviderDto = new DubboProviderDto(url.toString(), address);
//			dubboProviderDto.config(serviceInterface, group, version, methods, dubbo, timestamp,
//				application);
//
//			dubboProviderDtos.add(dubboProviderDto);
//		}
//
//		return dubboProviderDtos;
//	}
//
//	private String[] primitiveTypeNames = {"long"};
//
//	public Object invoke(DubboInvokeParam dubboInvokeParam)
//		throws ClassNotFoundException, NoSuchMethodException, RemotingException, ExecutionException, InterruptedException {
//		String classloaderName = dubboInvokeParam.getClassloaderName();
//		String serviceClassName = dubboInvokeParam.getServiceName();
//
//		// 解析出 class
//		ClassLoader classloader = classloaderService.getClassloader(classloaderName);
//		if (classloader == null) {
//			classloader = ClassLoader.getSystemClassLoader();
//		}
//		Class<?> clazz = classloader.loadClass(serviceClassName);
//
//		// 解析出方法
//		Method[] declaredMethods = clazz.getDeclaredMethods();
//		Method method = null;
//		for (Method declaredMethod : declaredMethods) {
//			if (declaredMethod.getName().equals(dubboInvokeParam.getMethodName())) {
//				method = declaredMethod;
//				break;
//			}
//		}
//
//		// 解析参数
//		Class<?>[] parameterTypes = method.getParameterTypes();
//		JSONArray args = dubboInvokeParam.getArgs();
//		Object[] argArray = new Object[parameterTypes.length];
//		for (int i = 0; i < parameterTypes.length; i++) {
//			Object object = args.get(i);
//			if (object instanceof JSONObject) {
//				JSONObject current = (JSONObject) object;
//				object = JSON.parseObject(current.toJSONString(), parameterTypes[i]);
//			}
//			argArray[i] = object;
//		}
//
//		// 得到要请求的提供者信息
//		String providerURL = dubboInvokeParam.getProviderURL();
//		URL provider = URL.valueOf(providerURL);
//		provider = provider.addParameter(Constants.CODEC_KEY, "dubbo");
//
//		// 请求体封装
//		HashMap<String, String> map = getAttachmentFromUrl(provider);
//		Request request = new Request();
//		request.setVersion(provider.getParameter("version"));
//		request.setTwoWay(true);
//		request.setData(new RpcInvocation(method, argArray, map));
//
//		// 请求数据
//		DoeClient client = new DoeClient(provider);
//		client.doConnect();
//		client.send(request);
//		CompletableFuture<RpcResult> future = ResponseDispatcher.getDispatcher().getFuture(request);
//		RpcResult rpcResult = future.get();
//		ResponseDispatcher.getDispatcher().removeFuture(request);
//		return rpcResult.getValue();
//	}
//
//	public static HashMap<String, String> getAttachmentFromUrl(URL url) {
//
//		String interfaceName = url.getParameter(Constants.INTERFACE_KEY, "");
//		if (StringUtils.isEmpty(interfaceName)) {
//			throw new ToolException("找不到接口名称！");
//		}
//
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put(Constants.PATH_KEY, interfaceName);
//		map.put(Constants.VERSION_KEY, url.getParameter(Constants.VERSION_KEY));
//		map.put(Constants.GROUP_KEY, url.getParameter(Constants.GROUP_KEY));
//		/**
//		 *  doesn't necessary to set these params.
//		 *
//		 map.put(Constants.SIDE_KEY, Constants.CONSUMER_SIDE);
//		 map.put(Constants.DUBBO_VERSION_KEY, Version.getVersion());
//		 map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
//		 map.put(Constants.PID_KEY, String.valueOf(ConfigUtils.getPid()));
//		 map.put(Constants.METHODS_KEY, methodNames);
//		 map.put(Constants.INTERFACE_KEY, interfaceName);
//		 map.put(Constants.VERSION_KEY, "1.0"); // 不能设置这个，不然服务端找不到invoker
//		 */
//		return map;
//	}
//
////    @PostConstruct
////    public void register(){
////        pluginManager.register(PluginDto.builder().module("call").name("dubbo").author("9420").desc("依赖 zookeeper ,在线调用 dubbo 方法").logo("dubbo.jpg").build());
////    }
//
//	public List<String> connects() {
//		final List<ConnectOutput> connectOutputs = connectService.moduleConnects(
//			ZookeeperService.module);
////        List<String> names = connectService.names(ZookeeperService.module);
//		List<String> connects = new ArrayList<>();
//		for (ConnectOutput connectOutput : connectOutputs) {
//			final String name = connectOutput.getConnectInput().getBaseName();
//			try {
//				boolean isDubbo = checkIsDubbo(name);
//				if (isDubbo) {
//					connects.add(name);
//				}
//			} catch (IOException e) {
//				LogUtil.error(e.getMessage(), e);
//			}
//		}
//		return connects;
//	}
}
