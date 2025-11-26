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

package com.taotao.cloud.distribution.biz.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.itstyle.distribution.common.entity.Result;
import com.itstyle.distribution.common.redis.RedisUtil;
import com.itstyle.distribution.common.utils.HttpClient;
import com.itstyle.distribution.common.utils.IPUtils;
import com.itstyle.distribution.queue.activemq.ActiveMQSender;
import com.itstyle.distribution.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "秒杀商品")
@RestController
@RequestMapping("/seckillPage")
public class SeckillPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillPageController.class);

    @Autowired
    private ISeckillService seckillService;

    @Autowired
    private ActiveMQSender activeMQSender;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HttpClient httpClient;

    @Value("${qq.captcha.url}")
    private String url;

    @Value("${qq.captcha.aid}")
    private String aid;

    @Value("${qq.captcha.AppSecretKey}")
    private String appSecretKey;

    @ApiOperation(value = "秒杀商品列表", nickname = "小柒2012")
    @PostMapping("/list")
    public Result list() {
        // 返回JSON数据、前端VUE迭代即可
        List<Seckill> List = seckillService.getSeckillList();
        return Result.ok(List);
    }

    @PostMapping("/startSeckill")
    public Result startSeckill(String ticket, String randstr, HttpServletRequest request) {
        HttpMethod method = HttpMethod.POST;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("aid", aid);
        params.add("AppSecretKey", appSecretKey);
        params.add("Ticket", ticket);
        params.add("Randstr", randstr);
        params.add("UserIP", IPUtils.getIpAddr());
        String msg = httpClient.client(url, method, params);
        /**
         * response: 1:验证成功，0:验证失败，100:AppSecretKey参数校验错误[required]
         * evil_level:[0,100]，恶意等级[optional] err_msg:验证错误信息[optional]
         */
        // {"response":"1","evil_level":"0","err_msg":"OK"}
        JSONObject json = JSONObject.parseObject(msg);
        String response = (String) json.get("response");
        if ("1".equals(response)) {
            // 进入队列、假数据而已
            Destination destination = new ActiveMQQueue("seckill.queue");
            activeMQSender.sendChannelMess(destination, 1000 + ";" + 1);
            return Result.ok();
        } else {
            return Result.error("验证失败");
        }
    }

    @ApiOperation(value = "最佳实践)", nickname = "爪哇笔记")
    @PostMapping("/startRedisCount")
    public Result startRedisCount(long secKillId, long userId) {
        /** 原子递减 */
        long number = redisUtil.decr(secKillId + "-num", 1);
        if (number >= 0) {
            seckillService.startSeckilDBPCC_TWO(secKillId, userId);
            LOGGER.info("用户:{}秒杀商品成功", userId);
        } else {
            LOGGER.info("用户:{}秒杀商品失败", userId);
        }
        return Result.ok();
    }
}
