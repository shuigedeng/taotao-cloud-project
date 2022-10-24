package com.taotao.cloud.data.elasticsearch.esearchx;

/**
 * 超时：单位：秒
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:09:48
 */
class PriHttpTimeout {
    public final int connectTimeout;
    public final int writeTimeout;
    public final int readTimeout;

    public PriHttpTimeout(int timeout) {
        this.connectTimeout = timeout;
        this.writeTimeout = timeout;
        this.readTimeout = timeout;
    }

    public PriHttpTimeout(int connectTimeout, int writeTimeout, int readTimeout) {
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
    }
}
