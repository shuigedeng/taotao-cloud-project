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
import com.gobrs.async.core.task.AsyncTask;

/**
 * The type A service.
 *
 * @program: gobrs -async-starter @ClassName AService
 * @description: 任务依赖类型
 *     AServiceCondition,BServiceCondition,CServiceCondition->DServiceCondition:anyCondition
 *     <p>简化配置
 *     <p>A,B,C->D:anyCondition
 *     <p>D根据 A,B,C 返回的任务结果中的 AnyCondition 的state状态 进行判断是否继续执行 子任务
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Task(failSubExec = true)
public class AServiceCondition extends AsyncTask {

    /** The . */
    int sums = 10000;

    @Override
    public AnyConditionResult<Boolean> task(Object o, TaskSupport support) {
        AnyConditionResultBuilder<Boolean> builder = AnyConditionResult.builder();
        try {
            LogUtils.info("AServiceCondition Begin");
            Thread.sleep(300);
            for (int i1 = 0; i1 < sums; i1++) {
                i1 += i1;
            }
            LogUtils.info("AServiceCondition Finish");
        } catch (InterruptedException e) {
            LogUtils.error(e);
            return builder.result(false).build();
        }
        return builder.result(true).build();
    }
}
