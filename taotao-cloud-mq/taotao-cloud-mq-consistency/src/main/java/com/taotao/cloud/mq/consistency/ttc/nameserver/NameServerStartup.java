package com.taotao.cloud.mq.consistency.ttc.nameserver;

import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * NameServer 进程入口（对标 RocketMQ NamesrvStartup）.
 * <p>
 * 启动方式：
 * <pre>
 *   java -cp z-mq-nameserver-*.jar com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerStartup [port] [kvConfigPath]
 * </pre>
 */
public class NameServerStartup {

    private static final Logger log = LoggerFactory.getLogger(NameServerStartup.class);

    public static void main(String[] args) throws Exception {
        // 解析命令行参数
        NamesrvConfig namesrvConfig = new NamesrvConfig();
        NettyServerConfig nettyServerConfig = new NettyServerConfig();

        if (args.length >= 1) {
            nettyServerConfig.setListenPort(Integer.parseInt(args[0]));
        } else {
            nettyServerConfig.setListenPort(9876);
        }
        if (args.length >= 2) {
            namesrvConfig.setKvConfigPath(args[1]);
        }

        log.info("Starting NameServer, listenPort={}, kvConfigPath={}",
                nettyServerConfig.getListenPort(), namesrvConfig.getKvConfigPath());

        NameServerController controller = new NameServerController(namesrvConfig, nettyServerConfig);
        boolean ok = controller.initialize();
        if (!ok) {
            log.error("NameServer initialize failed, exit");
            System.exit(1);
        }
        controller.start();

        // 注册 JVM 关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown, "NameServerShutdownHook"));

        log.info("NameServer started. Press any key to stop.");
        // 阻塞主线程
        Thread.currentThread().join();
    }
}
