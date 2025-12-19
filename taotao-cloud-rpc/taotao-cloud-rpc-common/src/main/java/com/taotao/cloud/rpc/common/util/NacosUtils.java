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

package com.taotao.cloud.rpc.common.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.StringUtils;
import com.taotao.cloud.rpc.common.enums.LoadBalancerCode;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.loadbalancer.LoadBalancer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * NacosUtils
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class NacosUtils {

    private static final Set<String> serviceNames = new HashSet<>();
    private static LoadBalancer loadBalancer;
    private static String SERVER_ADDR = "127.0.0.1:8848";
    private static NamingService namingService;
    private static InetSocketAddress inetSocketAddress;

    // 2. 加载配置文件，只需加载一次
    static {
        // 2.1 创建Properties对象
        Properties p = new Properties();
        // 2.2 调用p对象中的load方法进行配置文件的加载
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        InputStream is = null;
        String[] nodes = null;
        PropertyResourceBundle configResource = null;
        String useCluster = "";
        String balancer = "round";
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties"));) {

            configResource = new PropertyResourceBundle(bufferedReader);
            useCluster = configResource.getString(PropertiesConstants.NACOS_CLUSTER_USE);

            if ("false".equals(useCluster)
                    || "default".equals(useCluster)
                    || StringUtils.isBlank(useCluster)) {
                log.info("begin start with single mode");
                nodes = new String[1];
                try {
                    nodes[0] = configResource.getString(PropertiesConstants.NACOS_REGISTER_ADDR);
                } catch (MissingResourceException registerAddException) {
                    nodes[0] = SERVER_ADDR;
                    log.warn("nacos register address attribute is missing");
                    log.info("use default register address : " + SERVER_ADDR);
                }
            } else if ("true".equals(useCluster)) {
                log.info("cluster mode attribute is true and start with cluster mode");
                try {
                    balancer = configResource.getString(PropertiesConstants.NACOS_LOAD_BALANCER);
                } catch (MissingResourceException loadBalancerException) {
                    log.info(
                            "nacos property attribute is missing: {}",
                            loadBalancerException.getMessage());
                    log.info("use default loadBalancer : " + balancer);
                }
                try {
                    nodes =
                            configResource
                                    .getString(PropertiesConstants.NACOS_CLUSTER_NODES)
                                    .split("[;,|]");
                } catch (MissingResourceException clusterNodesException) {
                    log.error("nacos cluster nodes attribute is missing: ", clusterNodesException);
                    throw new RuntimeException("nacos cluster nodes attribute is missing!");
                }
            } else {
                throw new RuntimeException("nacos cluster mode attribute is illegal!");
            }
            log.info(
                    "read resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
        } catch (MissingResourceException clusterUseException) {
            log.warn("nacos cluster use attribute is missing");
            log.info("begin start with default single mode");
            nodes = new String[1];
            try {
                String nacosRegisterAddr =
                        configResource.getString(PropertiesConstants.NACOS_REGISTER_ADDR);
                nodes[0] = StringUtils.isBlank(nacosRegisterAddr) ? SERVER_ADDR : nacosRegisterAddr;
            } catch (MissingResourceException registerAddException) {
                nodes[0] = SERVER_ADDR;
                log.warn("nacos register address attribute is missing");
                log.info("use default register address : " + SERVER_ADDR);
            }
        } catch (IOException ioException) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
            try {
                ResourceBundle resource = ResourceBundle.getBundle("resource");

                try {
                    useCluster = resource.getString(PropertiesConstants.NACOS_CLUSTER_USE);

                    if ("false".equals(useCluster)
                            || "default".equals(useCluster)
                            || StringUtils.isBlank(useCluster)) {
                        log.info("begin start with default single mode");
                        nodes = new String[1];
                        try {
                            String nacosRegisterAddr =
                                    resource.getString(PropertiesConstants.NACOS_REGISTER_ADDR);
                            nodes[0] =
                                    StringUtils.isBlank(nacosRegisterAddr)
                                            ? SERVER_ADDR
                                            : nacosRegisterAddr;
                        } catch (MissingResourceException registerAddException) {
                            nodes[0] = SERVER_ADDR;
                            log.warn("nacos register address attribute is missing");
                            log.info("use default register address : " + SERVER_ADDR);
                        }
                    } else if ("true".equals(useCluster)) {
                        log.info("cluster mode attribute is true and start with cluster mode");
                        try {
                            balancer = resource.getString(PropertiesConstants.NACOS_LOAD_BALANCER);
                        } catch (MissingResourceException loadBalancerException) {
                            log.info(
                                    "nacos property attribute is missing: {}",
                                    loadBalancerException.getMessage());
                            log.info("use default loadBalancer : " + balancer);
                        }
                        try {
                            nodes =
                                    resource.getString(PropertiesConstants.NACOS_CLUSTER_NODES)
                                            .split("[;,|]");
                        } catch (MissingResourceException clusterNodesException) {
                            log.error(
                                    "nacos cluster nodes attribute is missing: ",
                                    clusterNodesException);
                            throw new RuntimeException("nacos cluster nodes attribute is missing!");
                        }
                    } else {
                        throw new RuntimeException("nacos cluster mode attribute is illegal!");
                    }
                } catch (MissingResourceException clusterUseException) {
                    log.info("cluster mode attribute is missing and start with single mode");
                    nodes = new String[1];
                    nodes[0] = resource.getString(PropertiesConstants.NACOS_REGISTER_ADDR);
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("Register center bind with default address {}", SERVER_ADDR);
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }
        int pre = -1;
        String host = "";
        Integer port = 0;

        String node = null;
        if ("random".equals(balancer)) {
            loadBalancer = LoadBalancer.getByCode(LoadBalancerCode.RANDOM.getCode());
            log.info("use { {} } loadBalancer", loadBalancer.getClass().getName());
        } else if ("round".equals(balancer)) {
            loadBalancer = LoadBalancer.getByCode(LoadBalancerCode.ROUNDROBIN.getCode());
            log.info("use { {} } loadBalancer", loadBalancer.getClass().getName());
        } else {
            log.error("naocs cluster loadBalancer attribute is illegal!");
            throw new RuntimeException("naocs cluster loadBalancer attribute is illegal!");
        }
        do {
            try {
                node = loadBalancer.selectNode(nodes);
                log.info("waiting for connection to the registration center...");

                if (( pre = node.indexOf(":") ) > 0 && pre == node.lastIndexOf(":")) {
                    boolean valid = IpUtils.valid(host = node.substring(0, pre));
                    if (valid) {
                        host = node.substring(0, pre);
                        port = Integer.parseInt(node.substring(pre + 1));
                        if (host.equals("localhost")) {
                            SERVER_ADDR = "127.0.0.1:" + port;
                        } else {
                            SERVER_ADDR = node;
                        }

                    } else {
                        log.error("wrong ip address: {}", node);
                    }
                } else if (!node.equals("")) {
                    log.error("wrong ip address: {}", node);
                }

            } catch (RpcException e) {

            }
            // 初始化 Nacos 注册中心服务接口
            namingService = getNacosNamingService();
        } while (namingService.getServerStatus() == "DOWN");
        if (namingService.getServerStatus() == "UP") {
            log.info("Register center bind with address {}", node);
        } else if (nodes != null && nodes.length == 1) {
            log.error("SingleTon Register Center is down from {}", SERVER_ADDR);
        } else if (nodes != null && nodes.length != 1) {
            log.error("Cluster Register Center is down from ");
            log.error("---");
            for (int i = 0; i < nodes.length; i++) {
                log.error("{}", nodes[i]);
            }
            log.error("---");
        } else {
            log.error("Service occupy Internal Errors");
        }
    }

    public static void init() {
        log.debug("nacos Services has initialize successfully!");
    }

    /**
     * 获取绑定的 Nacos 服务
     *
     * @return Nacos 服务
     */
    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("error occurred when connecting to nacos server: ", e);
            return null;
        }
    }

    /**
     * 获取配置中心中与服务名匹配的所有实例，可以通过使用负载均衡选择其中一个实例
     *
     * @param serviceName 服务名
     * @return 实例列表
     */
    public static List<Instance> getAllInstance( String serviceName ) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 获取配置中心中与服务名匹配的所有实例，可以通过使用负载均衡选择其中一个实例
     *
     * @param serviceName 服务名
     * @param groupName 组名
     * @return 实例列表
     */
    public static List<Instance> getAllInstance( String serviceName, String groupName )
            throws NacosException {
        return namingService.getAllInstances(serviceName, groupName);
    }

    /**
     * 将服务名与对应服务所在的地址注册到注册中心
     *
     * @param serviceName 服务名
     * @param address 服务所在机器地址
     */
    public static void registerService( String serviceName, InetSocketAddress address )
            throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        log.info(
                "host[{}], service[{}] has been registered on Register Center",
                address.getHostName(),
                serviceName);
        inetSocketAddress = address;
        serviceNames.add(serviceName);
    }

    /**
     * 将服务名与对应服务所在的地址注册到注册中心
     *
     * @param serviceName 服务名
     * @param address 服务所在机器地址
     */
    public static void registerService(
            String serviceName, String groupName, InetSocketAddress address ) throws NacosException {
        namingService.registerInstance(
                serviceName, groupName, address.getHostName(), address.getPort());
        log.info(
                "host[{}], service[{}] has been registered on Register Center",
                address.getHostName(),
                serviceName);
        inetSocketAddress = address;
        serviceNames.add(serviceName);
    }

    /**
     * 清除服务启动所在地址下注册表所有服务项
     */
    public static void clearRegistry() {
        if (!serviceNames.isEmpty() && inetSocketAddress != null) {
            String hostname = inetSocketAddress.getHostName();
            int port = inetSocketAddress.getPort();
            for (String serviceName : serviceNames) {
                try {
                    serviceNames.remove(serviceName);
                    namingService.deregisterInstance(serviceName, hostname, port);
                } catch (NacosException e) {
                    log.error("Failed to cancel service:{}, info:{}", serviceName, e);
                }
            }
            log.info("All services on the nacos service have been cleaned up successfully");
        }
    }
}
