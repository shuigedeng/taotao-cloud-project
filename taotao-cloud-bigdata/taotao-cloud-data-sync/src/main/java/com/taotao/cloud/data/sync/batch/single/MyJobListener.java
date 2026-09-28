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

package com.taotao.cloud.data.sync.batch.single;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :监听Job执行情况，实现JobExecutorListener，且在batch配置类里，Job的Bean上绑定该监听器
 **/
public class MyJobListener implements JobExecutionListener {

    private Logger logger = LoggerFactory.getLogger(MyJobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("job 开始, id={}", jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("job 结束, id={}", jobExecution.getJobId());
    }
}
