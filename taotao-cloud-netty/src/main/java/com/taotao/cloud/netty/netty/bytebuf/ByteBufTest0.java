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

package com.taotao.cloud.netty.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBufTest0
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ByteBufTest0 {

    public static void main( String[] args ) {
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; ++i) {
            buffer.writeByte(i);
        }

        //        for (int i = 0; i < buffer.capacity(); ++i) {
        //            System.out.println(buffer.getByte(i));
        //        }

        for (int i = 0; i < buffer.capacity(); ++i) {
            System.out.println(buffer.readByte());
        }
    }
}
