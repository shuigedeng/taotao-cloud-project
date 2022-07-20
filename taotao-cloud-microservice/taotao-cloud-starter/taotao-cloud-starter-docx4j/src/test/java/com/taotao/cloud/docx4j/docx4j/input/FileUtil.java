package com.taotao.cloud.docx4j.docx4j.input;

import java.util.Optional;

/**
 * 文件工具类
 */
public interface FileUtil {
    /**
     * 获得classpath的根目录
     * @param path  相对根目录路径
     * @param clazz {@link Class}
     * @return classpath根目录
     */
    static String rootPath(Class<?> clazz, String path) {
        return clazz.getResource(path).getPath();
    }

    /**
     * 获得class的绝对路径
     * @param clazz {@link Class}
     * @return class路径
     */
    static String classPath(Class<?> clazz) {
        return clazz.getResource("").getPath();
    }

    /**
     * 获得相对class的兄弟文件
     * @param clazz    {@link Class}
     * @param fileName 文件名
     * @return 文件路径
     */
    static String brotherPath(Class<?> clazz, String fileName) {
        return classPath(clazz) + fileName;
    }

    /**
     * 获得文件后缀名
     * @param fileName 文件名
     * @return 后缀名
     */
    static String suffix(String fileName) {
        return
            Optional.ofNullable(fileName)
                .filter(it -> it.contains("."))
                .map(it -> it.substring(it.lastIndexOf(".") + 1))
                .orElse("");
    }
}
