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

package com.taotao.cloud.netty.zerocopy;

import java.util.Map;

/**
 * NewIOClient
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class NewIOClient {

    public static void main( String[] args ) throws Exception {
        //        SocketChannel socketChannel = SocketChannel.open();
        //        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        //        socketChannel.configureBlocking(true);
        //
        //        String fileName = "/Users/zhanglong/Desktop/spark-2.2.0-bin-hadoop2.7.tgz";
        //
        //        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //
        //        long startTime = System.currentTimeMillis();
        //
        //        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        //
        //        System.out.println("发送总字节数：" + transferCount + "，耗时： " +
        // (System.currentTimeMillis() - startTime));
        //
        //        fileChannel.close();

        System.out.println("output environment var:");
        for (Map.Entry entry : System.getenv().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
