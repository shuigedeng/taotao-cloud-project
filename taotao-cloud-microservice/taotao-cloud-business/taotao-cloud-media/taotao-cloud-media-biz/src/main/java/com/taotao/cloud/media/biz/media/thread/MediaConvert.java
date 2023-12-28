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

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import com.taotao.cloud.media.biz.media.service.MediaService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;

/**
 * ******此类已经过时，后续会删除，请使用MediaRecodeOrTransfer******
 *
 * <p>拉流转换推流处理线程
 */
public class MediaConvert extends Thread {

    /** ws客户端 */
    private ConcurrentHashMap<String, ChannelHandlerContext> wsClients = new ConcurrentHashMap<>();
    /** http客户端 */
    private ConcurrentHashMap<String, ChannelHandlerContext> httpClients = new ConcurrentHashMap<>();

    /** 运行状态 */
    private boolean runing = false;

    private boolean grabberStatus = false;

    /** 是否可以自动关闭流 */
    private boolean autoClose = true;

    private int hcSize, wcSize = 0;

    /** 没有客户端计数 */
    private int noClient = 0;

    /** flv header */
    private byte[] header = null;
    // 输出流，视频最终会输出到此
    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    /** 相机 */
    private CameraDto cameraDto;

    /**
     * @param cameraDto
     * @param autoClose 流是否可以自动关闭
     */
    public MediaConvert(CameraDto cameraDto, boolean autoClose) {
        super();
        this.autoClose = autoClose;
        this.cameraDto = cameraDto;
    }

    public boolean isRuning() {
        return runing;
    }

    public void setRuning(boolean runing) {
        this.runing = runing;
    }

    /** */
    private void convert() {
        // 拉流器
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(cameraDto.getUrl());
        // 超时时间(15秒)
        grabber.setOption("stimoout", "15000000");
        grabber.setOption("threads", "1");
        grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1024000");
        // 如果为rtsp流，增加配置
        if ("rtsp".equals(cameraDto.getUrl().substring(0, 4))) {
            // 设置打开协议tcp / udp
            grabber.setOption("rtsp_transport", "tcp");
        }

        try {
            grabber.start();
            grabberStatus = true;
        } catch (Exception e) {
            LogUtils.error(e);
        }

        // 推流器
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(bos, grabber.getImageWidth(), grabber.getImageHeight());

        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();

        recorder.setInterleaved(false);
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoOption("crf", "26");
        recorder.setVideoOption("threads", "1");
        recorder.setFormat("flv");
        recorder.setFrameRate(25); // 设置帧率
        recorder.setGopSize(5); // 设置gop
        //		recorder.setVideoBitrate(500 * 1000);// 码率500kb/s
        recorder.setVideoCodecName("libx264");
        //		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        //		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.setAudioCodecName("aac");
        recorder.setAudioChannels(grabber.getAudioChannels());

        try {
            recorder.start();
            grabber.flush();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {

            LogUtils.info("启动录制器失败", e1);
            e1.printStackTrace();
        } catch (Exception e1) {
            LogUtils.info("拉流器异常", e1);
            e1.printStackTrace();
        }

        if (header == null) {
            header = bos.toByteArray();
            //			LogUtils.info(HexUtil.encodeHexStr(header));
            bos.reset();
        }

        runing = true;
        long startTime = 0;
        long videoTS = 0;
        long lastTimes = System.currentTimeMillis();

        while (runing && grabberStatus) {

            if ((System.currentTimeMillis() - lastTimes) > 1000) {
                try {
                    grabber.restart(); // grabber.grabFrame() avformat
                    grabber.flush();
                    //					log.info("\r\n{}\r\n重连成功》》》", camera.getUrl());
                } catch (Exception e) {
                    //					log.info("\r\n{}\r\n重连失败！", camera.getUrl());
                } finally {
                    lastTimes = System.currentTimeMillis();
                }
            }

            hasClient();

            try {
                Frame frame = grabber.grabFrame();
                if (frame != null) {
                    lastTimes = System.currentTimeMillis();

                    if (startTime == 0) {
                        startTime = System.currentTimeMillis();
                    }
                    videoTS = 1000 * (System.currentTimeMillis() - startTime);
                    // 判断时间偏移
                    if (videoTS > recorder.getTimestamp()) {
                        //					LogUtils.info("矫正时间戳: " + videoTS + " : " +
                        // recorder.getTimestamp() + " -> "
                        //							+ (videoTS - recorder.getTimestamp()));
                        recorder.setTimestamp((videoTS));
                    }
                    recorder.record(frame);

                    if (bos.size() > 0) {
                        byte[] b = bos.toByteArray();
                        bos.reset();

                        // ws输出帧流
                        for (Entry<String, ChannelHandlerContext> entry : wsClients.entrySet()) {
                            if (entry.getValue().channel().isWritable()) {
                                entry.getValue().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(b)));
                            } else {
                                wsClients.remove(entry.getKey());
                                hasClient();
                            }
                        }

                        // http
                        for (Entry<String, ChannelHandlerContext> entry : httpClients.entrySet()) {
                            if (entry.getValue().channel().isWritable()) {
                                entry.getValue().writeAndFlush(Unpooled.copiedBuffer(b));
                            } else {
                                httpClients.remove(entry.getKey());
                                hasClient();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                //				log.info("\r\n{}\r\n尝试重连。。。", camera.getUrl());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                }
                //				LogUtils.error(e);
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                //				runing = false;
                LogUtils.info("\r\n{}\r\n录制器出现异常。。。", cameraDto.getUrl());
                LogUtils.error(e);
            }
        }

        // close包含stop和release方法。录制文件必须保证最后执行stop()方法
        try {
            recorder.close();
            grabber.close();
            bos.close();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            LogUtils.error(e);
        } catch (Exception e) {
            LogUtils.error(e);
        } catch (IOException e) {
            LogUtils.error(e);
        } finally {
            runing = false;
        }
        LogUtils.info("关闭媒体流，{} ", cameraDto.getUrl());
    }

    /** 新增ws客戶端 */
    public void addWsClient(ChannelHandlerContext ctx) throws Exception {
        int timeout = 0;
        while (true) {
            if (runing) {
                if (ctx.channel().isWritable()) {
                    // 发送帧前先发送header
                    ChannelFuture future = ctx.writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(header)));
                    future.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                wsClients.put(ctx.channel().id().toString(), ctx);
                            }
                        }
                    });
                }

