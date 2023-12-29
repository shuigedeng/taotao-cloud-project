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

package com.taotao.cloud.sys.biz.gobrs.task.condition;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.common.domain.AnyConditionResult;
import com.gobrs.async.core.common.domain.AnyConditionResult.AnyConditionResultBuilder;
import com.gobrs.async.core.common.domain.TaskResult;
import com.gobrs.async.core.task.AsyncTask;
import lombok.SneakyThrows;

/**
 * The type C service.
 *
 * @program: gobrs -async-starter @ClassName CService
 * @description: 任务依赖类型
 *     AServiceCondition,BServiceCondition,CServiceCondition->DServiceCondition:anyCondition
 *     <p>简化配置
 *     <p>A,B,C->D:anyCondition
 *     <p>D根据 A,B,C 返回的任务结果中的 AnyCondition 的state状态 进行判断是否继续执行 子任务
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Task
public class CServiceCondition extends AsyncTask<String, AnyConditionResult<String>> {

    /** The . */
    int i = 1;

    @SneakyThrows
    @Override
    public AnyConditionResult<String> task(String o, TaskSupport support) {
        AnyConditionResultBuilder<String> builder = AnyConditionResult.builder();

        LogUtils.info("CServiceCondition Begin");
        /** 获取 所依赖的父任务的结果 */
        String result = getResult(support, AServiceCondition.class, String.class);

        /** 获取自身任务的返回结果 这里获取 结果值为 null */
        TaskResult<AnyConditionResult<String>> tk = getTaskResult(support);

        /** 尝试获取 AServiceCondition 任务的返回结果 */
        TaskResult<String> taskResult = getTaskResult(support, AServiceCondition.class, String.class);
        /** 设置任务返回结果 */
        if (taskResult != null) {
            builder.result(taskResult.getResult());
        } else {
            builder.result("Mock CServiceCondition Result ");
        }

        Thread.sleep(2000);

        for (int i1 = 0; i1 < i; i1++) {
            i1 += i1;
        }
        LogUtils.info("CServiceCondition Finish");
        return builder.build();
    }

    @Override
    public void onSuccess(TaskSupport support) {
        /** 获取自身task 执行完成之后的结果 这里会拿到当前任务的返回结果 第二个参数是 anyCondition 类型 */
        AnyConditionResult<String> result = getResult(support, true);

        /** 获取 任务结果封装 包含执行状态 TaskResult 是任务执行结果的一个封装 */
        TaskResult<AnyConditionResult<String>> taskResult = getTaskResult(support);
    }
}
