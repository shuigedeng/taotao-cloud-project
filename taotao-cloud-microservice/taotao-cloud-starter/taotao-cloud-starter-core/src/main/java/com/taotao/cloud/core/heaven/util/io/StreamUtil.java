package com.taotao.cloud.core.heaven.util.io;


import com.taotao.cloud.core.heaven.constant.CharsetConst;
import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import com.taotao.cloud.core.heaven.support.handler.IMapHandler;
import com.taotao.cloud.core.heaven.util.id.impl.Ids;
import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 流工具类
 *
 */
public class StreamUtil {

    private StreamUtil() {
    }

    /**
     * 流转换为字符
     *
     * @param is      流。注意：这里并不会关闭输入流，需要外部自行处理。
     * @param charset 编码集合
     * @return 字符串
     */
    @Deprecated
    public static String toString(InputStream is, String charset) {
        try (ByteArrayOutputStream boa = new ByteArrayOutputStream()) {
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = is.read(buffer)) != -1) {
                boa.write(buffer, 0, len);
            }
            byte[] result = boa.toByteArray();
            return new String(result, charset);
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 流转换为字符
     *
     * @param is 流。注意：这里并不会关闭输入流，需要外部自行处理。
     * @return 字符串
     */
    @Deprecated
    public static String toString(InputStream is) {
        return toString(is, CharsetConst.UTF8);
    }

    /**
     * 获取文章内容
     * @param path 路径
     * @return 文件内容
     * @since 0.1.71
     */
    public static String getFileContent(final String path) {
        return getFileContent(path, CharsetConst.UTF8);
    }

    /**
     * 获取文章内容
     * @param path 路径
     * @param charset 字符集合
     * @return 文件内容
     * @since 0.1.71
     */
    public static String getFileContent(final String path,
                                 final String charset) {
        try (InputStream inputStream = getInputStream(path);
                ByteArrayOutputStream boa = new ByteArrayOutputStream()) {
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = inputStream.read(buffer)) != -1) {
                boa.write(buffer, 0, len);
            }
            byte[] result = boa.toByteArray();
            return new String(result, charset);
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取文件字节流
     * @param filePath 文件路径
     * @return 字节流
     * @since 0.1.99
     */
    public static byte[] getFileBytes(final String filePath) {
        InputStream inputStream = getInputStream(filePath);
        return inputStreamToBytes(inputStream);
    }

    /**
     * 获取文件对应输入流
     *
     * @param filePath 文件路径
     * @return 输入流
     * @since 0.1.44
     */
    public static InputStream getInputStream(final String filePath) {
        InputStream inputStream;
        try {
            inputStream = new URL(filePath).openStream();
        } catch (MalformedURLException localMalformedURLException) {
            try {
                inputStream = new FileInputStream(filePath);
            } catch (Exception localException2) {
                ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
                if (localClassLoader == null) {
                    localClassLoader = StreamUtil.class.getClassLoader();
                }
                inputStream = localClassLoader.getResourceAsStream(filePath);
                if (inputStream == null) {
                    throw new CommonRuntimeException("Could not find file: " + filePath);
                }
            }
        } catch (IOException localIOException1) {
            throw new CommonRuntimeException(localIOException1);
        }

        return inputStream;
    }

