/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.bigdata.hadoop.mr.controller;

import com.taotao.cloud.bigdata.hadoop.mr.service.MapReduceService;
import com.taotao.cloud.core.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * MapReduceController
 *
 * @author dengtao
 * @date 2020/10/30 17:39
 * @since v1.0
 */
@RestController
@RequestMapping("/hadoop/reduce")
public class MapReduceController {
	@Autowired
    MapReduceService mapReduceService;

	/**
	 * 分组统计、排序
	 *
	 * @param jobName
	 * @param inputPath
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "groupSort", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> groupSort(@RequestParam("jobName") String jobName, @RequestParam("inputPath") String inputPath)
		throws Exception {
		if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
			return Result.failed("请求参数为空");
		}
		mapReduceService.groupSort(jobName, inputPath);
		return Result.succeed("分组统计、排序成功");
	}

}
