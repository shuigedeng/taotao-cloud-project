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

package com.taotao.cloud.hadoop.mr.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.hadoop.mr.service.MapReduceService;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * MapReduceController
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/30 17:39
 */
@RestController
@RequestMapping("/hadoop/reduce")
public class MapReduceController {

    @Autowired MapReduceService mapReduceService;

    /**
     * 分组统计、排序
     */
    @PostMapping(value = "groupSort")
    @ResponseBody
    public Result<String> groupSort(String jobName, String inputPath) throws Exception {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
            return Result.failed("请求参数为空");
        }
        mapReduceService.groupSort(jobName, inputPath);
        return Result.success("分组统计、排序成功");
    }
}
