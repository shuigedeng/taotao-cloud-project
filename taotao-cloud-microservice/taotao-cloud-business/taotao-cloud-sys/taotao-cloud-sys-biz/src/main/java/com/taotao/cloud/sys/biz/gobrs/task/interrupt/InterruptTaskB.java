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

package com.taotao.cloud.sys.biz.gobrs.task.interrupt;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;

/**
 * The type D service.
 *
 * @program: gobrs -async-starter @ClassName DService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Task
public class InterruptTaskB extends AsyncTask<Object, Object> {

    /** The . */
    int i = 10000;

    @Override
    public void prepare(Object o) {}

    @Override
    public Object task(Object o, TaskSupport support) {
        try {
            LogUtils.info("InterruptTaskB Begin");
            Thread.sleep(200);
            for (int i1 = 0; i1 < i; i1++) {
                i1 += i1;
            }
            LogUtils.info("",1 / 0);
            LogUtils.info("InterruptTaskB Finish");

        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return null;
    }

    @Override
    public boolean necessary(Object o, TaskSupport support) {
        return true;
    }

    @Override
    public void onSuccess(TaskSupport support) {}
}
