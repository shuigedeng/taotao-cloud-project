/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.log.event;

import com.taotao.cloud.log.model.RequestLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 *
 * @author dengtao
 * @date 2020/6/3 13:33
 * @since v1.0
 */
public class RequestLogEvent extends ApplicationEvent {

    public RequestLogEvent(RequestLog requestLog) {
        super(requestLog);
    }
}
