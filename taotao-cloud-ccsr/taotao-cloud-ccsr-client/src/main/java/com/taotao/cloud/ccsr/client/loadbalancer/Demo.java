package com.taotao.cloud.ccsr.client.loadbalancer;//package org.ohara.msc.loadbalancer;
//
//import org.ohara.msc.dto.ServerAddress;
//
//import java.util.List;
//
//public class Demo {
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
//}
