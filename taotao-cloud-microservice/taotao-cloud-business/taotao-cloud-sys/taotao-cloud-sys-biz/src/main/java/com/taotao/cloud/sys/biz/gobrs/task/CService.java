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

package com.taotao.cloud.sys.biz.gobrs.task;

import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.common.domain.TaskResult;
import com.gobrs.async.core.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.utils.log.LogUtils;
/**
 * The type C service.
 *
 * @program: gobrs -async-starter @ClassName CService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Slf4j
@Task
public class CService extends AsyncTask<String, Integer> {

    /** The . */
    int i = 10000;

    @Override
    public void prepare(String o) {
        log.info(this.getName() + " 使用线程---" + Thread.currentThread().getName());
    }

    @Override
    public Integer task(String o, TaskSupport support) {
        try {
            LogUtils.info("CService Begin");
            // 获取 所依赖的父任务的结果
            Integer rt = getResult(support);
            String result = getResult(support, AService.class, String.class);
            TaskResult<Integer> tk = getTaskResult(support);
            TaskResult<String> taskResult = getTaskResult(support, AService.class, String.class);
            Thread.sleep(300);
            for (int i1 = 0; i1 < i; i1++) {
                i1 += i1;
            }

            LogUtils.info("CService Finish");
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return null;
    }

    @Override
    public boolean necessary(String o, TaskSupport support) {
        return true;
    }

    @Override
    public void onSuccess(TaskSupport support) {
        // 获取自身task 执行完成之后的结果
        Integer result = getResult(support);

        // 获取 任务结果封装 包含执行状态
        TaskResult<Integer> taskResult = getTaskResult(support);
    }
}
