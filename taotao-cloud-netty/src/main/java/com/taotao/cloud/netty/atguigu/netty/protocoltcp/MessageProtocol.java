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

package com.taotao.cloud.netty.atguigu.netty.protocoltcp;

// 协议包
/**
 * MessageProtocol
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class MessageProtocol {

    private int len; // 关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen( int len ) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent( byte[] content ) {
        this.content = content;
    }
}
