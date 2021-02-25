/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.exception.BaseException;
import lombok.experimental.UtilityClass;

import java.io.*;

/**
 * FileUtils
 *
 * @author dengtao
 * @date 2020/6/2 16:36
 * @since v1.0
 */
@UtilityClass
public class FileUtil {

    /**
     * 文件是否存在
     *
     * @param filepath 文件路径
     * @return boolean
     * @author dengtao
     * @date 2020/10/15 15:00
     * @since v1.0
     */
    public boolean existFile(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /**
     * 获取文件目录路径
     *
     * @param path 文件路径
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 15:01
     * @since v1.0
     */
    public String getDirectoryPath(String path) {
        File file = new File(path);
        return file.getAbsolutePath();
    }

    /**
     * 获取文件目录路径
     *
     * @param cls 类型
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 15:01
     * @since v1.0
     */
    public String getDirectoryPath(Class<?> cls) {
        File file = getJarFile(cls);
        if (file == null) {
            return null;
        }
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取文件
     *
     * @param cls 类型
     * @return java.io.File
     * @author dengtao
     * @date 2020/10/15 15:02
     * @since v1.0
     */
    public File getJarFile(Class<?> cls) {
        String path = cls.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            // 转换处理中文及空格
            path = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return new File(path);
    }

    // /**
    //  * 获取文件路径
    //  *
    //  * @param paths 路径列表
    //  * @return java.lang.String
    //  * @author dengtao
    //  * @date 2020/10/15 15:03
    //  * @since v1.0
    //  */
    // public String getFilePath(String... paths) {
    //     StringBuilder sb = new StringBuilder();
    //     for (String path : paths) {
    //         sb.append(org.springframework.util.StringUtils.trimTrailingCharacter(path, File.separatorChar));
    //         sb.append(File.separator);
    //     }
    //     return org.springframework.util.StringUtils.trimTrailingCharacter(sb.toString(), File.separatorChar);
    // }

    /**
     * 创建目录
     *
     * @param path 文件路径
     * @return void
     * @author dengtao
     * @date 2020/10/15 15:03
     * @since v1.0
     */
    public Boolean createDirectory(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return false;
        }
    }

    /**
     * 追加文件内容
     *
     * @param path     文件路径
     * @param contents 内容
     * @return void
     * @author dengtao
     * @date 2020/10/15 15:04
     * @since v1.0
     */
    public static void appendAllText(String path, String contents) {
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(path);
            try (FileWriter fw = new FileWriter(f, true)) {
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.println(contents);
                    pw.flush();
                    fw.flush();
                }
            }
        } catch (IOException exp) {
            throw new BaseException("追加文件异常", exp);
        }

    }

    /**
     * 写文件内容
     *
     * @param path     文件路径
     * @param contents 内容
     * @return void
     * @author dengtao
     * @date 2020/10/15 15:05
     * @since v1.0
     */
    public void writeAllText(String path, String contents) {
        try {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }

            if (f.createNewFile()) {
                try (BufferedWriter output = new BufferedWriter(new FileWriter(f))) {
                    output.write(contents);
                }
            }
        } catch (IOException exp) {
            throw new BaseException("写文件异常", exp);
        }
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 15:07
     * @since v1.0
     */
    public String readAllText(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                //获取文件长度
                long fileLength = f.length();
                byte[] fileContent = new byte[(int) fileLength];
                try (FileInputStream in = new FileInputStream(f)) {
                    in.read(fileContent);
                }
                //返回文件内容,默认编码
                return new String(fileContent);
            } else {
                throw new FileNotFoundException(path);
            }
        } catch (IOException exp) {
            throw new BaseException("读文件异常", exp);
        }
    }

    /**
     * 获取行分隔符
     *
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 15:08
     * @since v1.0
     */
    public String lineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * 根据文件路径获取文件名
     *
     * @param filePath 文件路径
     * @return java.lang.String 文件名
     * @author dengtao
     * @date 2020/10/15 15:08
     * @since v1.0
     */
    public String getFileName(String filePath) {
        String path = filePath.replaceAll("\\\\", "/");
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }
}
