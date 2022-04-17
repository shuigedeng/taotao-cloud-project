package com.taotao.cloud.oss.artislong.constant;

public enum OssType {

    /**
     * 本地磁盘存储
     */
    LOCAL(OssConstant.OssType.LOCAL),

    /**
     * FTP协议存储
     */
    FTP(OssConstant.OssType.FTP),

    /**
     * SFTP存储
     */
    SFTP(OssConstant.OssType.SFTP),

    /**
     * 阿里OSS存储
     */
    ALI(OssConstant.OssType.ALI),

    /**
     * 七牛云存储
     */
    QINIU(OssConstant.OssType.QINIU),

    /**
     * MinIO存储
     */
    MINIO(OssConstant.OssType.MINIO),

    /**
     * 百度云存储
     */
    BAIDU(OssConstant.OssType.BAIDU),

    /**
     * 腾讯云存储
     */
    TENCENT(OssConstant.OssType.TENCENT),

    /**
     * 华为云存储
     */
    HUAWEI(OssConstant.OssType.HUAWEI),

    /**
     * 京东云存储
     */
    JD(OssConstant.OssType.JD),

    /**
     * 又拍云存储
     */
    UP(OssConstant.OssType.UP),

    /**
     * 金山云
     */
    JINSHAN(OssConstant.OssType.JINSHAN),

    /**
     * 网易数帆
     */
    WANGYI(OssConstant.OssType.WANGYI),

    /**
     * UCloud
     */
    UCLOUD(OssConstant.OssType.UCLOUD),

    /**
     * 平安云
     */
    PINGAN(OssConstant.OssType.PINGAN),

    /**
     * 青云
     */
    QINGYUN(OssConstant.OssType.QINGYUN),

    /**
     * JDBC
     */
    JDBC(OssConstant.OssType.JDBC),

    /**
     * 亚马逊
     */
    AWS(OssConstant.OssType.AWS);

    private final String value;

    OssType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
