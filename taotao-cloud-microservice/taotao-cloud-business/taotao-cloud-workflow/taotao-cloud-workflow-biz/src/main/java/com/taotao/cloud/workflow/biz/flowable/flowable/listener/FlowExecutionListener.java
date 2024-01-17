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

package com.taotao.cloud.workflow.biz.flowable.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 执行监听器
 *
 * <p>执行监听器允许在执行过程中执行Java代码。 执行监听器可以捕获事件的类型： 流程实例启动，结束 输出流捕获 获取启动，结束 路由开始，结束 中间事件开始，结束 触发开始事件，触发结束事件
 *
 * @author Tony
 * @since 2022/12/16
 */
@Slf4j
@Component
public class FlowExecutionListener implements ExecutionListener {
    /** 流程设计器添加的参数 */
    private Expression param;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("执行监听器:{}", execution);
    }
}
