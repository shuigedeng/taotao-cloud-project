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

package com.taotao.cloud.rpc.common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class ReflectUtil {

    /**
     * fix: 修复 Jar 包启动，注解扫描失败的问题 这里如果使用 springboot 启动的话，有两种启动情况 1. 项目启动方式：那么堆栈信息最后堆栈类为 启动类，本项目堆栈信息
     * stack info: cn.fyupeng.util.ReflectUtil.getStackTrace(ReflectUtil.java:31) stack info:
     * cn.fyupeng.net.AbstractRpcServer.scanServices(AbstractRpcServer.java:36) stack info:
     * cn.fyupeng.net.netty.server.NettyServer.<init>(NettyServer.java:46) stack info:
     * cn.fyupeng.UserServer.run(UserServer.java:42) stack info:
     * org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:813) stack info:
     * org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:797) stack
     * info: org.springframework.boot.SpringApplication.run(SpringApplication.java:324) stack info:
     * org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) stack info:
     * org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) stack info:
     * cn.fyupeng.UserServer.main(UserServer.java:35)
     * <p>
     * 2. Jar启动方式：是先按照第一种方式启动，然后本地方法反射，最后还是在 springboot 的 JarLauncher 启动器上启动 stack info:
     * cn.fyupeng.util.ReflectUtil.getStackTrace(ReflectUtil.java:31) stack info:
     * cn.fyupeng.net.AbstractRpcServer.scanServices(AbstractRpcServer.java:36) stack info:
     * cn.fyupeng.net.netty.server.NettyServer.<init>(NettyServer.java:46) stack info:
     * .UserServer.run(UserServer.java:42) stack info:
     * org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:813) stack info:
     * org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:797) stack
     * info: org.springframework.boot.SpringApplication.run(SpringApplication.java:324) stack info:
     * org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) stack info:
     * org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) stack info:
     * cn.fyupeng.UserServer.main(UserServer.java:35) stack info:
     * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) stack info:
     * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) stack info:
     * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) stack
     * info: java.lang.reflect.Method.invoke(Method.java:498) stack info:
     * org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48) stack info:
     * org.springframework.boot.loader.Launcher.launch(Launcher.java:87) stack info:
     * org.springframework.boot.loader.Launcher.launch(Launcher.java:50) stack info:
     * org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:51)
     *
     * @return
     */
    public static String getStackTrace() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        Stack<StackTraceElement> newStack = new Stack<>();
        for (int index = 0; index < stack.length; index++) {
            if (!stack[index].getClassName().startsWith("java.lang.reflect")
                    && !stack[index].getClassName().startsWith("sun.reflect")
                    && !stack[index].getClassName().startsWith("org.springframework.boot")
                    && !stack[index].getClassName().startsWith("jdk.internal.reflect")) {
                newStack.push(stack[index]);
            }
            log.trace("stack info: {}", stack[index]);
        }
        // Jar 启动会 出现 注解扫描失败
        // return stack[stack.length - 1].getClassName();
        return newStack.pop().getClassName();
    }

    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classSet = new LinkedHashSet<>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                // 获取 下一个元素
                URL url = dirs.nextElement();
                // 得到 协议名称
                String protocol = url.getProtocol();
                // 如果 以 文件 形式保存 在 服务器上
                if ("file".equals(protocol)) {
                    // 获取包的 物理路径
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classSet);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 以 / 开头
                            if (name.charAt(0) == '/') {
                                // 获取 后面字符串
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    // 获取包名, 并 把 / 改为 .
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //
                                if (idx != -1 || recursive) {
                                    // 如果 是 .class 文件，而且不是 目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的 .class 获取真正的 类名
                                        String className =
                                                name.substring(
                                                        packageName.length() + 1,
                                                        name.length() - 6);
                                        try {
                                            // 添加到 class 集合中
                                            classSet.add(
                                                    Class.forName(packageName + "." + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    private static void findAndAddClassesInPackageByFile(
            String packageName,
            String packagePath,
            final boolean recursive,
            Set<Class<?>> classSet) {
        // 获取 此包的 目录，建立 一个 File
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles =
                dir.listFiles(
                        new FileFilter() {
                            @Override
                            public boolean accept(File file) {
                                return (recursive && file.isDirectory()
                                        || (file.getName().endsWith(".class")));
                            }
                        });
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(
                        packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classSet);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classSet.add(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .loadClass(packageName + "." + className));
                } catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                    log.warn("ClassNotFoundWarning: {}", e.getMessage());
                }
            }
        }
    }
}
