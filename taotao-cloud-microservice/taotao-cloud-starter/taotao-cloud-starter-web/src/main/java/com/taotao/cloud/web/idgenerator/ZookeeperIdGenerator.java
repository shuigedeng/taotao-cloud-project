package com.taotao.cloud.web.idgenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.xxl.job.core.util.IpUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.boot.CommandLineRunner;

/**
 * id生成器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-19 11:18:07
 */
public class ZookeeperIdGenerator implements CommandLineRunner {

	private static final byte WORKER_ID_BIT_LENGTH = 16;

	private CuratorFramework curatorFramework;

	public ZookeeperIdGenerator(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	@Override
	public void run(String... args) throws Exception {
		SnowflakeZookeeper holder = new SnowflakeZookeeper(curatorFramework);
		boolean initFlag = holder.init();
		if (initFlag) {
			int workerId = holder.getWorkerId();

			LogUtil.info("当前ID生成器编号: " + workerId);
			IdGeneratorOptions options = new IdGeneratorOptions((short) workerId);
			options.WorkerIdBitLength = WORKER_ID_BIT_LENGTH;
			YitIdHelper.setIdGenerator(options);
		}
	}

	public static class SnowflakeZookeeper {

		//保存自身的key ip:port-000000001
		private String zkAddressNode = null;

		//保存自身的key ip:port
		private String listenAddress;

		private int workerId;

		private String port;

		private String projectName = PropertyUtil.getProperty("spring.application.name");

		private final String PREFIX_ZK_PATH = "/snowflake/" + projectName;

		private final String PROP_PATH =
			System.getProperty("java.io.tmpdir") + File.separator + projectName
				+ "/leafconf/{port}/workerID.properties";

		//保存所有数据持久的节点
		private final String PATH_FOREVER = PREFIX_ZK_PATH + "/forever";

		private String ip;

		private String connectionString;

		private long lastUpdateTime;

		private final CuratorFramework curator;

		public SnowflakeZookeeper(CuratorFramework curator) {
			this.ip = IpUtil.getIp();
			this.curator = curator;
		}

		public SnowflakeZookeeper(String ip, CuratorFramework curator) {
			this.ip = ip;
			this.curator = curator;
		}

		public SnowflakeZookeeper(String ip, String port, String connectionString) {
			this.ip = ip;
			this.port = port;
			this.listenAddress = ip + ":" + port;
			this.connectionString = connectionString;

			CuratorFramework curator = createWithOptions(connectionString,
				new RetryUntilElapsed(1000, 4), 10000, 6000);
			curator.start();
			this.curator = curator;
		}

		public Boolean init() {
			try {
				Stat stat = curator.checkExists().forPath(PATH_FOREVER);
				if (stat == null) {
					//不存在根节点,机器第一次启动,创建/snowflake/ip:port-000000000,并上传数据
					zkAddressNode = createNode(curator);
					//worker id 默认是0
					updateLocalWorkerID(workerId);
					//定时上报本机时间给forever节点
					ScheduledUploadData(curator, zkAddressNode);
					return true;
				} else {
					Map<String, Integer> nodeMap = Maps.newHashMap();//ip:port->00001
					Map<String, String> realNode = Maps.newHashMap();//ip:port->(ipport-000001)
					//存在根节点,先检查是否有属于自己的根节点
					List<String> keys = curator.getChildren().forPath(PATH_FOREVER);
					for (String key : keys) {
						String[] nodeKey = key.split("-");
						realNode.put(nodeKey[0], key);
						nodeMap.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
					}
					Integer workerId = nodeMap.get(listenAddress);
					if (workerId != null) {
						//有自己的节点,zk_AddressNode=ip:port
						zkAddressNode = PATH_FOREVER + "/" + realNode.get(listenAddress);
						this.workerId = workerId;//启动worder时使用会使用
						if (!checkInitTimeStamp(curator, zkAddressNode)) {
							throw new RuntimeException(
								"init timestamp check error,forever node timestamp gt this node time");
						}
						//准备创建临时节点
						doService(curator);
						updateLocalWorkerID(this.workerId);
						LogUtil.info(
							"[Old NODE]find forever node have this endpoint ip-{} port-{} workid-{} childnode and start SUCCESS",
							ip, port, this.workerId);
					} else {
						//表示新启动的节点,创建持久节点 ,不用check时间
						String newNode = createNode(curator);
						zkAddressNode = newNode;
						String[] nodeKey = newNode.split("-");
						this.workerId = Integer.parseInt(nodeKey[1]);
						doService(curator);
						updateLocalWorkerID(this.workerId);
						LogUtil.info(
							"[New NODE]can not find node on forever node that endpoint ip-{} port-{} workid-{},create own node on forever node and start SUCCESS ",
							ip, port, this.workerId);
					}
				}
			} catch (Exception e) {
				LogUtil.error("Start node ERROR {}", e);
				try {
					Properties properties = new Properties();
					properties.load(
						new FileInputStream(new File(PROP_PATH.replace("{port}", port + ""))));
					workerId = Integer.parseInt(properties.getProperty("workerID"));
					LogUtil.warn("START FAILED ,use local node file properties workerID-{}",
						workerId);
				} catch (Exception e1) {
					LogUtil.error("Read file error ", e1);
					return false;
				}
			}
			return true;
		}

		private void doService(CuratorFramework curator) {
			// /snowflake_forever/ip:port-000000001
			ScheduledUploadData(curator, zkAddressNode);
		}

		private void ScheduledUploadData(final CuratorFramework curator,
			final String zk_AddressNode) {
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread thread = new Thread(r, "schedule-upload-time");
				thread.setDaemon(true);
				return thread;
			}).scheduleWithFixedDelay(() -> updateNewData(curator, zk_AddressNode), 1L, 3L,
				TimeUnit.SECONDS);//每3s上报数据
		}