                break;
            }

            // 等待推拉流启动
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtils.error(e);
            }
            // 启动录制器失败
            timeout += 100;
            if (timeout > 15000) {
                break;
            }
        }
    }

    /**
     * 关闭流
     *
     * @return
     */
    public void hasClient() {

        int newHcSize = httpClients.size();
        int newWcSize = wsClients.size();
        if (hcSize != newHcSize || wcSize != newWcSize) {
            hcSize = newHcSize;
            wcSize = newWcSize;
            LogUtils.info("\r\n{}\r\nhttp连接数：{}, ws连接数：{} \r\n", cameraDto.getUrl(), newHcSize, newWcSize);
        }

        // 自动拉流无需关闭
        if (!autoClose) {
            if (httpClients.isEmpty() && wsClients.isEmpty()) {
                try {
                    Thread.sleep(5); // 不能太久
                } catch (InterruptedException e) {
                }
            }
            return;
        }
        if (httpClients.isEmpty() && wsClients.isEmpty()) {
            // 5*2000=10000=10，等待10秒还没有客户端，则关闭推流
            if (noClient > 2000) {
                runing = false;
                String mediaKey = MD5.create().digestHex(cameraDto.getUrl());
                MediaService.cameras.remove(mediaKey);

            } else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                noClient += 1;
            }
        } else {
            noClient = 0;
        }
    }

    /** 新增http客戶端 */
    public void addHttpClient(ChannelHandlerContext ctx) {
        int timeout = 0;
        while (true) {
            if (runing) {
                if (ctx.channel().isWritable()) {
                    // 发送帧前先发送header
                    ChannelFuture future = ctx.writeAndFlush(Unpooled.copiedBuffer(header));
                    future.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                httpClients.put(ctx.channel().id().toString(), ctx);
                            }
                        }
                    });
                }

                break;
            }

            // 等待推拉流启动
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtils.error(e);
            }

            // 启动录制器失败
            timeout += 100;
            if (timeout > 15000) {
                break;
            }
        }
    }

    @Override
    public void run() {
        convert();
    }
}
