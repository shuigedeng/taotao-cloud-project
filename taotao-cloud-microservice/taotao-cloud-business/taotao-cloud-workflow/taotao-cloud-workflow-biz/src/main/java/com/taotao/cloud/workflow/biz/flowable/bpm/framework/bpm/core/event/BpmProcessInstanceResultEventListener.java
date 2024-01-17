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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.bpm.core.event;

import com.taotao.cloud.flowable.biz.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import org.springframework.context.ApplicationListener;

/**
 * {@link BpmProcessInstanceResultEvent} 的监听器
 *
 * @author 芋道源码
 */
public abstract class BpmProcessInstanceResultEventListener
        implements ApplicationListener<BpmProcessInstanceResultEvent> {

    @Override
    public final void onApplicationEvent(BpmProcessInstanceResultEvent event) {
        if (!StrUtil.equals(event.getProcessDefinitionKey(), getProcessDefinitionKey())) {
            return;
        }
        onEvent(event);
    }

    /**
     * @return 返回监听的流程定义 Key
     */
    protected abstract String getProcessDefinitionKey();

    /**
     * 处理事件
     *
     * @param event 事件
     */
    protected abstract void onEvent(BpmProcessInstanceResultEvent event);
}
