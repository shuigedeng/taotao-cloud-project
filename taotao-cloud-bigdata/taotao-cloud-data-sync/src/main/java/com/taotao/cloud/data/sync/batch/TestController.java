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

package com.taotao.cloud.data.sync.batch;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
@RestController
public class TestController {

    @Autowired JobLauncher jobLauncher;

    // @Autowired
    // Job myJob;
    //
    // @GetMapping("testJob")
    // public void testJob()
    //	throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
    // JobRestartException,
    // JobInstanceAlreadyCompleteException {
    //	//    后置参数：使用JobParameters中绑定参数 addLong  addString 等方法
    //	JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
    //	jobLauncher.run(myJob, jobParameters);
    //
    // }

    // @Autowired
    // Job mybatisJob;
    //
    // @GetMapping("testJobNew")
    // public void testJobNew(@RequestParam("authorId") String authorId)
    //	throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
    // JobRestartException,
    // JobInstanceAlreadyCompleteException {
    //	JobParameters jobParametersNew = new JobParametersBuilder()
    //		.addLong("timeNew", System.currentTimeMillis())
    //		.addString("authorId", authorId)
    //		.toJobParameters();
    //	jobLauncher.run(mybatisJob, jobParametersNew);
    //
    // }

}
