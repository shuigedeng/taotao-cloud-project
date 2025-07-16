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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
@RestController
public class BatchController {

    @Autowired private Job singleStepJob;

    @Autowired private Job multiBoundStepsJob;

    @Autowired private JobLauncher jobLauncher;

    @GetMapping("/job/step")
    public String invokeStep() throws Exception {
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

        jobLauncher.run(singleStepJob, jobParameters);
        return "The job is proceed.";
    }

    @GetMapping("/job/steps")
    public String invokeSteps() throws Exception {
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
        jobLauncher.run(multiBoundStepsJob, jobParameters);
        return "The multi bound steps job is proceed.";
    }
}
