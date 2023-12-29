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

package com.taotao.cloud.sys.biz.gobrs.task.sence.casefour;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;

/**
 * @program: gobrs-async @ClassName CaseOneTaskC
 * @description:
 * @author: sizegang
 * @create: 2022-10-31
 */
@Task
public class CaseFourTaskC extends AsyncTask {

    @Override
    public Object task(Object o, TaskSupport support) {
        LogUtils.info("C任务执行");
        return "CResult";
    }
}
