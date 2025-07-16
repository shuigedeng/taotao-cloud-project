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

package com.taotao.cloud.generator.maku.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目名变更 工具类
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class ProjectUtils {
    /**
     * 需要变更的文件后缀
     */
    public static final String MODIFY_SUFFIX = "java,xml,yml,factories,md,txt";

    /**
     * 排除的文件
     */
    public static final String EXCLUSIONS = ".git,.idea,target,logs";

    /**
     * 分隔符
     */
    public static final String SPLIT = ",";

    /**
     * 拷贝目录文件
     *
     * @param srcRoot    原文件
     * @param destRoot   目标文件
     * @param exclusions 排除文件
     * @param replaceMap 替换规则
     */
    public static void copyDirectory(
            File srcRoot, File destRoot, List<String> exclusions, Map<String, String> replaceMap)
            throws IOException {
        String destPath = destRoot.getPath().replaceAll("\\\\", "/");
        destRoot = new File(replaceData(destPath, replaceMap));

        // 获取排除后的源文件
        File[] srcFiles =
                CollectionUtil.isEmpty(exclusions)
                        ? srcRoot.listFiles()
                        : srcRoot.listFiles(file -> !exclusions.contains(file.getName()));

        if (srcFiles == null) {
            throw new IOException("没有需要拷贝的文件 " + srcRoot);
        }

        for (File srcFile : srcFiles) {
            String fileName = srcFile.getName();
            if (srcFile.isFile()) {
                fileName = replaceData(fileName, replaceMap);
            }
            File destFile = new File(destRoot, fileName);
            if (srcFile.isDirectory()) {
                copyDirectory(srcFile, destFile, exclusions, replaceMap);
            } else {
                FileUtil.copyFile(srcFile, destFile);
            }
        }
    }

    /**
     * 内容格式化
     *
     * @param rootFile   文件根目录
     * @param suffixList 需要格式化的文件后缀
     * @param replaceMap 替换规则
     */
    public static void contentFormat(
            File rootFile, List<String> suffixList, Map<String, String> replaceMap) {
        List<File> destList =
                FileUtil.loopFiles(
                        rootFile, file -> suffixList.contains(FileNameUtil.getSuffix(file)));

        for (File dest : destList) {
            List<String> lines = FileUtil.readUtf8Lines(dest);
            List<String> newList = new ArrayList<>();

            for (String line : lines) {
                newList.add(replaceData(line, replaceMap));
            }

            FileUtil.writeUtf8Lines(newList, dest);
        }
    }

    /**
     * 生成临时路径
     */
    public static String getTmpDirPath(String... names) {
        StringBuilder tmpPath = new StringBuilder(FileUtil.getTmpDirPath());
        tmpPath.append("generator");
        tmpPath.append(File.separator);
        tmpPath.append(System.currentTimeMillis());

        for (String name : names) {
            tmpPath.append(File.separator).append(name);
        }

        return tmpPath.toString();
    }

    /**
     * 替换数据
     *
     * @param str 待替换的字符串
     * @param map 替换的kv集合
     * @return 返回替换后的数据
     */
    private static String replaceData(String str, Map<String, String> map) {
        for (String key : map.keySet()) {
            str = str.replaceAll(key, map.get(key));
        }
        return str;
    }
}
