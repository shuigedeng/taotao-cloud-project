package com.taotao.cloud.common.utils.io;


import com.taotao.cloud.common.exception.CommonRuntimeException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 文件随机访问工具类
 *
 */
public final class RandomAccessFileUtil {

    private RandomAccessFileUtil(){}

    /**
     * 获取文件内容
     * @param filePath 文件路径
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     * @return 结果
     * @since 0.1.78
     */
    public static String getFileContent(final String filePath,
                                        final int startIndex,
                                        final int endIndex) {
        return getFileContent(filePath, startIndex, endIndex, StandardCharsets.UTF_8);
    }

    /**
     * 获取文件内容
     * @param filePath 文件路径
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     * @param charset 编码
     * @return 结果
     * @since 0.1.78
     */
    public static String getFileContent(final String filePath,
                                        final int startIndex,
                                        final int endIndex,
                                        final Charset charset) {
        final int size = endIndex-startIndex;
        try(RandomAccessFile randomAccessFile  = new RandomAccessFile(filePath, "r")) {
            MappedByteBuffer inputBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, startIndex, size);

            byte[] bs = new byte[size];
            for (int offset = 0; offset < inputBuffer.capacity(); offset++) {
                bs[offset] = inputBuffer.get(offset);
            }

            return new String(bs, charset);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

}
