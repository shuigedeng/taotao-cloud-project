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

package com.taotao.cloud.message.biz.austin.handler.script;

import jakarta.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * sms发送脚本的抽象类
 *
 * @author 3y
 */
@Slf4j
public abstract class BaseSmsScript implements SmsScript {

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @PostConstruct
    public void registerProcessScript() {
        if (ArrayUtils.isEmpty(this.getClass().getAnnotations())) {
            log.error("BaseSmsScript can not find annotation!");
            return;
        }
        Annotation handlerAnnotations = null;
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof SmsScriptHandler) {
                handlerAnnotations = annotation;
                break;
            }
        }
        if (handlerAnnotations == null) {
            log.error("handler annotations not declared");
            return;
        }
        // 注册handler
        smsScriptHolder.putHandler(((SmsScriptHandler) handlerAnnotations).value(), this);
    }
}
