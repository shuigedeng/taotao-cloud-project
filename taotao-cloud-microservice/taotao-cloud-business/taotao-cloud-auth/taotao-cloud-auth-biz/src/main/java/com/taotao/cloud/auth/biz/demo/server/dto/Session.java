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

package com.taotao.cloud.auth.biz.demo.server.dto;

import cn.herodotus.engine.assistant.core.definition.domain.AbstractDto;
import com.google.common.base.MoreObjects;

/**
 * Description: Session响应实体
 *
 * @author : gengwei.zheng
 * @date : 2021/10/2 11:42
 */
public class Session extends AbstractDto {

    /** 前端未登录时，唯一身份标识。如果由前端生成，则直接返回；如果由后端生成，则返回后端生成值 */
    private String sessionId;

    /** 后台RSA公钥 */
    private String publicKey;

    /** 本系统授权码模式校验参数 */
    private String state;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sessionId", sessionId)
                .add("publicKey", publicKey)
                .add("state", state)
                .toString();
    }
}
