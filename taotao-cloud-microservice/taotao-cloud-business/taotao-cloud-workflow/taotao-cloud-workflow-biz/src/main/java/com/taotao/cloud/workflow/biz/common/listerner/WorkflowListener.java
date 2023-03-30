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

package com.taotao.cloud.workflow.biz.common.listerner;

import com.taotao.cloud.workflow.biz.common.config.ConfigValueUtil;
import com.taotao.cloud.workflow.biz.common.util.RedisUtil;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/** */
public class WorkflowListener implements ApplicationListener<ContextRefreshedEvent> {

    private ConfigValueUtil configValueUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        configValueUtil = SpringContext.getBean(ConfigValueUtil.class);
        if ("false".equals(configValueUtil.getTestVersion())) {
            RedisUtil redisUtil = SpringContext.getBean(RedisUtil.class);
            redisUtil.removeAll();
        }
    }
}
