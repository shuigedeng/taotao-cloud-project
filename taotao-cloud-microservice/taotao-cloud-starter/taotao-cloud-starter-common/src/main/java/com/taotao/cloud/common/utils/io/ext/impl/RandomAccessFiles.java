package com.taotao.cloud.common.utils.io.ext.impl;


import com.taotao.cloud.common.constant.FileOptionConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.io.ext.IFiles;
import java.io.*;

/**
 * 用于封装 {@link RandomAccessFile} 随机访问文件
 *
 * @see FileOptionConst 文件打开方式常量
 */
public class RandomAccessFiles implements IFiles {

    /**
     * 文件信息
     */
    private final RandomAccessFile file;

    public RandomAccessFiles(final String path) {
        try {
            file = new RandomAccessFile(path, FileOptionConst.READ_WRITE);
        } catch (FileNotFoundException e) {
            throw new CommonRuntimeException(e);
        }
    }


    @Override
    public byte[] read(long startIndex, long endIndex) {
        try {
            ArgUtil.notNegative(startIndex, "startIndex not allow negative!");
            ArgUtil.assertTrue(endIndex >= startIndex, "endIndex >= startIndex is expected!");

            // 遍历文件内容
            final long actualEndIndex = getMin(file.length(), endIndex);
            int length = (int) (actualEndIndex-startIndex);
            int startIndexInt = (int) startIndex;
            byte[] bytes = new byte[length];
            file.readFully(bytes, startIndexInt, length);

            return bytes;
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    @Override
    public String read(long startIndex, long endIndex, String charset) {
        try {
            byte[] bytes = this.read(startIndex, endIndex);
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new CommonRuntimeException(e);
        }
    }

    @Override
    public void write(long startIndex, byte[] bytes) {
        ArgUtil.notNegative(startIndex, "startIndex not allow negative!");

        try {
            // 保留 startIndex 之后的文件信息
            final long fileLength = file.length();
            final long actualStartIndex = getMin(startIndex, fileLength);
            byte[] tempBytes = this.read(actualStartIndex, fileLength);

            // 写入文件内容
            file.seek(actualStartIndex);
            file.write(bytes);

            // 恢复临时保存的文件内容
            file.write(tempBytes);
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 返回较小的长度
     * @param paramOne 参数1
     * @param paramTwo 参数2
     * @return 最小的结果
     */
    private long getMin(final long paramOne, final long paramTwo) {
        if(paramOne <= paramTwo) {
            return paramOne;
        }
        return paramTwo;
    }

}
