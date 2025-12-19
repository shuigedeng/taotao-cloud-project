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

package com.taotao.cloud.netty.itcast.nio.c2;

import static com.taotao.cloud.netty.itcast.nio.c2.ByteBufferUtil.debugAll;

import java.nio.ByteBuffer;

/**
 * TestByteBufferRead
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class TestByteBufferRead {

    public static void main( String[] args ) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读
        /*buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
        System.out.println((char)buffer.get());*/

        // mark & reset
        // mark 做一个标记，记录 position 位置， reset 是将 position 重置到 mark 的位置
        /*System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark(); // 加标记，索引2 的位置
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset(); // 将 position 重置到索引 2
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());*/

        // get(i) 不会改变读索引的位置
        System.out.println((char) buffer.get(3));
        debugAll(buffer);
    }
}