    /**
     * 关闭流
     * @param closeable 可关闭的信息
     * @since 0.1.44
     */
    public static void closeStream(final Closeable closeable) {
        if(ObjectUtil.isNotNull(closeable)) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new CommonRuntimeException(e);
            }
        }
    }

    /**
     * 获取数据内容
     * 例如： /data.txt
     * @param path resource 下的文件路径
     * @return 返回数据集合
     * @since 0.1.67
     */
    public static List<String> readAllLines(final String path) {
        InputStream inputStream = StreamUtil.class.getResourceAsStream(path);
        return readAllLines(inputStream, CharsetConst.UTF8, true);
    }

    /**
     * 构建数据集合
     * @param is 文件输入流
     * @return 返回数据集合
     * @since 0.1.67
     */
    public static List<String> readAllLines(final InputStream is) {
        return readAllLines(is, CharsetConst.UTF8, true);
    }

    /**
     * 构建数据集合
     *
     * @param is 文件输入流
     * @param charset 文件编码
     * @param ignoreEmpty 是否忽略空白行
     * @return 返回数据集合
     * @since 0.1.67
     */
    public static List<String> readAllLines(InputStream is,
                                            final String charset,
                                            final boolean ignoreEmpty) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader e = new BufferedReader(new InputStreamReader(is,
                    Charset.forName(charset)));

            while (e.ready()) {
                String entry = e.readLine();
                if (StringUtil.isEmpty(entry)
                        && ignoreEmpty) {
                    continue;
                }
                lines.add(entry);
            }
            return lines;
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取文件内容
     * @param path 路径
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     * @return 结果
     * @since 0.1.78
     */
    public static String getFileContent(final String path,
                                        final int startIndex,
                                        final int endIndex) {
        return getFileContent(path, startIndex, endIndex, StandardCharsets.UTF_8);
    }

    /**
     * 获取文件内容
     * @param path 路径
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     * @param charset 编码
     * @return 结果
     * @since 0.1.78
     */
    public static String getFileContent(final String path,
                                        final int startIndex,
                                        final int endIndex,
                                        final Charset charset) {
        try(InputStream inputStream = StreamUtil.class.getResourceAsStream(path)) {
            return FileUtil.getFileContent(inputStream, startIndex, endIndex, charset);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 文件输入流转 file
     * https://www.cnblogs.com/asfeixue/p/9065681.html
     *
     * @param inputStream 输入流
     * @param deleteOnExit 退出时删除
     * @return 文件信息
     * @since 0.1.87
     */
    public static File inputStreamToFile(final InputStream inputStream,
                                         final boolean deleteOnExit) {
        if (ObjectUtil.isNull(inputStream)) {
            return null;
        }

        try {
            File temp = File.createTempFile(Ids.uuid32(), "temp");
            // 退出时删除
            if(deleteOnExit) {
                temp.deleteOnExit();
            }

            // 复制文件流信息到 temp 中
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return temp;
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        } finally {
            StreamUtil.closeStream(inputStream);
        }
    }

    /**
     * 文件输入流转 file
     * https://www.cnblogs.com/asfeixue/p/9065681.html
     *
     * @param inputStream 输入流
     * @return 文件信息
     * @since 0.1.87
     */
    public static File inputStreamToFile(final InputStream inputStream) {
        return inputStreamToFile(inputStream, false);
    }

    /**
     * 输入流转为字节流
     * @param inputStream 输入流
     * @return 字节数组
     * @since 0.1.87
     */
    public static byte[] inputStreamToBytes(final InputStream inputStream) {
        try(ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }

            return output.toByteArray();
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 输入流转为字符串
     * @param inputStream 输入流
     * @param charsetStr 字符编码
     * @return 字节数组
     * @since 0.1.87
     */
    public static String inputStreamToString(final InputStream inputStream,
                                            final String charsetStr) {
        byte[] bytes = inputStreamToBytes(inputStream);
        Charset charset = Charset.forName(charsetStr);
        return new String(bytes, charset);
    }

    /**
     * 输入流转为字符串
     * @param inputStream 输入流
     * @return 字节数组
     * @since 0.1.87
     */
    public static String inputStreamToString(final InputStream inputStream) {
        return inputStreamToString(inputStream, CharsetConst.UTF8);
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
     * @since 0.1.95
     */
    public static <K, V> Map<K, V> readToMap(final String path,
                                             final String charset,
                                             final IMapHandler<K, V, String> mapHandler) {
        InputStream inputStream = StreamUtil.class.getResourceAsStream(path);
        return FileUtil.readToMap(inputStream, charset, mapHandler);
    }

    /**
     * 将文件内容转换为 map
     *
     * @param path       文件路径
     * @param mapHandler 转换实现
     * @param <K>        key 泛型
     * @param <V>        value 泛型
     * @return 结果
     * @since 0.1.95
     */
    public static <K, V> Map<K, V> readToMap(final String path,
                                             final IMapHandler<K, V, String> mapHandler) {
        return readToMap(path, CharsetConst.UTF8, mapHandler);
    }

    /**
     * 将文件内容转换为 map
     *
     * @param path       文件路径
     * @param splliter  拆分符号
     * @return 结果
     * @since 0.1.95
     */
    public static Map<String, String> readToMap(final String path, final String splliter) {
        return readToMap(path, new IMapHandler<String, String, String>() {
            @Override
            public String getKey(String o) {
                return o.split(splliter)[0];
            }

            @Override
            public String getValue(String o) {
                return o.split(splliter)[1];
            }
        });
    }

    /**
     * Writes the set of service class names to a service file.
     *
     * @param output not {@code null}. Not closed after use.
     * @param lines a not {@code null Collection} of service class names.
     * @param charset 文件编码
     * @since 0.1.108
     */
    public static void write(Collection<String> lines, OutputStream output,
                              final String charset) {
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, Charset.forName(charset)))) {
            for (String service : lines) {
                writer.write(service);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the set of service class names to a service file.
     *
     * @param output not {@code null}. Not closed after use.
     * @param lines a not {@code null Collection} of service class names.
     * @since 0.1.108
     */
    public static void write(Collection<String> lines, OutputStream output) {
        write(lines, output, CharsetConst.UTF8);
    }

}
