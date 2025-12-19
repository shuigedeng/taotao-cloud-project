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

package com.taotao.cloud.netty.atguigu.nio;

import java.nio.IntBuffer;

/**
 * BasicBuffer
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class BasicBuffer {

    public static void main( String[] args ) {

        // 举例说明Buffer 的使用 (简单说明)
        // 创建一个Buffer, 大小为 5, 即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向buffer 存放数据
        //        intBuffer.put(10);
        //        intBuffer.put(11);
        //        intBuffer.put(12);
        //        intBuffer.put(13);
        //        intBuffer.put(14);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 如何从buffer读取数据
        // 将buffer转换，读写切换(!!!)
        /*
            public final Buffer flip() {
            limit = position; //读数据不能超过5
            position = 0;
            mark = -1;
            return this;
        }
             */
        intBuffer.flip();
        intBuffer.position(1); // 1,2
        System.out.println(intBuffer.get());
        intBuffer.limit(3);
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
