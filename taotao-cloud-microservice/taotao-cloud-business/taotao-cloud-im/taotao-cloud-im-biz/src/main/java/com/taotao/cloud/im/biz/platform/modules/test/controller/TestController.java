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

package com.taotao.cloud.im.biz.platform.modules.test.controller;

import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 测试 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    /** 获取所有key */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getRedis")
    public AjaxResult getRedis() {
        Set<String> keys = redisUtils.keys("*");
        return AjaxResult.success(
                keys.stream().sorted(Comparator.naturalOrder()).toList());
    }

    /** 删除指定key */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/removeRedis/{key}")
    public AjaxResult removeRedis(@PathVariable String key) {
        redisUtils.delete(key);
        return AjaxResult.success();
    }

    /** 删除一组key */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/delRedis/{key}")
    public AjaxResult delRedis(@PathVariable String key) {
        Set<String> set = redisUtils.keys(key);
        for (String k : set) {
            redisUtils.delete(k);
        }
        return AjaxResult.success();
    }
}
