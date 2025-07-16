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

package com.taotao.cloud.auth.biz.metadata.event;

import com.taotao.boot.security.spring.event.LocalApplicationEvent;
import com.taotao.cloud.auth.biz.strategy.user.SysAttribute;
import java.time.Clock;

/**
 * <p>SysSecurityAttribute实体数据变更事件 </p>
 *
 */
public class SysAttributeChangeEvent extends LocalApplicationEvent<SysAttribute> {

    public SysAttributeChangeEvent(SysAttribute data) {
        super(data);
    }

    public SysAttributeChangeEvent(SysAttribute data, Clock clock) {
        super(data, clock);
    }
}
