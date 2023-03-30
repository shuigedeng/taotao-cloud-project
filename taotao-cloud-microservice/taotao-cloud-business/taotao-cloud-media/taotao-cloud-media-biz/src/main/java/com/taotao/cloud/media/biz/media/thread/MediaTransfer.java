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

package com.taotao.cloud.media.biz.media.thread;

import io.netty.channel.ChannelHandlerContext;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegLogCallback;

/** 媒体转换者 */
public abstract class MediaTransfer {

    static {
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();
    }

    /** ws客户端 */
    public ConcurrentHashMap<String, ChannelHandlerContext> wsClients = new ConcurrentHashMap<>();

    /** http客户端 */
    public ConcurrentHashMap<String, ChannelHandlerContext> httpClients = new ConcurrentHashMap<>();

    /** 当前在线人数 */
    public int hcSize, wcSize = 0;

    /** 用于没有客户端时候的计时 */
    public int noClient = 0;

    /** flv header */
    public byte[] header = null;

    /** 输出流，视频最终会输出到此 */
    public ByteArrayOutputStream bos = new ByteArrayOutputStream();

    /** 转码回调 */
    public TransferCallback transferCallback;

    public ConcurrentHashMap<String, ChannelHandlerContext> getWsClients() {
        return wsClients;
    }

    public void setWsClients(ConcurrentHashMap<String, ChannelHandlerContext> wsClients) {
        this.wsClients = wsClients;
    }

    public ConcurrentHashMap<String, ChannelHandlerContext> getHttpClients() {
        return httpClients;
    }

    public void setHttpClients(ConcurrentHashMap<String, ChannelHandlerContext> httpClients) {
        this.httpClients = httpClients;
    }

    public int getHcSize() {
        return hcSize;
    }

    public void setHcSize(int hcSize) {
        this.hcSize = hcSize;
    }

    public int getWcSize() {
        return wcSize;
    }

    public void setWcSize(int wcSize) {
        this.wcSize = wcSize;
    }

    public int getNoClient() {
        return noClient;
    }

    public void setNoClient(int noClient) {
        this.noClient = noClient;
    }

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public ByteArrayOutputStream getBos() {
        return bos;
    }

    public void setBos(ByteArrayOutputStream bos) {
        this.bos = bos;
    }

    public TransferCallback getTransferCallback() {
        return transferCallback;
    }

    public void setTransferCallback(TransferCallback transferCallback) {
        this.transferCallback = transferCallback;
    }
    //	public void addClient() {
    //
    //	}
}
