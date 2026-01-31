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

package com.taotao.cloud.netty.itcast.nio.c3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestFilesCopy
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class TestFilesCopy {

    public static void main( String[] args ) throws IOException {
        long start = System.currentTimeMillis();
        String source = "D:\\Snipaste-1.16.2-x64";
        String target = "D:\\Snipaste-1.16.2-x64aaa";

        Files.walk(Paths.get(source))
                .forEach(
                        path -> {
                            try {
                                String targetName = path.toString().replace(source, target);
                                // 是目录
                                if (Files.isDirectory(path)) {
                                    Files.createDirectory(Paths.get(targetName));
                                }
                                // 是普通文件
                                else if (Files.isRegularFile(path)) {
                                    Files.copy(path, Paths.get(targetName));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
