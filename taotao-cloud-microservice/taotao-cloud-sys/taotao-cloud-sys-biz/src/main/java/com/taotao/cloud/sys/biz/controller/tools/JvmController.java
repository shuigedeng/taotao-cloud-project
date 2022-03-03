package com.taotao.cloud.sys.biz.controller.tools;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JvmController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:05:02
 */
@Validated
@RestController
@Tag(name = "工具管理端-jvm管理API", description = "工具管理端-jvm管理API")
@RequestMapping("/sys/tools/jvm")
public class JvmController {

	//@Autowired
	//private MBeanMonitorService mBeanMonitorService;
	//@Autowired
	//private DiagnosticCommandService diagnosticCommandService;
	//
	///**
	// * 监控数据列表 系统,运行时数据,编译,内存,线程,类加载器,vm参数
	// */
	//@GetMapping("/dashboard")
	//public AggregationVMInfo dashboard(@NotBlank final String connName)
	//	throws ExecutionException, InterruptedException, IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
	//	final AggregationVMInfo aggregationVMInfo = new AggregationVMInfo();
	//	final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
	//					OperatingSystemMXBean.class.getName());
	//				aggregationVMInfo.setSystem((OperatingSystemMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class.getName());
	//				aggregationVMInfo.setRuntime((RuntimeMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.COMPILATION_MXBEAN_NAME,
	//					CompilationMXBean.class.getName());
	//				aggregationVMInfo.setCompilation((CompilationMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class.getName());
	//				aggregationVMInfo.setThread((ThreadMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.CLASS_LOADING_MXBEAN_NAME,
	//					ClassLoadingMXBean.class.getName());
	//				aggregationVMInfo.setClassLoading((ClassLoadingMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final Object proxyMXBean = mBeanMonitorService.proxyMXBean(connName,
	//					ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class.getName());
	//				aggregationVMInfo.setMemory((MemoryMXBean) proxyMXBean);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final List<VMParam> vmParams = diagnosticCommandService.flagsSetted(connName);
	//				aggregationVMInfo.setFlags(vmParams);
	//			} catch (Exception e) {
	//				log.info("执行 flagsSetted 获取时异常:{}", e.getMessage(), e);
	//			}
	//		}),
	//		CompletableFuture.runAsync(() -> {
	//			try {
	//				final List<PlatformManagedObject> platformManagedObjects = mBeanMonitorService.proxyMXBeans(
	//					connName, GarbageCollectorMXBean.class.getName());
	//				List<GarbageCollectorMXBean> garbageCollectorMXBeans = new ArrayList<>();
	//				for (PlatformManagedObject platformManagedObject : platformManagedObjects) {
	//					garbageCollectorMXBeans.add((GarbageCollectorMXBean) platformManagedObject);
	//				}
	//				aggregationVMInfo.setGarbageCollectors(garbageCollectorMXBeans);
	//			} catch (IOException | ClassNotFoundException e) {
	//				log.info("执行 mbean 获取时异常:{}", e.getMessage(), e);
	//			}
	//
	//		})
	//	);
	//	voidCompletableFuture.get();
	//	return aggregationVMInfo;
	//}
	//
	//
	///**
	// * 监控堆区内存数据
	// *
	// * @return
	// */
	//@GetMapping("/diagnostic/gcClassHistogram")
	//public HeapHistogramImpl gcClassHistogram(@NotBlank String connName, boolean all)
	//	throws MalformedObjectNameException, ReflectionException, MBeanException, InstanceNotFoundException, IOException {
	//	return heapService.gcClassHistogram(connName, all);
	//}
	//
	//@GetMapping("/diagnostic/flagsSetted")
	//public List<VMParam> flagsSetted(@NotBlank String connName)
	//	throws ReflectionException, MBeanException, InstanceNotFoundException, IOException {
	//	return heapService.flagsSetted(connName);
	//}
	//
	//
	///**
	// * 类似 jconsole 那样展示 mbean 树结构
	// *
	// * @param jmxHostAndPort
	// * @return
	// */
	//@GetMapping("/mbean/names")
	//public List<NameInfo> mBeans(@NotBlank String connName)
	//	throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
	//	return mBeanMonitorService.mBeans(connName);
	//}
	//
	///**
	// * 获取 mbean 的详细信息
	// *
	// * @param jmxHostAndPort 开启了JMX的主机:端口
	// * @param mBeanName      mBean名称
	// * @return
	// */
	//@GetMapping("/mbean/detail")
	//public MBeanInfo mBeanInfo(@NotBlank String connName, @NotBlank String mBeanName)
	//	throws IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
	//	return mBeanMonitorService.mBeanInfo(connName, mBeanName);
	//}
	//
	///**
	// * 获取指定 mBean 的单个代理类
	// *
	// * @param connName  连接名
	// * @param mBeanName mBean
	// * @param className 类名
	// * @return
	// */
	//@GetMapping("/mbean/proxyMXBean")
	//public Object proxyMXBean(@NotBlank String connName, @NotBlank String mBeanName,
	//	@NotBlank String className) throws IOException, ClassNotFoundException {
	//	return mBeanMonitorService.proxyMXBean(connName, mBeanName, className);
	//}
	//
	///**
	// * 获取代理类列表
	// *
	// * @param connName      连接名
	// * @param interfaceName 接口名称
	// * @return
	// */
	//@GetMapping("/mbean/proxyMXBeans")
	//public List<PlatformManagedObject> proxyMXBeans(@NotBlank String connName,
	//	@NotBlank String interfaceName) throws IOException, ClassNotFoundException {
	//	return mBeanMonitorService.proxyMXBeans(connName, interfaceName);
	//}
	//
	///**
	// * 获取属性值
	// *
	// * @param jmxHostAndPort 开启了JMX的主机:端口
	// * @param mBeanName      bean名称
	// * @param attrNames      属性名列表
	// * @return
	// */
	//@GetMapping("/mbean/attrValue")
	//public List<Attribute> attrValue(@NotBlank String connName, @NotBlank String mBeanName,
	//	String[] attrNames) throws ReflectionException, InstanceNotFoundException, IOException {
	//	return mBeanMonitorService.attrValue(connName, mBeanName, attrNames);
	//}
	//
	///**
	// * 调用 mbean 方法
	// *
	// * @param invokeParam 调用 mbean 的方法参数信息
	// * @return
	// */
	//@PostMapping("/mbean/invoke")
	//public Object invoke(@RequestBody @Validated InvokeParam invokeParam)
	//	throws ReflectionException, MBeanException, InstanceNotFoundException, IOException, ClassNotFoundException, IntrospectionException {
	//	return mBeanMonitorService.invokeMBean(invokeParam);
	//}
	//
	//
	///**
	// * 展示所有线程列表
	// *
	// * @param connName 连接名称
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/thread/list")
	//public ThreadInfo[] threads(@NotBlank String connName) throws IOException {
	//	return threadMXBeanService.threads(connName);
	//}
	//
	///**
	// * 获取线程详细信息
	// *
	// * @param connName
	// * @param threadId
	// * @return
	// * @throws IOException
	// */
	//@GetMapping("/thread/detail")
	//public ThreadInfo thread(@NotBlank String connName, long threadId) throws IOException {
	//	return threadMXBeanService.thread(connName, threadId);
	//}
	//
	///**
	// * 将线程池进行合并, 展示树状的线程列表
	// *
	// * @param connName
	// * @return
	// */
	//@GetMapping("/thread/list/threadPools")
	//public List<ThreadPoolInfo> threadPools(@NotBlank String connName) throws IOException {
	//	return threadMXBeanService.threadPoolInfos(connName);
	//}
	//
	///**
	// * 检查死锁
	// *
	// * @param connName
	// * @return
	// */
	//@GetMapping("/thread/checkDeadLock")
	//public ThreadInfo[] checkDeadLock(@NotBlank String connName)
	//	throws ReflectionException, IntrospectionException, IOException, InstanceNotFoundException, MBeanException, ClassNotFoundException {
	//	return threadMXBeanService.checkDeadLock(connName);
	//}
}
