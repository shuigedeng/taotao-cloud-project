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

package com.taotao.cloud.wechat.biz.weixin.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.joolun.weixin.entity.ThirdSession;
import lombok.experimental.UtilityClass;

/**
 * @author thirdSession工具类
 */
@UtilityClass
public class ThirdSessionHolder {

    private final ThreadLocal<ThirdSession> THREAD_LOCAL_THIRD_SESSION = new TransmittableThreadLocal<>();

    /**
     * TTL 设置thirdSession
     *
     * @param thirdSession
     */
    public void setThirdSession(ThirdSession thirdSession) {
        THREAD_LOCAL_THIRD_SESSION.set(thirdSession);
    }

    /**
     * 获取TTL中的thirdSession
     *
     * @return
     */
    public ThirdSession getThirdSession() {
        return THREAD_LOCAL_THIRD_SESSION.get();
    }

    public void clear() {
        THREAD_LOCAL_THIRD_SESSION.remove();
    }

    /**
     * 获取用户商城ID
     *
     * @return
     */
    public String getWxUserId() {
        return THREAD_LOCAL_THIRD_SESSION.get().getWxUserId();
    }
}
