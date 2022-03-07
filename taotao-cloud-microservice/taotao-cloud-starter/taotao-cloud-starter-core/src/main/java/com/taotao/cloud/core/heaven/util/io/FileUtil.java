/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.io;


import com.taotao.cloud.core.heaven.constant.CharsetConst;
import com.taotao.cloud.core.heaven.constant.FileTypeConst;
import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import com.taotao.cloud.core.heaven.support.handler.IMapHandler;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import com.taotao.cloud.core.heaven.util.util.ArrayUtil;
import com.taotao.cloud.core.heaven.util.util.MapUtil;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.*;
import java.util.*;

/**
 * 文件工具类
 * 1. 用于获取文件的内容
 *
 * @author bbhou
 * @since 0.0.1
 */
public final class FileUtil {

    private FileUtil() {
    }

    /**
     * 获取文件内容
     *
     * @param filePath 文件路径
     * @return 文件不存在或异常等, 直接抛出异常
     * @since 0.1.80
     */
    public static String getFileContent(String filePath) {
        return getFileContent(filePath, CharsetConst.UTF8);
    }

    /**
     * 获取文件内容
     *
     * @param filePath 文件路径
     * @param charset  文件编码
     * @return 文件不存在或异常等, 直接抛出异常
     * @since 0.1.80
     */
    public static String getFileContent(String filePath, final String charset) {
        File file = new File(filePath);
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                return getFileContent(inputStream, charset);
            } catch (IOException e) {
                throw new CommonRuntimeException(e);
            }
        }
        throw new CommonRuntimeException(filePath + " is not exists!");
    }

    /**
     * 获取文件内容
     * 默认编码UTF8
     *
     * @param inputStream 输入流
     * @return 文件内容
     * @since 0.1.10
     */
    public static String getFileContent(InputStream inputStream) {
        return getFileContent(inputStream, CharsetConst.UTF8);
    }

    /**
     * 获取文件内容
     * 默认编码UTF8
     *
     * @param file    文件
     * @param charset 文件编码
     * @return 文件内容
     * @since 0.1.94
     */
    public static String getFileContent(final File file,
                                        final String charset) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return getFileContent(inputStream, charset);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取文件内容
     * 默认编码UTF8
     *
     * @param file 文件
     * @return 文件内容
     * @since 0.1.94
     */
    public static String getFileContent(final File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return getFileContent(inputStream);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取文件内容
     *
     * @param inputStream 文件输入流
     * @param charset     文件编码
     * @return 文件内容字符串
     * @since 0.1.10
     */
    public static String getFileContent(InputStream inputStream, String charset) {
        Charset charsetVal = Charset.forName(charset);
        return getFileContent(inputStream, 0, Integer.MAX_VALUE, charsetVal);
    }

    /**
     * 获取文件内容
     *
     * @param inputStream 输入流
     * @param startIndex  开始下标
     * @param endIndex    结束下标
     * @param charset     编码
     * @return 结果
     * @since 0.1.85
     */
    public static String getFileContent(final InputStream inputStream,
                                        int startIndex,
                                        int endIndex,
                                        final Charset charset) {
        try {
            // 参数纠正
            endIndex = Math.min(endIndex, inputStream.available());
            startIndex = Math.max(0, startIndex);

            // 跳过指定长度
            inputStream.skip(startIndex);

            // 这个读取的数据可能不正确
            // InputStream.read(byte[] b) 无法保证读取的结果正确。
            final int count = endIndex - startIndex;
            byte[] bytes = new byte[count];
            // 已经成功读取的字节的个数
            // -1 也代表结束
            int readCount = 0;
            while (readCount < count && readCount != -1) {
                readCount += inputStream.read(bytes, readCount, count - readCount);
            }

            return new String(bytes, charset);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名称
     * @return 文件后缀
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }


    /**
     * 获取指定路径文件的每一行内容
     *
     * @param filePath 文件路径
     * @param initLine 初始化行数
     * @return 内容列表
     */
    public static List<String> getFileContentEachLine(String filePath, int initLine) {
        File file = new File(filePath);
        return getFileContentEachLine(file, initLine);
    }

    /**
     * 获取指定路径文件的每一行内容
     * 1.初始化行数默认为0
     *
     * @param filePath 文件路径
     * @return 内容列表
     * @see #getFileContentEachLine(String, int) 获取指定路径文件的每一行内容
     */
    public static List<String> getFileContentEachLine(String filePath) {
        File file = new File(filePath);
        return getFileContentEachLine(file, 0);
    }


    /**
     * 获取指定文件的每一行内容。并对内容进行trim()操作。
     *
     * @param filePath 文件路径
     * @param initLine 初始化行数
     * @return 内容列表
     */
    public static List<String> getFileContentEachLineTrim(String filePath, int initLine) {
        List<String> stringList = getFileContentEachLine(filePath, initLine);
        List<String> resultList = new LinkedList<>();

        for (String string : stringList) {
            resultList.add(string.trim());
        }

        return resultList;
    }

    /**
     * 获取指定文件的每一行内容
     * 默认初始行数为0
     *
     * @param file 文件
     * @return 内容列表
     */
    public static List<String> getFileContentEachLine(File file) {
        return getFileContentEachLine(file, 0);
    }

    /**
     * 获取指定文件的每一行内容
     * [TWR](http://blog.csdn.net/doctor_who2004/article/details/50901195)
     *
     * @param file     指定读取文件
     * @param initLine 初始读取行数
     * @return 错误返回空列表
     * @since 1.7
     */
    public static List<String> getFileContentEachLine(File file, int initLine) {
        List<String> contentList = new LinkedList<>();

        if (!file.exists()) {
            return contentList;
        }

        //暂时使用此编码
        String charset = "UTF-8";
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            // 用于记录行号
            int lineNo = 0;
            while (lineNo < initLine) {
                lineNo++;
                String ignore = bufferedReader.readLine();
            }

            String dataEachLine;   //每一行的内容
            while ((dataEachLine = bufferedReader.readLine()) != null) {
                lineNo++;
                //跳过空白行
                if (Objects.equals("", dataEachLine)) {
                    continue;
                }
                contentList.add(dataEachLine);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }

        return contentList;
    }


    /**
     * 获取文件内容的列表
     *
     * @param file     文件
     * @param initLine 0 开始
     * @param endLine  下标从0开始
     * @param charset  编码
     * @return string list
     */
    @Deprecated
    public static List<String> getFileContentEachLine(final File file, final int initLine, final int endLine, final String charset) {
        List<String> contentList = new LinkedList<>();

        if (!file.exists()) {
            return contentList;
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            // 用于记录行号
            int lineNo = 0;
            while (lineNo < initLine) {
                lineNo++;
                String ignore = bufferedReader.readLine();
            }

            //每一行的内容
            String dataEachLine;
            while ((dataEachLine = bufferedReader.readLine()) != null
                    && lineNo < endLine) {
                lineNo++;
                contentList.add(dataEachLine);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }

        return contentList;
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param file        文件信息
     * @param charset     编码
     * @param initLine    初始化行
     * @param endLine     结束航
     * @param ignoreEmpty 是否跳过空白行
     * @return 结果列表
     * @since 0.1.65
     */
    public static List<String> readAllLines(final File file,
                                            final String charset,
                                            final int initLine,
                                            final int endLine,
                                            final boolean ignoreEmpty) {
        ArgUtil.notNull(file, "file");
        ArgUtil.notEmpty(charset, "charset");
        if (!file.exists()) {
            throw new CommonRuntimeException("File not exists!");
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            return readAllLines(inputStream, charset, initLine, endLine, ignoreEmpty);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param inputStream 文件输入流
     * @param charset     编码
     * @param initLine    初始化行
     * @param endLine     结束航
     * @param ignoreEmpty 是否跳过空白行
     * @return 结果列表
     * @since 0.1.95
     */
    public static List<String> readAllLines(final InputStream inputStream,
                                            final String charset,
                                            final int initLine,
                                            final int endLine,
                                            final boolean ignoreEmpty) {
        ArgUtil.notNull(inputStream, "inputStream");
        ArgUtil.notEmpty(charset, "charset");

        List<String> contentList = new LinkedList<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            // 用于记录行号
            int lineNo = 0;
            while (lineNo < initLine) {
                lineNo++;
                String ignore = bufferedReader.readLine();
            }

            //每一行的内容
            String dataEachLine;
            while ((dataEachLine = bufferedReader.readLine()) != null
                    && lineNo < endLine) {
                lineNo++;

                // 跳过空白行且内容为空，则不进行计入结果
                if (ignoreEmpty && StringUtil.isEmpty(dataEachLine)) {
                    // ignore
                    continue;
                } else {
                    contentList.add(dataEachLine);
                }
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return contentList;
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param inputStream 文件输入流
     * @param charset     编码
     * @param initLine    初始化行
     * @param endLine     结束航
     * @return 结果列表
     * @since 0.1.95
     */
    public static List<String> readAllLines(final InputStream inputStream,
                                            final String charset,
                                            final int initLine,
                                            final int endLine) {
        return readAllLines(inputStream, charset, initLine, endLine, true);
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param inputStream 文件输入流
     * @param charset     编码
     * @param initLine    初始化行
     * @return 结果列表
     * @since 0.1.95
     */
    public static List<String> readAllLines(final InputStream inputStream,
                                            final String charset,
                                            final int initLine) {
        return readAllLines(inputStream, charset, initLine, Integer.MAX_VALUE);
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param inputStream 文件输入流
     * @param charset     编码
     * @return 结果列表
     * @since 0.1.95
     */
    public static List<String> readAllLines(final InputStream inputStream,
                                            final String charset) {
        return readAllLines(inputStream, charset, 0);
    }

    /**
     * 获取每一行的文件内容
     * （1）如果文件不存在，直接返回空列表。
     *
     * @param inputStream 文件输入流
     * @return 结果列表
     * @since 0.1.95
     */
    public static List<String> readAllLines(final InputStream inputStream) {
        return readAllLines(inputStream, CharsetConst.UTF8);
    }

    /**
     * 获取每一行的文件内容
     *
     * @param filePath    文件路径
     * @param charset     文件编码
     * @param ignoreEmpty 是否跳过空白行
     * @return 结果列表
     * @since 0.1.65
     */
    public static List<String> readAllLines(final String filePath,
                                            final String charset,
                                            final boolean ignoreEmpty) {
        File file = new File(filePath);
        return readAllLines(file, charset, 0, Integer.MAX_VALUE, ignoreEmpty);
    }

    /**
     * 获取每一行的文件内容
     *
     * @param file        文件路径
     * @param charset     文件编码
     * @param ignoreEmpty 是否跳过空白行
     * @return 结果列表
     * @since 0.1.88
     */
    public static List<String> readAllLines(final File file,
                                            final String charset,
                                            final boolean ignoreEmpty) {
        return readAllLines(file, charset, 0, Integer.MAX_VALUE, ignoreEmpty);
    }

    /**
     * 获取每一行的文件内容
     *
     * @param file    文件路径
     * @param charset 文件编码
     * @return 结果列表
     * @since 0.1.88
     */
    public static List<String> readAllLines(final File file,
                                            final String charset) {
        return readAllLines(file, charset, false);
    }


    /**
     * 获取每一行的文件内容
     *
     * @param file 文件路径
     * @return 结果列表
     * @since 0.1.88
     */
    public static List<String> readAllLines(final File file) {
        return readAllLines(file, CharsetConst.UTF8);
    }

    /**
     * 获取每一行的文件内容
     *
     * @param filePath 文件路径
     * @param charset  文件编码
     * @return 结果列表
     * @since 0.1.65
     */
    public static List<String> readAllLines(final String filePath,
                                            final String charset) {
        return readAllLines(filePath, charset, false);
    }


    /**
     * 获取每一行的文件内容
     *
     * @param filePath 文件路径
     * @return 结果列表
     * @since 0.1.65
     */
    public static List<String> readAllLines(final String filePath) {
        return readAllLines(filePath, CharsetConst.UTF8);
    }

    /**
     * 复制文件夹
     *
     * @param sourceDir 原始文件夹
     * @param targetDir 目标文件夹
     * @throws IOException if any
     * @since 1.1.2
     */
    public static void copyDir(String sourceDir, String targetDir) throws IOException {
        File file = new File(sourceDir);
        String[] filePath = file.list();

        if (!(new File(targetDir)).exists()) {
            (new File(targetDir)).mkdir();
        }

        if (ArrayUtil.isNotEmpty(filePath)) {

            for (String aFilePath : filePath) {
                if ((new File(sourceDir + File.separator + aFilePath)).isDirectory()) {
                    copyDir(sourceDir + File.separator + aFilePath, targetDir + File.separator + aFilePath);
                }

                if (new File(sourceDir + File.separator + aFilePath).isFile()) {
                    copyFile(sourceDir + File.separator + aFilePath, targetDir + File.separator + aFilePath);
                }

            }

        }

    }


    /**
     * 复制文件
     *
     * @param sourceFile 原始路径
     * @param targetPath 目标路径
     * @throws IOException if any
     * @since 1.1.2
     */
    public static void copyFile(String sourceFile, String targetPath) throws IOException {
        File oldFile = new File(sourceFile);
        File file = new File(targetPath);

        try (FileInputStream in = new FileInputStream(oldFile);
             FileOutputStream out = new FileOutputStream(file)) {

            byte[] buffer = new byte[2097152];

            while ((in.read(buffer)) != -1) {
                out.write(buffer);
            }

        }

    }

    /**
     * 写入文件信息
     * （1）默认 utf-8 编码
     * （2）默认新建一个文件
     * （3）默认为一行
     *
     * @param filePath    文件路径
     * @param line        行信息
     * @param openOptions 操作属性
     * @since 0.1.78
     */
    public static void write(final String filePath, final CharSequence line, OpenOption... openOptions) {
        write(filePath, Collections.singletonList(line), openOptions);
    }

    /**
     * 写入文件信息
     * （1）默认 utf-8 编码
     * （2）默认新建一个文件
     *
     * @param filePath    文件路径
     * @param lines       行信息
     * @param openOptions 文件选项
     * @since 0.1.22
     */
    public static void write(final String filePath, final Iterable<? extends CharSequence> lines, OpenOption... openOptions) {
        write(filePath, lines, CharsetConst.UTF8, openOptions);
    }

    /**
     * 写入文件信息
     *
     * @param filePath    文件路径
     * @param lines       行信息
     * @param charset     文件编码
     * @param openOptions 文件操作选项
     * @since 0.1.22
     */
    public static void write(final String filePath, final Iterable<? extends CharSequence> lines,
                             final String charset, OpenOption... openOptions) {
        try {
            // ensure lines is not null before opening file
            ArgUtil.notNull(lines, "charSequences");
            CharsetEncoder encoder = Charset.forName(charset).newEncoder();
            final Path path = Paths.get(filePath);

            // 创建父类文件夹
            Path pathParent = path.getParent();
            // 路径判断空
            if(pathParent != null) {
                File parent = pathParent.toFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
            }

            OutputStream out = path.getFileSystem().provider().newOutputStream(path, openOptions);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
                for (CharSequence line : lines) {
                    writer.append(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 创建文件
     * （1）文件路径为空，则直接返回 false
     * （2）如果文件已经存在，则返回 true
     * （3）如果文件不存在，则创建文件夹，然后创建文件。
     * 3.1 如果父类文件夹创建失败，则直接返回 false.
     *
     * @param filePath 文件路径
     * @return 是否成功
     * @throws CommonRuntimeException 运行时异常，如果创建文件异常。包括的异常为 {@link IOException} 文件异常.
     * @since 0.1.24
     */
    public static boolean createFile(final String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }

        if (FileUtil.exists(filePath)) {
            return true;
        }

        File file = new File(filePath);

        // 父类文件夹的处理
        File dir = file.getParentFile();
        if (dir != null && FileUtil.notExists(dir)) {
            boolean mkdirResult = dir.mkdirs();
            if (!mkdirResult) {
                return false;
            }
        }
        // 创建文件
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }


    /**
     * 文件是否存在
     *
     * @param filePath 文件路径
     * @param options  连接选项
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean exists(final String filePath, LinkOption... options) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }

        Path path = Paths.get(filePath);
        return Files.exists(path, options);
    }

    /**
     * 文件是否不存在
     *
     * @param filePath 文件路径
     * @param options  连接选项
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean notExists(final String filePath, LinkOption... options) {
        return !exists(filePath, options);
    }


    /**
     * 文件是否不存在
     *
     * @param file 文件
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean notExists(final File file) {
        ArgUtil.notNull(file, "file");
        return !file.exists();
    }

    /**
     * 判断文件是否为空
     * （1）文件不存在，返回 true
     * （2）文件存在，且 {@link File#length()} 为0，则认为空。
     * （3）文件存在，且length大于0，则认为不空
     *
     * @param filePath 文件路径
     * @return 内容是否为空
     * @since 0.1.24
     */
    public static boolean isEmpty(final String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return true;
        }
        File file = new File(filePath);
        return file.length() <= 0;
    }

    /**
     * 内容是否为不空
     *
     * @param filePath 文件路径
     * @return 内容是否为不空
     * @since 0.1.24
     */
    public static boolean isNotEmpty(final String filePath) {
        return !isEmpty(filePath);
    }

    /**
     * 获取文件字节数组
     *
     * @param file 文件信息
     * @return 字节数组
     * @since 0.1.50
     */
    public static byte[] getFileBytes(final File file) {
        ArgUtil.notNull(file, "file");

        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取文件字节流
     *
     * @param filePath 文件路径
     * @return 字节数组
     * @since 0.1.50
     */
    public static byte[] getFileBytes(final String filePath) {
        ArgUtil.notNull(filePath, "filePath");

        File file = new File(filePath);
        return getFileBytes(file);
    }

    /**
     * 根据字节信息创建文件
     *
     * @param filePath 文件路径
     * @param bytes    字节数组
     * @see #createFileAssertSuccess 断言创建成功
     * @since 0.1.50
     */
    public static void createFile(final String filePath, final byte[] bytes) {
        File file = createFileAssertSuccess(filePath);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            bos.write(bytes);
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @return 文件信息
     * @throws CommonRuntimeException 运行时异常，如果创建文件异常。包括的异常为 {@link IOException} 文件异常.
     * @since 0.1.50
     */
    public static File createFileAssertSuccess(final String filePath) {
        ArgUtil.notEmpty(filePath, "filePath");

        // 判断文件是否存在
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        // 父类文件夹的处理
        File dir = file.getParentFile();
        if (FileUtil.notExists(dir)) {
            boolean mkdirResult = dir.mkdirs();
            if (!mkdirResult) {
                throw new CommonRuntimeException("Parent file create fail " + filePath);
            }
        }

        try {
            // 创建文件
            boolean createFile = file.createNewFile();
            if (!createFile) {
                throw new CommonRuntimeException("Create new file fail for path " + filePath);
            }
            return file;
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件信息
     * @since 0.1.50
     */
    public static void deleteFile(final File file) {
        ArgUtil.notNull(file, "file");

        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                throw new CommonRuntimeException("Delete file fail for path " + file.getAbsolutePath());
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件信息
     * @since 0.1.141
     */
    public static void deleteFile(final String filePath) {
        ArgUtil.notEmpty(filePath, "filePath");
        File file = new File(filePath);
        deleteFile(file);
    }

    /**
     * 创建临时文件
     *
     * @param name   文件名称
     * @param suffix 文件后缀
     * @return 临时文件
     * @since 0.1.50
     */
    public static File createTempFile(final String name, final String suffix) {
        try {
            ArgUtil.notEmpty(name, "prefix");
            ArgUtil.notEmpty(suffix, "suffix");

            return File.createTempFile(name, suffix);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 创建临时文件
     *
     * @param nameWithSuffix 文件名称全称
     * @return 临时文件
     * @since 0.1.50
     */
    public static File createTempFile(final String nameWithSuffix) {
        try {
            ArgUtil.notEmpty(nameWithSuffix, "fileName");
            String[] strings = nameWithSuffix.split("\\.");
            return File.createTempFile(strings[0], strings[1]);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 是否为图片格式
     *
     * @param string 原始字符串
     * @return 是否为图片
     * @since 0.1.73
     */
    public static boolean isImage(final String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        return string.endsWith(FileTypeConst.Image.PNG)
                || string.endsWith(FileTypeConst.Image.JPEG)
                || string.endsWith(FileTypeConst.Image.JPG)
                || string.endsWith(FileTypeConst.Image.GIF);
    }

    /**
     * 将文件内容转换为 map
     *
     * @param inputStream 输入流
     * @param charset     文件编码
     * @param mapHandler  转换实现
     * @param <K>         key 泛型
     * @param <V>         value 泛型
     * @return 结果
     * @since 0.1.95
     */
    public static <K, V> Map<K, V> readToMap(final InputStream inputStream,
                                             final String charset,
                                             final IMapHandler<K, V, String> mapHandler) {
        List<String> allLines = FileUtil.readAllLines(inputStream, charset);
        return MapUtil.toMap(allLines, mapHandler);
    }

    /**
     * 将文件内容转换为 map
     *
     * @param path       文件路径
     * @param charset    文件编码
     * @param mapHandler 转换实现
     * @param <K>        key 泛型
     * @param <V>        value 泛型
     * @return 结果
     * @since 0.1.83
     */
    public static <K, V> Map<K, V> readToMap(final String path,
                                             final String charset,
                                             final IMapHandler<K, V, String> mapHandler) {
        List<String> allLines = FileUtil.readAllLines(path, charset);
        return MapUtil.toMap(allLines, mapHandler);
    }

    /**
     * 将文件内容转换为 map
     *
     * @param path       文件路径
     * @param mapHandler 转换实现
     * @param <K>        key 泛型
     * @param <V>        value 泛型
     * @return 结果
     * @since 0.1.83
     */
    public static <K, V> Map<K, V> readToMap(final String path,
                                             final IMapHandler<K, V, String> mapHandler) {
        return readToMap(path, CharsetConst.UTF8, mapHandler);
    }

    /**
     * 将文件内容转换为 map
     * <p>
     * （1）直接拆分。取第一个值和第一个值
     * （2）默认使用空格分隔
     * @param path     文件路径
     * @return 结果
     * @since 0.1.119
     */
    public static Map<String, String> readToMap(final String path) {
        return readToMap(path, " ");
    }

    /**
     * 将文件内容转换为 map
     * <p>
     * （1）直接拆分。取第一个值和第一个值
     *
     * @param path     文件路径
     * @param splitter 分隔符号
     * @return 结果
     * @since 0.1.83
     */
    public static Map<String, String> readToMap(final String path,
                                                final String splitter) {
        return readToMap(path, new IMapHandler<String, String, String>() {
            @Override
            public String getKey(String o) {
                return o.split(splitter)[0];
            }

            @Override
            public String getValue(String o) {
                return o.split(splitter)[1];
            }
        });
    }

    /**
     * 获取文件名称
     *
     * @param path 完整路径
     * @return 名称
     * @since 0.1.86
     */
    public static String getFileName(final String path) {
        if (StringUtil.isEmptyTrim(path)) {
            return StringUtil.EMPTY;
        }

        File file = new File(path);
        String name = file.getName();

        return name.substring(0, name.lastIndexOf('.'));
    }

    /**
     * 获取父类路径
     *
     * @param path 当前路径
     * @return 父类路径
     * @since 0.1.86
     */
    public static String getDirPath(final String path) {
        Path path1 = Paths.get(path);
        return path1.getParent().toAbsolutePath().toString() + File.separator;
    }


    /**
     * 移除 windows 中禁止出现的特殊符号名称
     *
     * @param name 名称
     * @return 结果
     * @since 0.1.88
     */
    public static String trimWindowsSpecialChars(final String name) {
        if (StringUtil.isEmpty(name)) {
            return name;
        }

        return name.replaceAll("[?/\\\\*<>|:\"]", "");
    }

    /**
     * 重命名
     *
     * @param sourcePath 原始路径
     * @param targetPath 结果路径
     * @return 重命名结果
     * @since 0.1.98
     */
    public static boolean rename(final String sourcePath,
                                 final String targetPath) {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        return sourceFile.renameTo(targetFile);
    }

    /**
     * 文件合并
     *
     * @param result  结果路径
     * @param sources 其他待合并文件路径
     * @since 0.1.99
     */
    public static void merge(final String result,
                             final String... sources) {
        ArgUtil.notEmpty(result, "result");
        ArgUtil.notEmpty(sources, "sources");

        try (OutputStream os = new FileOutputStream(result)) {
            for (String source : sources) {
                byte[] bytes = getFileBytes(source);
                os.write(bytes);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 将指定的数组信息合并到指定的文件中
     *
     * @param result     结果路径
     * @param byteArrays 其他待合并文件路径
     * @since 0.1.99
     */
    public static void merge(final String result,
                             final byte[]... byteArrays) {
        ArgUtil.notEmpty(result, "result");
        ArgUtil.notEmpty(byteArrays, "byteArrays");

        try (OutputStream os = new FileOutputStream(result)) {
            for (byte[] bytes : byteArrays) {
                os.write(bytes);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 将指定的数组信息合并到指定的文件中
     *
     * @param result        结果路径
     * @param byteArrayList 其他待合并文件字节数组
     * @since 0.1.99
     */
    public static void merge(final String result,
                             final List<byte[]> byteArrayList) {
        ArgUtil.notEmpty(result, "result");
        ArgUtil.notEmpty(byteArrayList, "byteArrayList");

        try (OutputStream os = new FileOutputStream(result)) {
            for (byte[] bytes : byteArrayList) {
                os.write(bytes);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 写入字节到文件
     *
     * @param filePath 文件路径
     * @param bytes    字节信息
     * @since 0.2.1
     */
    public static void write(final String filePath,
                             final byte[] bytes) {
        ArgUtil.notEmpty(filePath, "filePath");

        try (OutputStream os = new FileOutputStream(filePath)) {
            os.write(bytes);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 转义 windows 下的特殊字符
     * @param fileName 文件名称
     * @return 转义后的字符串
     * @since 0.1.113
     */
    public static String escapeWindowsSpecial(final String fileName) {
        if(StringUtil.isEmpty(fileName)) {
            return fileName;
        }
        return fileName.replaceAll("[\"<>/\\\\|:*?]", "");
    }

    /**
     * 创建文件夹
     * @param dir 文件夹
     * @return 结果
     * @since 0.1.113
     */
    public static boolean createDir(final String dir) {
        if(StringUtil.isEmpty(dir)) {
            return false;
        }

        File file = new File(dir);
        if (file.isDirectory()) {
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 清空文件内容
     * @param filePath 文件路径
     * @since 0.1.116
     */
    public static void truncate(final String filePath) {
        FileUtil.write(filePath, StringUtil.EMPTY, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * 追加文件内容
     * @param filePath 文件路径
     * @param line 文件内容
     * @since 0.1.117
     */
    public static void append(final String filePath, final String line) {
        FileUtil.write(filePath, line, StandardOpenOption.APPEND);
    }

    /**
     * 追加文件内容
     * @param filePath 文件路径
     * @param collection 文件内容
     * @since 0.1.117
     */
    public static void append(final String filePath, final Collection<String> collection) {
        FileUtil.write(filePath, collection, StandardOpenOption.APPEND);
    }

    /**
     * 将文件转成 base64 字符串
     *
     * https://www.cnblogs.com/darkhumor/p/7525392.html
     * https://blog.csdn.net/phoenix_cat/article/details/84676302
     * https://blog.csdn.net/myloverisxin/article/details/117530365
     * https://www.cnblogs.com/yejg1212/p/11926649.html
     *
     * 不同规范编码不同，会导致出现换行符号，但是解码的时候会被忽略。
     * @param filePath 文件路径
     * @return base64 字符串
     * @since 0.1.146
     */
    public static String fileToBase64(String filePath) {
        //File file = new File(filePath);
		//
        //try(FileInputStream inputFile = new FileInputStream(file)) {
        //    byte[] buffer = new byte[(int)file.length()];
        //    inputFile.read(buffer);
        //    String plainText = new BASE64Encoder().encode(buffer);
		//
        //    return plainText.replaceAll("\r", "")
        //            .replaceAll("\n", "");
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
	    return filePath;
    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code base64 内容
     * @param targetPath 目标文件
     * @since 0.1.146
     */
    public static void base64ToFile(String base64Code,String targetPath) {
        //FileUtil.createFile(targetPath);
		//
        //try(FileOutputStream out = new FileOutputStream(targetPath);) {
        //    byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        //    out.write(buffer);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
    }

}
