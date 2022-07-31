package com.taotao.cloud.health.collect;


import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * nacos 客户端性能采集
 */
public class NacosCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.nacos";

	private final CollectTaskProperties collectTaskProperties;

	public NacosCollectTask(CollectTaskProperties collectTaskProperties) {
		this.collectTaskProperties = collectTaskProperties;
	}

	@Override
	public int getTimeSpan() {
		return collectTaskProperties.getNacosTimeSpan();
	}

	@Override
	public String getDesc() {
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	public boolean getEnabled() {
		return collectTaskProperties.isNacosEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			Collector collector = Collector.getCollector();
			NacosServiceManager nacosServiceManager = ContextUtil.getBean(NacosServiceManager.class,
				false);
			if (Objects.nonNull(collector) && Objects.nonNull(nacosServiceManager)) {
				NacosClientInfo info = new NacosClientInfo();

				NamingService namingService = ReflectionUtil.getFieldValue(nacosServiceManager,
					"namingService");
				NacosNamingService nacosNamingService = (NacosNamingService) namingService;

				info.namespace = ReflectionUtil.getFieldValue(nacosNamingService, "namespace");
				info.endpoint = ReflectionUtil.getFieldValue(nacosNamingService, "endpoint");
				info.serverList = ReflectionUtil.getFieldValue(nacosNamingService, "serverList");
				info.cacheDir = ReflectionUtil.getFieldValue(nacosNamingService, "cacheDir");
				info.logName = ReflectionUtil.getFieldValue(nacosNamingService, "logName");

				//HostReactor hostReactor = ReflectionUtil.getFieldValue(nacosNamingService,
				//	"hostReactor");
				//NamingProxy serverProxy = ReflectionUtil.getFieldValue(nacosNamingService,
				//	"serverProxy");
				//BeatReactor beatReactor = nacosNamingService.getBeatReactor();
				//
				//info.serviceInfoMap = hostReactor.getServiceInfoMap();

				info.instances = nacosNamingService.getAllInstances(
					PropertyUtil.getProperty(CommonConstant.SPRING_APP_NAME_KEY),
					CommonConstant.SPRING_APP_NAME_KEY);
				info.servicesOfServer = nacosNamingService.getServicesOfServer(0,
					Integer.MAX_VALUE);
				info.subscribeServices = nacosNamingService.getSubscribeServices();
				info.serverStatus = nacosNamingService.getServerStatus();

				return info;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class NacosClientInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".namespace", desc = "nacos namespace")
		private String namespace = "";
		@FieldReport(name = TASK_NAME + ".endpoint", desc = "nacos endpoint")
		private String endpoint = "";
		@FieldReport(name = TASK_NAME + ".serverList", desc = "nacos serverList")
		private String serverList = "";
		@FieldReport(name = TASK_NAME + ".cacheDir", desc = "nacos cacheDir")
		private String cacheDir = "";
		@FieldReport(name = TASK_NAME + ".logName", desc = "nacos logName")
		private String logName = "";
		@FieldReport(name = TASK_NAME + ".serverStatus", desc = "nacos serverStatus")
		private String serverStatus = "";
		@FieldReport(name = TASK_NAME + ".instances", desc = "nacos instances")
		private List<Instance> instances;
		@FieldReport(name = TASK_NAME + ".serviceInfoMap", desc = "nacos serviceInfoMap")
		private Map<String, ServiceInfo> serviceInfoMap;
		@FieldReport(name = TASK_NAME + ".servicesOfServer", desc = "nacos servicesOfServer")
		private ListView<String> servicesOfServer;
		@FieldReport(name = TASK_NAME + ".subscribeServices", desc = "nacos subscribeServices")
		private List<ServiceInfo> subscribeServices;
	}
}
