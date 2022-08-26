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
package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.sys.biz.service.business.IJvmService;
import org.springframework.stereotype.Service;

/**
 * JvmServiceImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/03/03 14:43
 */
@Service
public class JvmServiceImpl implements IJvmService {
//	/**
//	 * 连接缓存
//	 * connName => MBeanServerConnection
//	 */
//	private static final Map<String, MBeanServerConnection> mBeanServerConnectionMap = new ConcurrentHashMap<>();
//
//	@Autowired
//	private ConnectService connectService;
//
//	@Autowired
//	private MBeanMonitorService mBeanMonitorService;
//
//	private static final String DIAGNOSTIC_COMMAND_MXBEAN_NAME = "com.sun.management:type=DiagnosticCommand";
//
//	/**
//	 * 获取堆数据
//	 * @param jmxConnectInfo
//	 * @param all
//	 * @return
//	 */
//	public HeapHistogramImpl gcClassHistogram(String connName, boolean all) throws IOException, MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ObjectName diagCommName = new ObjectName(DIAGNOSTIC_COMMAND_MXBEAN_NAME);
//
//		String[] signature = new String[]{String[].class.getName()};
//		Object [] params = new Object[1];
//		params[0] = all ? new String[]{"-all="} : new String[0];
//		final String histogramText = (String) mBeanServerConnection.invoke(diagCommName, "gcClassHistogram", params, signature);
//		final HeapHistogramImpl heapHistogram = new HeapHistogramImpl(histogramText);
//		Comparator<ClassInfo> comparator = (a, b) -> {
//			long temp =  b.getBytes() - a.getBytes();
//			if (temp == 0 ){
//				return (int) (b.getInstancesCount() - a.getInstancesCount() );
//			}
//			return (int) temp;
//		};
//		Collections.sort(heapHistogram.getHeapHistogram(), comparator);
//		return heapHistogram;
//	}
//
//	/**
//	 * 已经设置的 vm 参数
//	 * @param connName
//	 */
//	public List<VMParam> flagsSetted(String connName) throws IOException, MBeanException, InstanceNotFoundException, ReflectionException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		final ObjectName diagCommName = mBeanMonitorService.createBeanName(DIAGNOSTIC_COMMAND_MXBEAN_NAME);
//		String[] signature = new String[]{String[].class.getName()};
//		Object [] params = new Object[1];
//		params[0] = new String[0];
//		final String vmFlags = (String) mBeanServerConnection.invoke(diagCommName, "vmFlags", params, signature);
//
//		List<VMParam> vmParams = new ArrayList<>();
//		final String[] keyValues = StringUtils.split(vmFlags, " ");
//		for (String keyValue : keyValues) {
//			String realKeyValue = keyValue.trim();
//			if (StringUtils.isBlank(realKeyValue)){
//				continue;
//			}
//			final String[] split = StringUtils.split(realKeyValue, "=");
//			if (split.length == 2) {
//				vmParams.add(new VMParam(split[0], split[1]));
//			}else if (split.length == 1){
//				vmParams.add(new VMParam(split[0],"true"));
//			}else{
//				log.warn("vm 参数个数不正确: {}",realKeyValue);
//			}
//		}
//		return vmParams;
//	}
//
//	public void flags(String connName) throws IOException, MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ObjectName diagCommName = new ObjectName(DIAGNOSTIC_COMMAND_MXBEAN_NAME);
//
//		String[] signature = new String[]{String[].class.getName()};
//		Object [] params = new Object[1];
//		params[0] = new String[]{"-all="};
//		// 查询所有的标志
//		final String flagsText = (String) mBeanServerConnection.invoke(diagCommName, "vmFlags", params, signature);
//
//		// 查询当前配置的标志
//		params[0] = new String[0];
//		final String editFlags = (String) mBeanServerConnection.invoke(diagCommName, "vmFlags", params, signature);
//	}
//
//	/**
//	 * 将所有注册的域对象转成树状名称结构, 参考 jconsole 设计
//	 * @param jmxHostAndPort
//	 */
//	public List<NameInfo> mBeans(String connName) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(connName);
//
//		// 查询所有注册的域对象
//		Set<ObjectName> objectNames = mBeanServerConnection.queryNames(null, null);
//
//		// 使用域名做域对象列表映射 domain => List<ObjectName>
//		MultiValueMap<String, ObjectName> objectNameMultiValueMap = new LinkedMultiValueMap<>();
//		for (ObjectName objectName : objectNames) {
//			objectNameMultiValueMap.add(objectName.getDomain(), objectName);
//		}
//
//		// 遍历每一个域, 将属性转成树状结构
//		List<NameInfo> nameInfos = new ArrayList<>();
//		for (String domain : objectNameMultiValueMap.keySet()) {
//			// 根节点
//			final NameInfo root = new NameInfo(domain);
//			nameInfos.add(root);
//
//			final List<ObjectName> domainObjectNames = objectNameMultiValueMap.get(domain);
//			for (ObjectName domainObjectName : domainObjectNames) {
//				final Hashtable<String, String> keyPropertyList = domainObjectName.getKeyPropertyList();
//				final List<String> values = new ArrayList<>(keyPropertyList.values());
//				final List<String> keys = new ArrayList<>(keyPropertyList.keySet());
//				Collections.reverse(values);
//				Collections.reverse(keys);
//				appendTree(mBeanServerConnection,values,domainObjectName,root,0);
//			}
//		}
//
//		return nameInfos;
//	}
//
//	/**
//	 * 追加 values 列表到 parent 节点上去
//	 * @param values 值列表
//	 * @param domainObjectName 域对象
//	 * @param parent 父级名称
//	 * @param deep 当前深度
//	 */
//	private void appendTree(MBeanServerConnection mBeanServerConnection,List<String> values, ObjectName domainObjectName, NameInfo parent, int deep) throws IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
//		if (deep >= values.size()){
//			return ;
//		}
//		final String part = values.get(deep);
//		for (NameInfo child : parent.getChildren()) {
//			if (child.getLabel().equals(part)){
//				appendTree(mBeanServerConnection, values,domainObjectName,child,++deep);
//				return ;
//			}
//		}
//		addTree(mBeanServerConnection,values,domainObjectName,parent,deep);
//	}
//
//	private void addTree(MBeanServerConnection mBeanServerConnection,List<String> values, ObjectName domainObjectName, NameInfo parent, int deep) throws IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
//		if (deep >= values.size()){
//			return ;
//		}
//		for (int i = deep; i < values.size(); i++) {
//			final String part = values.get(i);
//			final NameInfo nameInfo = new NameInfo(part);
//			nameInfo.setRef(domainObjectName);
//			parent.getChildren().add(nameInfo);
//			parent = nameInfo;
//		}
//
//		parent.setNodeType(NameInfo.NodeType.MBEAN);
//
////        // 创建 mbean 的属性,操作,通知信息
////        final MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(domainObjectName);
////
////        // 属性
////        final MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
////        if (attributes != null && attributes.length > 0){
////            final NameInfo attributeNameInfo = new NameInfo("属性");
////            attributeNameInfo.setNodeType(NameInfo.NodeType.PROPERTY_FOLDER);
////            parent.getChildren().add(attributeNameInfo);
////
////            for (MBeanAttributeInfo attribute : attributes) {
////                final NameInfo property = new NameInfo(attribute.getName());
////                property.setNodeType(NameInfo.NodeType.PROPERTY);
////                attributeNameInfo.getChildren().add(property);
////            }
////        }
////
////        // 操作
////        final MBeanOperationInfo[] operations = mBeanInfo.getOperations();
////        if (operations != null && operations.length > 0){
////            final NameInfo operationsNameInfo = new NameInfo("操作");
////            operationsNameInfo.setNodeType(NameInfo.NodeType.OPERATION_FOLDER);
////            parent.getChildren().add(operationsNameInfo);
////
////            for (MBeanOperationInfo operation : operations) {
////                final NameInfo operationNameInfo = new NameInfo(operation.getName());
////                operationNameInfo.setNodeType(NameInfo.NodeType.OPERATION);
////                operationsNameInfo.getChildren().add(operationNameInfo);
////            }
////        }
////
////        // 通知
////        final MBeanNotificationInfo[] notifications = mBeanInfo.getNotifications();
////        if (notifications != null && notifications.length > 0){
////            final NameInfo notificationsNameInfo = new NameInfo("通知");
////            notificationsNameInfo.setNodeType(NameInfo.NodeType.NOTIFY_FOLDER);
////            parent.getChildren().add(notificationsNameInfo);
////
////            for (MBeanNotificationInfo notificationInfo : notifications) {
////                final NameInfo notificationNameInfo = new NameInfo(notificationInfo.getName());
////                notificationNameInfo.setNodeType(NameInfo.NodeType.NOTIFY);
////                notificationsNameInfo.getChildren().add(notificationNameInfo);
////            }
////        }
//	}
//
//	public MBeanInfo mBeanInfo(String connName, String mBeanName) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(connName);
//		return mBeanServerConnection.getMBeanInfo(createBeanName(mBeanName));
//	}
//
//	public Object proxyMXBean(String connName, String mBeanName, String className) throws IOException, ClassNotFoundException {
//		log.info("加载 MXBean 数据: {}-{}-{}",connName,mBeanName,className);
//		long startTime = System.currentTimeMillis();
//		final Class<?> mbeanClass = Class.forName(className);
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(connName);
//		final Object mxBeanProxy = JMX.newMXBeanProxy(mBeanServerConnection, createBeanName(mBeanName), mbeanClass);
//		log.info("加载 MXBean 数据: {}-{}-{} 耗时: {} ms",connName,mBeanName,className,(System.currentTimeMillis() - startTime));
//		return mxBeanProxy;
//	}
//
//	/**
//	 * 获取某个接口的多个代理类, 只支持 PlatformManagedObject 的子接口
//	 * @param connName 连接名
//	 * @param interfaceName 接口名
//	 * @return
//	 */
//	public List<PlatformManagedObject> proxyMXBeans(String connName,String interfaceName) throws ClassNotFoundException, IOException {
//		final Class<PlatformManagedObject> mbeanClass = (Class<PlatformManagedObject>) Class.forName(interfaceName);
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(connName);
//		return ManagementFactory.getPlatformMXBeans(mBeanServerConnection,mbeanClass);
//	}
//
////    Retryer<Boolean> retryer = null;
////    {
////
////        /**
////         * 当发生 IO 异常重试, 总共尝试 3 次, 每次等待 1s
////         */
////        retryer = RetryerBuilder.<Boolean> newBuilder()
////                .retryIfExceptionOfType(IOException.class)
////                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
////                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
////                .build();
////    }
//
//	/**
//	 * 加载 mbean 连接
//	 * @param jmxHostAndPort
//	 * @return
//	 * @throws IOException
//	 */
//	MBeanServerConnection loadMBeanConnection(String connName) throws IOException {
//		MBeanServerConnection mBeanServerConnection = mBeanServerConnectionMap.get(connName);
//		if (mBeanServerConnection != null ){
//			log.info("命中连接: {}",connName);
//			try {
//				mBeanServerConnection.getDomains();
//				return mBeanServerConnection;
//			}catch (IOException e){
//				log.warn("连接[{}]已经断开, 将重连");
//				mBeanServerConnectionMap.remove(connName);
//			}
//		}
//
//		// 获取一个到 mBeanServer 的连接
//		final String connectInfo = connectService.loadContent("jvm", connName);
//		final JMXConnectInfo jmxConnectInfo = JSON.parseObject(connectInfo, JMXConnectInfo.class);
//		final String jmxHostAndPort = jmxConnectInfo.getJmxHostAndPort();
//
//		JMXServiceURL jmxSeriverUrl = new JMXServiceURL("service:jmx:rmi://" + jmxHostAndPort + "/jndi/rmi://" + jmxHostAndPort + "/jmxrmi");
//		Map credentials = new HashMap(1);
//		String[] creds = new String[]{jmxConnectInfo.getUsername(), jmxConnectInfo.getPassword()};
//		credentials.put(JMXConnector.CREDENTIALS, creds);
//		// 主要耗时方法在获取连接
//		final long startTime = System.currentTimeMillis();
//		JMXConnector connector = JMXConnectorFactory.connect(jmxSeriverUrl,credentials);
//		log.info("连接 mBean[{}]耗时[{} ms]",jmxHostAndPort,(System.currentTimeMillis() - startTime));
//		MBeanServerConnection mbeanConnection = connector.getMBeanServerConnection();
//		mBeanServerConnectionMap.put(connName,mbeanConnection);
//		return mbeanConnection;
//	}
//
//	/**
//	 * mBean 方法调用
//	 * 参数最多只支持一维数组, 多维数组不支持
//	 * @param invokeParam
//	 * @return
//	 */
//	public Object invokeMBean(InvokeParam invokeParam) throws IOException, MBeanException, InstanceNotFoundException, ReflectionException, ClassNotFoundException, IntrospectionException {
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(invokeParam.getConnName());
//		final ObjectName objectName = createBeanName(invokeParam.getBeanName());
//
//		final Object[] params = invokeParam.getParams();
//
//		if (params != null && invokeParam.getSignature() != null) {
//			for (int i = 0; i < invokeParam.getSignature().length; i++) {
//				final String signature = invokeParam.getSignature()[i];
//				final Object param = params[i];
//				final Object typeConvert = typeConvert(param, signature);
//				params[i] = typeConvert;
//			}
//		}
//
//		return mBeanServerConnection.invoke(objectName,invokeParam.getOperation(), params,invokeParam.getSignature());
//
//	}
//
//	public Object typeConvert(Object param,String signature) throws ClassNotFoundException {
//		switch (signature){
//			case "long":
//				return NumberUtils.toLong(Objects.toString(param));
//			case "int":
//				return NumberUtils.toInt(Objects.toString(param));
//			case "java.lang.String":
//				return param;
//		}
//
//		// 如果是数组类型, 并且传入进来是 ArrayList , 那么把 ArrayList 转数组
//		if (signature.startsWith("[") && param instanceof ArrayList) {
//			String className = signature.substring(2, signature.length() - 1);
//			final Class<?> forName = Class.forName(className);
//
//			ArrayList paramList = (ArrayList) param;
//			final Object newInstance = Array.newInstance(forName, paramList.size());
//			for (int j = 0; j < paramList.size(); j++) {
//				Array.set(newInstance, j, paramList.get(j));
//			}
//			return newInstance;
//		}
//
//		throw new ToolException("当前类型 "+signature+" 不支持转换");
//	}
//
//
//	public List<Attribute> attrValue(String connName, String mBeanName, String[] attrNames) throws IOException, ReflectionException, InstanceNotFoundException {
//		final MBeanServerConnection mBeanServerConnection = loadMBeanConnection(connName);
//		final ObjectName objectName = createBeanName(mBeanName);
//
//		final AttributeList attributes = mBeanServerConnection.getAttributes(objectName, attrNames);
//		return attributes.asList();
//	}
//
//	ObjectName createBeanName(String beanName) {
//		try {
//			return new ObjectName(beanName);
//		} catch (MalformedObjectNameException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Autowired
//	private MBeanMonitorService mBeanMonitorService;
//
//	public ThreadInfo[] threads(String connName) throws IOException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
//		final long[] allThreadIds = threadMXBean.getAllThreadIds();
//		final ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(allThreadIds);
//		return threadInfo;
//	}
//
//	public ThreadInfo thread(String connName,long threadId) throws IOException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
//		return threadMXBean.getThreadInfo(threadId,100);
//	}
//
//	/**
//	 * 将线程进行分组, 如前 10 位是一致的, 则为一组, 当线程池处理
//	 * @param connName
//	 * @return
//	 * @throws IOException
//	 */
//	public List<ThreadPoolInfo> threadPoolInfos(String connName) throws IOException {
//		List<ThreadPoolInfo> threadPoolInfos = new ArrayList<>();
//
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
//		final long[] allThreadIds = threadMXBean.getAllThreadIds();
//		final ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(allThreadIds);
//
//		// 获取所有的线程名, 并照字典序排序
//		final List<ThreadPoolInfo.ThreadInfo> threadSimpleInfo = Arrays.stream(threadInfo).map(th -> new ThreadPoolInfo.ThreadInfo(th.getThreadId(),th.getThreadName(),th.getThreadState())).collect(
//			Collectors.toList());
//		Collections.sort(threadSimpleInfo);
//
//		int j = 0 ;
//		for (int i = 0; i < threadSimpleInfo.size(); i = j) {
//			List<ThreadPoolInfo.ThreadInfo> group = new ArrayList<>();
//			group.add(threadSimpleInfo.get(i));
//			final ThreadPoolInfo.ThreadInfo needCompareThread = threadSimpleInfo.get(i);
//			for (j = (i+1); j < threadSimpleInfo.size(); j++) {
//				final String prefix = longestCommonPrefix(Arrays.asList(needCompareThread.getName(), threadSimpleInfo.get(j).getName()));
//				if (prefix.length() > 10){
//					// 超过 10 位相同的为一组
//					group.add(threadSimpleInfo.get(j));
//				}else{
//					break;
//				}
//			}
//
//			// 求当前组的最大公共前缀
//			final String maxPublicPrefix = longestCommonPrefix(group.stream().map(ThreadPoolInfo.ThreadInfo::getName).collect(Collectors.toList()));
//			final ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo(maxPublicPrefix);
//			threadPoolInfo.setChildren(group);
//			threadPoolInfos.add(threadPoolInfo);
//		}
//
//		return threadPoolInfos;
//	}
//
//	/**
//	 * 检查死锁
//	 * @param connName
//	 * @return
//	 */
//	public ThreadInfo[] checkDeadLock(String connName) throws IOException, IntrospectionException, ReflectionException, InstanceNotFoundException, MBeanException, ClassNotFoundException {
//		final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
//		ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
//
//		final InvokeParam findDeadlockedThreads = new InvokeParam(connName, "java.lang:type=Threading", "findDeadlockedThreads");
//		final long[] deadLockThreadIds = (long[])mBeanMonitorService.invokeMBean(findDeadlockedThreads);
//		if (deadLockThreadIds != null && deadLockThreadIds.length > 0) {
//			return threadMXBean.getThreadInfo(deadLockThreadIds);
//		}
//		return null;
//	}
//
//	/**
//	 * 查询字符串列表中的最大公共前缀
//	 * @param strs
//	 * @return
//	 */
//	String longestCommonPrefix(List<String> strs) {
//		if (strs == null || strs.size() == 0) {
//			return "";
//		}
//		if (strs.size() == 1){
//			return strs.get(0);
//		}
//		String ans = strs.get(0);
//		for (int j = 1; j < strs.size(); j++) {
//			int i = 0;
//			for (; i < ans.length() && i < strs.get(j).length(); i++) {
//				if (ans.charAt(i) != strs.get(j).charAt(i)) {
//					break;
//				}
//			}
//			ans = ans.substring(0, i);
//			if ("".equals(ans)) {
//				return "";
//			}
//		}
//		return ans;
//	}


}
