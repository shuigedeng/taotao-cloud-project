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
package com.taotao.cloud.quartz.test;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * QuartzComanndRunner
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-03 16:39
 */
public class QuartzComanndRunner implements CommandLineRunner {
@Autowired
private QuartzService quartzService;

	@Override
	public void run(String... args) throws Exception {
		HashMap<String,Object> map = new HashMap<>();
		map.put("name",1);
		quartzService.deleteJob("job", "test");
		quartzService.addJob(Job.class, "job", "test", "0 * * * * ?", map);

		map.put("name",2);
		quartzService.deleteJob("job2", "test");
		quartzService.addJob(Job.class, "job2", "test", "10 * * * * ?", map);

		map.put("name",3);
		quartzService.deleteJob("job3", "test2");
		quartzService.addJob(Job.class, "job3", "test2", "15 * * * * ?", map);
	}
}
