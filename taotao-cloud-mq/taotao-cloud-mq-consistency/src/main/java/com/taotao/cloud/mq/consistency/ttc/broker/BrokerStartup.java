package com.taotao.cloud.mq.consistency.ttc.broker;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Broker 进程入口（对标 RocketMQ BrokerStartup）.
 * <p>
 * 启动方式：
 * <pre>
 *   java -jar z-mq-broker-*-exec.jar [brokerPort] [namesrvAddr] [storePath] [brokerIp1]
 * </pre>
 * {@code brokerIp1} 是**对外公布**的地址主机名（对标 RocketMQ 的 -c brokerIP1）：
 * 客户端是按 NameServer 路由表里这个地址来连 Broker 的，容器部署必须给可路由的值。
 */
public class BrokerStartup {

    private static final Logger log = LoggerFactory.getLogger(BrokerStartup.class);

    public static void main(String[] args) throws Exception {
        BrokerConfig brokerConfig = new BrokerConfig();
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        MessageStoreConfig messageStoreConfig = new MessageStoreConfig();

        if (args.length >= 1) {
            nettyServerConfig.setListenPort(Integer.parseInt(args[0]));
        } else {
            nettyServerConfig.setListenPort(10911);
        }
        if (args.length >= 2) {
            brokerConfig.setNamesrvAddr(args[1]);
        }
        if (args.length >= 3) {
            messageStoreConfig.setStorePathRootDir(args[2]);
        }
        if (args.length >= 4 && !args[3].isEmpty()) {
            brokerConfig.setBrokerIp1(args[3]);
        }

        log.info("Starting Broker, brokerPort={}, namesrvAddr={}, storePath={}, advertisedAddr={}",
                nettyServerConfig.getListenPort(),
                brokerConfig.getNamesrvAddr(),
                messageStoreConfig.getStorePathRootDir(),
                brokerConfig.getAdvertisedAddr());

        BrokerController controller = new BrokerController(brokerConfig, messageStoreConfig, nettyServerConfig);
        boolean ok = controller.initialize();
        if (!ok) {
            log.error("Broker initialize failed, exit");
            System.exit(1);
        }
        controller.start();

        Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown, "BrokerShutdownHook"));

        log.info("Broker started.");
        Thread.currentThread().join();
    }
}
