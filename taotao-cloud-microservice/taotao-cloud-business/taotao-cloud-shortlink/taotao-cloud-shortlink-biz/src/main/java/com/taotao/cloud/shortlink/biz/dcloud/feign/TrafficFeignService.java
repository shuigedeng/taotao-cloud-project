package com.taotao.cloud.shortlink.biz.dcloud.feign;

import net.xdclass.controller.request.UseTrafficRequest;
import net.xdclass.util.JsonData;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author 刘森飚
 * @since 2023-02-09
 */

@HttpExchange(name = "dcloud-account-service")
public interface TrafficFeignService {

    /**
     * 使用流量包
     * @param request
     * @return
     */
    @PostMapping(value = "/api/traffic/v1/reduce",headers = {"rpc-token=${rpc.token}"})
    JsonData useTraffic(@RequestBody UseTrafficRequest request);

}
