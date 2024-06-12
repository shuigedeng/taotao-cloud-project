package com.taotao.cloud.rpc.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 服务注册<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    private String registryAddress;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * 连接zk
     *
     * @return org.apache.zookeeper.ZooKeeper
     * @author shuigedeng
     * @date 2024.06
     */
//    private ZooKeeper connectZookeeper() {
//        ZooKeeper zk = null;
//        try {
//            zk = new ZooKeeper(registryAddress, Constants.ZK_SESSION_TIMOUT, watchedEvent -> {
//                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
//                    countDownLatch.countDown();
//                }
//            });
//            countDownLatch.wait();
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("连接zk失败");
//        }
//        return zk;
//    }


    /**
     * 创建节点
     *
     * @param zk   zookeeper
     * @param data data
     * @return void
     * @author shuigedeng
     * @date 2020/2/27 13:47
     */
//    private void createNode(ZooKeeper zk, String data) {
//        try {
//            byte[] bytes = data.getBytes();
//            if (zk.exists(Constants.ZK_REGISTRY_PATH, null) == null) {
//                zk.create(Constants.ZK_REGISTRY_PATH, null,
//                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            }
//            zk.create(Constants.ZK_DATA_PATH, data.getBytes(),
//                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("创建节点失败");
//        }
//    }

    /**
     * 注册数据
     *
     * @param data data
     * @return void
     * @author shuigedeng
     * @date 2024.06
     */
    public void registry(String data) {
//        if (null != data) {
//            ZooKeeper zk = connectZookeeper();
//            if (null != zk) {
//                createNode(zk, data);
//            }
//        }
    }
}
