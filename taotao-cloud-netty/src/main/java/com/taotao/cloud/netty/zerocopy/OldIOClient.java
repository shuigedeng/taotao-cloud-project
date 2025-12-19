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

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * OldIOClient
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class OldIOClient {

    public static void main( String[] args ) throws Exception {
        Socket socket = new Socket("localhost", 8899);

        String fileName = "/Users/zhanglong/Desktop/spark-2.2.0-bin-hadoop2.7.tgz";
        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while (( readCount = inputStream.read(buffer) ) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println(
                "发送总字节数： " + total + ", 耗时： " + ( System.currentTimeMillis() - startTime ));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
