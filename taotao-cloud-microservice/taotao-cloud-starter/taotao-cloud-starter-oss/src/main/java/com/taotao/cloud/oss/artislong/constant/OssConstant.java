package com.taotao.cloud.oss.artislong.constant;

import java.util.concurrent.TimeUnit;

/**
 * oss常数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:12
 */
public class OssConstant {

    public static final String OSS = "oss";
    public static final String ENABLE = "enable";
    public static final String DEFAULT_ENABLE_VALUE = "true";

    public static final int KB = 1024;
    /**
     * 默认分片大小
     */
    public static final Long DEFAULT_PART_SIZE = 5L * 1024 * KB;
    /**
     * 默认缓冲区大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 8 * KB;
    /**
     * 默认最大分片数
     */
    public static final Long DEFAULT_PART_NUM = 10000L;
    /**
     * 默认并发线程数
     */
    public static final Integer DEFAULT_TASK_NUM = Runtime.getRuntime().availableProcessors();

    public static final Long DEFAULT_CONNECTION_TIMEOUT = TimeUnit.MINUTES.toMillis(5);

    /**
     * OSS存储类型
     */
    public interface OssType {
        String LOCAL = "local";
        String FTP = "ftp";
        String SFTP = "sftp";
        String ALI = "ali";
        String QINIU = "qiniu";
        String MINIO = "minio";
        String BAIDU = "baidu";
        String TENCENT = "tencent";
        String HUAWEI = "huawei";
        String JD = "jd";
        String UP = "up";
        String JINSHAN = "jinshan";
        String WANGYI = "wangyi";
        String UCLOUD = "ucloud";
        String PINGAN = "pingan";
        String QINGYUN = "qingyun";
        String JDBC = "jdbc";
        String AWS = "aws";
    }

}
