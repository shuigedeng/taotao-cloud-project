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
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TestFilesWalkFileTree
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class TestFilesWalkFileTree {

    public static void main( String[] args ) throws IOException {
        //        Files.delete(Paths.get("D:\\Snipaste-1.16.2-x64 - 副本"));
        Files.walkFileTree(
                Paths.get("D:\\Snipaste-1.16.2-x64 - 副本"),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs )
                            throws IOException {
                        Files.delete(file);
                        return super.visitFile(file, attrs);
                    }

                    @Override
                    public FileVisitResult postVisitDirectory( Path dir, IOException exc )
                            throws IOException {
                        Files.delete(dir);
                        return super.postVisitDirectory(dir, exc);
                    }
                });
    }

    private static void m2() throws IOException {
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(
                Paths.get("C:\\Program Files\\Java\\jdk1.8.0_91"),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs )
                            throws IOException {
                        if (file.toString().endsWith(".jar")) {
                            System.out.println(file);
                            jarCount.incrementAndGet();
                        }
                        return super.visitFile(file, attrs);
                    }
                });
        System.out.println("jar count:" + jarCount);
    }

    private static void m1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(
                Paths.get("C:\\Program Files\\Java\\jdk1.8.0_91"),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs )
                            throws IOException {
                        System.out.println("====>" + dir);
                        dirCount.incrementAndGet();
                        return super.preVisitDirectory(dir, attrs);
                    }

                    @Override
                    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs )
                            throws IOException {
                        System.out.println(file);
                        fileCount.incrementAndGet();
                        return super.visitFile(file, attrs);
                    }
                });
        System.out.println("dir count:" + dirCount);
        System.out.println("file count:" + fileCount);
    }
}
