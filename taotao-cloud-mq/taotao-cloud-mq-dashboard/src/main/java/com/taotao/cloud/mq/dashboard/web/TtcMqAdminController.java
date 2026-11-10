package com.taotao.cloud.mq.dashboard.web;

import com.taotao.cloud.mq.dashboard.service.AdminService;
import com.taotao.cloud.mq.dashboard.web.dto.Dtos;
import com.taotao.cloud.mq.dashboard.web.dto.MessageQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ttc-MQ 控制台 REST 门面.
 * <p>
 * 端点契约由 ttc-mq-admin 前端 {@code src/api/index.ts} 反推而来，前端换后端时只改 baseUrl，
 * 不改这六个路径。
 */
@RestController
public class TtcMqAdminController {

    private final AdminService adminService;

    public TtcMqAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/cluster/overview")
    public Dtos.ClusterOverviewDto clusterOverview() {
        return adminService.clusterOverview();
    }

    @GetMapping("/cluster/brokers")
    public List<Dtos.BrokerInfoDto> listBrokers() {
        return adminService.listBrokers();
    }

    @GetMapping("/topics")
    public List<Dtos.TopicInfoDto> listTopics() {
        return adminService.listTopics();
    }

    @PostMapping("/topics")
    public Map<String, Object> createTopic(@RequestBody Map<String, Object> body) {
        String topic = String.valueOf(body.get("topic"));
        int readQueueNums = intOf(body.get("readQueueNums"), 4);
        int writeQueueNums = intOf(body.get("writeQueueNums"), 4);
        return adminService.createTopic(topic, readQueueNums, writeQueueNums);
    }

    @DeleteMapping("/topics/{topic}")
    public Map<String, Object> deleteTopic(@PathVariable("topic") String topic) {
        return adminService.deleteTopic(topic);
    }

    @PostMapping("/messages/query")
    public List<Dtos.MessageItemDto> queryMessages(@RequestBody MessageQueryRequest req) {
        return adminService.queryMessages(req);
    }

    @GetMapping("/consumer/groups")
    public List<Dtos.ConsumerGroupInfoDto> listConsumerGroups() {
        return adminService.listConsumerGroups();
    }

    private static int intOf(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
