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

package com.taotao.cloud.netty.itcast.netty.c5;

import java.io.*;
import java.net.Socket;

/**
 * TestClient
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class TestClient {

    public static void main( String[] args ) throws IOException {
        Socket s = new Socket("localhost", 8888);

        new Thread(
                () -> {
                    try {
                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(s.getInputStream()));
                        while (true) {
                            System.out.println(reader.readLine());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .start();

        new Thread(
                () -> {
                    try {
                        BufferedWriter writer =
                                new BufferedWriter(
                                        new OutputStreamWriter(s.getOutputStream()));
                        for (int i = 0; i < 100; i++) {
                            writer.write(String.valueOf(i));
                            writer.newLine();
                            writer.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .start();
    }
}