		private boolean checkInitTimeStamp(CuratorFramework curator, String zk_AddressNode)
			throws Exception {
			byte[] bytes = curator.getData().forPath(zk_AddressNode);
			Endpoint endPoint = deBuildData(new String(bytes));
			//该节点的时间不能小于最后一次上报的时间
			return !(endPoint.getTimestamp() > System.currentTimeMillis());
		}

		/**
		 * 创建持久顺序节点 ,并把节点数据放入 value
		 */
		private String createNode(CuratorFramework curator) throws Exception {
			try {
				return curator.create().creatingParentsIfNeeded()
					.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.forPath(PATH_FOREVER + "/" + listenAddress + "-", buildData().getBytes());
			} catch (Exception e) {
				LogUtil.error("create node error msg {} ", e.getMessage());
				throw e;
			}
		}

		private void updateNewData(CuratorFramework curator, String path) {
			try {
				if (System.currentTimeMillis() < lastUpdateTime) {
					return;
				}
				curator.setData().forPath(path, buildData().getBytes());
				lastUpdateTime = System.currentTimeMillis();
			} catch (Exception e) {
				LogUtil.info("update init data error path is {} error is {}", path, e);
			}
		}

		/**
		 * 构建需要上传的数据
		 */
		private String buildData() throws JsonProcessingException {
			Endpoint endpoint = new Endpoint(ip, port, System.currentTimeMillis());
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(endpoint);
		}

		private Endpoint deBuildData(String json) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Endpoint.class);
		}

		/**
		 * 在节点文件系统上缓存一个workid值,zk失效,机器重启时保证能够正常启动
		 *
		 * @param workerID
		 */
		private void updateLocalWorkerID(int workerID) {
			File leafConfFile = new File(PROP_PATH.replace("{port}", port));
			boolean exists = leafConfFile.exists();
			LogUtil.info("file exists status is {}", exists);
			if (exists) {
				try {
					FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID, false);
					LogUtil.info("update file cache workerID is {}", workerID);
				} catch (IOException e) {
					LogUtil.error("update file cache error ", e);
				}
			} else {
				//不存在文件,父目录页肯定不存在
				try {
					boolean mkdirs = leafConfFile.getParentFile().mkdirs();
					LogUtil.info(
						"init local file cache create parent dis status is {}, worker id is {}",
						mkdirs, workerID);
					if (mkdirs) {
						if (leafConfFile.createNewFile()) {
							FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID,
								false);
							LogUtil.info("local file cache workerID is {}", workerID);
						}
					} else {
						LogUtil.warn("create parent dir error===");
					}
				} catch (IOException e) {
					LogUtil.warn("craete workerID conf file error", e);
				}
			}
		}

		private CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy,
			int connectionTimeoutMs, int sessionTimeoutMs) {
			return CuratorFrameworkFactory.builder().connectString(connectionString)
				.retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(sessionTimeoutMs)
				.build();
		}

		/**
		 * 上报数据结构
		 */
		public static class Endpoint {

			private String ip;
			private String port;
			private long timestamp;

			public Endpoint() {
			}

			public Endpoint(String ip, String port, long timestamp) {
				this.ip = ip;
				this.port = port;
				this.timestamp = timestamp;
			}

			public String getIp() {
				return ip;
			}

			public void setIp(String ip) {
				this.ip = ip;
			}

			public String getPort() {
				return port;
			}

			public void setPort(String port) {
				this.port = port;
			}

			public long getTimestamp() {
				return timestamp;
			}

			public void setTimestamp(long timestamp) {
				this.timestamp = timestamp;
			}
		}


		public String getZkAddressNode() {
			return zkAddressNode;
		}

		public void setZkAddressNode(String zkAddressNode) {
			this.zkAddressNode = zkAddressNode;
		}

		public String getListenAddress() {
			return listenAddress;
		}

		public void setListenAddress(String listenAddress) {
			this.listenAddress = listenAddress;
		}

		public int getWorkerId() {
			return workerId;
		}

		public void setWorkerId(int workerId) {
			this.workerId = workerId;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public String getPREFIX_ZK_PATH() {
			return PREFIX_ZK_PATH;
		}

		public String getPROP_PATH() {
			return PROP_PATH;
		}

		public String getPATH_FOREVER() {
			return PATH_FOREVER;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getConnectionString() {
			return connectionString;
		}

		public void setConnectionString(String connectionString) {
			this.connectionString = connectionString;
		}

		public long getLastUpdateTime() {
			return lastUpdateTime;
		}

		public void setLastUpdateTime(long lastUpdateTime) {
			this.lastUpdateTime = lastUpdateTime;
		}
	}

}
