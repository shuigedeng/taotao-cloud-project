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

package com.taotao.cloud.media.biz.media.service;

import com.taotao.cloud.media.biz.media.common.ClientType;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import com.taotao.cloud.media.biz.media.thread.MediaTransfer;
import com.taotao.cloud.media.biz.media.thread.MediaTransferFlvByFFmpeg;
import com.taotao.cloud.media.biz.media.thread.MediaTransferFlvByJavacv;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/** 媒体服务，支持全局网络超时、读写超时、无人拉流持续时长自动关闭流等配置 */
@Service
public class MediaService {

    /** 缓存流转换线程 */
    public static ConcurrentHashMap<String, MediaTransfer> cameras = new ConcurrentHashMap<>();

    /**
     * http-flv播放
     *
     * @param cameraDto
     * @param ctx
     */
    public void playForHttp(CameraDto cameraDto, ChannelHandlerContext ctx) {

        if (cameras.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = cameras.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                // 如果当前已经用ffmpeg，则重新拉流
                if (cameraDto.isEnabledFFmpeg()) {
                    mediaTransferFlvByJavacv.setRunning(false);
                    cameras.remove(cameraDto.getMediaKey());
                    this.playForHttp(cameraDto, ctx);
                } else {
                    mediaTransferFlvByJavacv.addClient(ctx, ClientType.HTTP);
                }
            } else if (mediaConvert instanceof MediaTransferFlvByFFmpeg) {
                MediaTransferFlvByFFmpeg mediaTransferFlvByFFmpeg = (MediaTransferFlvByFFmpeg) mediaConvert;
                // 如果当前已经用javacv，则关闭再重新拉流
                if (!cameraDto.isEnabledFFmpeg()) {
                    mediaTransferFlvByFFmpeg.stopFFmpeg();
                    cameras.remove(cameraDto.getMediaKey());
                    this.playForHttp(cameraDto, ctx);
                } else {
                    mediaTransferFlvByFFmpeg.addClient(ctx, ClientType.HTTP);
                }
            }

        } else {
            if (cameraDto.isEnabledFFmpeg()) {
                MediaTransferFlvByFFmpeg mediaft = new MediaTransferFlvByFFmpeg(cameraDto);
                mediaft.execute();
                cameras.put(cameraDto.getMediaKey(), mediaft);
                mediaft.addClient(ctx, ClientType.HTTP);
            } else {
                MediaTransferFlvByJavacv mediaConvert = new MediaTransferFlvByJavacv(cameraDto);
                cameras.put(cameraDto.getMediaKey(), mediaConvert);
                ThreadUtil.execute(mediaConvert);
                mediaConvert.addClient(ctx, ClientType.HTTP);
            }
        }
    }

    /**
     * ws-flv播放
     *
     * @param cameraDto
     * @param ctx
     */
    public void playForWs(CameraDto cameraDto, ChannelHandlerContext ctx) {

        if (cameras.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = cameras.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                // 如果当前已经用ffmpeg，则重新拉流
                if (cameraDto.isEnabledFFmpeg()) {
                    mediaTransferFlvByJavacv.setRunning(false);
                    cameras.remove(cameraDto.getMediaKey());
                    this.playForWs(cameraDto, ctx);
                } else {
                    mediaTransferFlvByJavacv.addClient(ctx, ClientType.WEBSOCKET);
                }
            } else if (mediaConvert instanceof MediaTransferFlvByFFmpeg) {
                MediaTransferFlvByFFmpeg mediaTransferFlvByFFmpeg = (MediaTransferFlvByFFmpeg) mediaConvert;
                // 如果当前已经用javacv，则关闭再重新拉流
                if (!cameraDto.isEnabledFFmpeg()) {
                    mediaTransferFlvByFFmpeg.stopFFmpeg();
                    cameras.remove(cameraDto.getMediaKey());
                    this.playForWs(cameraDto, ctx);
                } else {
                    mediaTransferFlvByFFmpeg.addClient(ctx, ClientType.WEBSOCKET);
                }
            }
        } else {
            if (cameraDto.isEnabledFFmpeg()) {
                MediaTransferFlvByFFmpeg mediaft = new MediaTransferFlvByFFmpeg(cameraDto);
                mediaft.execute();
                cameras.put(cameraDto.getMediaKey(), mediaft);
                mediaft.addClient(ctx, ClientType.WEBSOCKET);
            } else {
                MediaTransferFlvByJavacv mediaConvert = new MediaTransferFlvByJavacv(cameraDto);
                cameras.put(cameraDto.getMediaKey(), mediaConvert);
                ThreadUtil.execute(mediaConvert);
                mediaConvert.addClient(ctx, ClientType.WEBSOCKET);
            }
        }
    }

    /**
     * api播放
     *
     * @param cameraDto
     * @return
     */
    public boolean playForApi(CameraDto cameraDto) {
        // 区分不同媒体
        String mediaKey = MD5.create().digestHex(cameraDto.getUrl());
        cameraDto.setMediaKey(mediaKey);
        cameraDto.setEnabledFlv(true);

        MediaTransfer mediaTransfer = cameras.get(cameraDto.getMediaKey());
        if (null == mediaTransfer) {
            if (cameraDto.isEnabledFFmpeg()) {
                MediaTransferFlvByFFmpeg mediaft = new MediaTransferFlvByFFmpeg(cameraDto);
                mediaft.execute();
                cameras.put(cameraDto.getMediaKey(), mediaft);
            } else {
                MediaTransferFlvByJavacv mediaConvert = new MediaTransferFlvByJavacv(cameraDto);
                cameras.put(cameraDto.getMediaKey(), mediaConvert);
                ThreadUtil.execute(mediaConvert);
            }
        }

        mediaTransfer = cameras.get(cameraDto.getMediaKey());
        // 同步等待
        if (mediaTransfer instanceof MediaTransferFlvByJavacv) {
            MediaTransferFlvByJavacv mediaft = (MediaTransferFlvByJavacv) mediaTransfer;
            // 30秒还没true认为启动不了
            for (int i = 0; i < 60; i++) {
                if (mediaft.isRunning() && mediaft.isGrabberStatus() && mediaft.isRecorderStatus()) {
                    return true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        } else if (mediaTransfer instanceof MediaTransferFlvByFFmpeg) {
            MediaTransferFlvByFFmpeg mediaft = (MediaTransferFlvByFFmpeg) mediaTransfer;
            // 30秒还没true认为启动不了
            for (int i = 0; i < 60; i++) {
                if (mediaft.isRunning()) {
                    return true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
        return false;
    }

    /**
     * 关闭流
     *
     * @param cameraDto
     */
    public void closeForApi(CameraDto cameraDto) {
        cameraDto.setEnabledFlv(false);

        if (cameras.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = cameras.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                mediaTransferFlvByJavacv.setRunning(false);
                cameras.remove(cameraDto.getMediaKey());
            } else if (mediaConvert instanceof MediaTransferFlvByFFmpeg) {
                MediaTransferFlvByFFmpeg mediaTransferFlvByFFmpeg = (MediaTransferFlvByFFmpeg) mediaConvert;
                mediaTransferFlvByFFmpeg.stopFFmpeg();
                cameras.remove(cameraDto.getMediaKey());
            }
        }
    }
}
