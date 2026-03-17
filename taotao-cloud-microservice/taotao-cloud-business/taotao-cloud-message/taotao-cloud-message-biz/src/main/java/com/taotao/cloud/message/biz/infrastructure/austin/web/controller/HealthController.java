package com.taotao.cloud.message.biz.infrastructure.austin.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检测
 *
 * @author shuigedeng
 */
@Slf4j
@RestController
@Api("健康检测")
public class HealthController {
    @GetMapping("/")
    @ApiOperation("/健康检测")
    public String health() {
        return "success";
    }
}
