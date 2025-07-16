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

package com.taotao.cloud.ccsr.client.loadbalancer;
//
// import com.taotao.cloud.ccsr.dto.ServerAddress;
//
// import java.util.List;
//
// public class Demo {
//    public static void main(String[] args) {
//        // 初始化服务列表（可以从配置或注册中心加载）
//        List<ServerAddress> servers = List.of(
//            new ServerAddress("server1", 8080, true),
//            new ServerAddress("server2", 8080, true),
//            new ServerAddress("server3", 8080, true)
//        );
//
//        // 选择负载均衡策略
//        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
//        // LoadBalancer loadBalancer = new RandomLoadBalancer();
//
//        // 创建服务发现对象
//        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(loadBalancer);
//        serviceDiscovery.update(servers);
//
//        // 模拟请求分发
//        for (int i = 0; i < 10; i++) {
//            try {
//                ServerAddress server = serviceDiscovery.selector();
//                System.out.println("Request " + (i+1) + " -> " + server.getHost());
//            } catch (Exception e) {
//                System.err.println("Error: " + e.getMessage());
//            }
//        }
//
//        // 模拟 server2 下线
//        serviceDiscovery.markServerDown("server2", 8080);
//        System.out.println("\nAfter marking server2 down:");
//
//        for (int i = 0; i < 5; i++) {
//            try {
//                ServerAddress server = serviceDiscovery.selector();
//                System.out.println("Request " + (i+1) + " -> " + server.getHost());
//            } catch (Exception e) {
//                System.err.println("Error: " + e.getMessage());
//            }
//        }
//    }
// }
