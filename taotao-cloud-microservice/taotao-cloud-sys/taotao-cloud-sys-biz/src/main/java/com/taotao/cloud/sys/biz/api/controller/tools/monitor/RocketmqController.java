package com.taotao.cloud.sys.biz.api.controller.tools.monitor;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RocketmqController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:55:15
 */
@Validated
@RestController
@Tag(name = "工具管理端-rocketmq管理API", description = "工具管理端-rocketmq管理API")
@RequestMapping("/sys/tools/monitor/rocketmq")
public class RocketmqController {

}
