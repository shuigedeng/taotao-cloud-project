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
import com.taotao.cloud.media.biz.media.common.ClientType;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import com.taotao.cloud.media.biz.media.service.MediaService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Map.Entry;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;

/**
 * <b>支持转复用或转码线程<b> <b> 什么情况下会转复用?</b>
 *
 * <p>视频源的音视频编码必须是浏览器和flv规范两者同时支持的编码，比如H264/AAC，
 *
 * <p>否则将进行转码。
 *
 * <p>转封装暂不支持hevc、vvc、vp8、vp9、g711、g771a等编码 <b> 转码累积延迟补偿暂未实现。</b> *
 * 由于转流过程中的拉流解码和编码是个线性串联链，多线程转码也不能解决该问题，后面可能需要采用主动跳包方式来解决
 */
public class MediaTransferFlvByJavacv extends MediaTransfer implements Runnable {

    /** 运行状态 */
    private volatile boolean running = false;

    private boolean grabberStatus = false;

    private boolean recorderStatus = false;

    private FFmpegFrameGrabber grabber; // 拉流器
    private FFmpegFrameRecorder recorder; // 推流录制器

    /** true:转复用,false:转码 */
    boolean transferFlag = false; // 默认转码

    /** 相机 */
    private CameraDto cameraDto;

    /** 监听线程，用于监听状态 */
    private Thread listenThread;

    /**
     * @param cameraDto
     * @param autoClose 流是否可以自动关闭
     */
    public MediaTransferFlvByJavacv(CameraDto cameraDto) {
        super();
        this.cameraDto = cameraDto;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isGrabberStatus() {
        return grabberStatus;
    }

    public void setGrabberStatus(boolean grabberStatus) {
        this.grabberStatus = grabberStatus;
    }

    public boolean isRecorderStatus() {
        return recorderStatus;
    }

    public void setRecorderStatus(boolean recorderStatus) {
        this.recorderStatus = recorderStatus;
    }

    /**
     * 创建拉流器
     *
     * @return
     */
    protected boolean createGrabber() {
        // 拉流器
        grabber = new FFmpegFrameGrabber(cameraDto.getUrl());
        // 超时时间(15秒)
        grabber.setOption("stimeout", cameraDto.getNetTimeout());
        grabber.setOption("threads", "1");
        // grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1024000");

        // 读写超时，适用于所有协议的通用读写超时
        grabber.setOption("rw_timeout", cameraDto.getReadOrWriteTimeout());
        // 探测视频流信息，为空默认5000000微秒
        grabber.setOption("probesize", cameraDto.getReadOrWriteTimeout());
        // 解析视频流信息，为空默认5000000微秒
        grabber.setOption("analyzeduration", cameraDto.getReadOrWriteTimeout());

        // 如果为rtsp流，增加配置
        if ("rtsp".equals(cameraDto.getUrl().substring(0, 4))) {
            // 设置打开协议tcp / udp
            grabber.setOption("rtsp_transport", "tcp");
            // 首选TCP进行RTP传输
            grabber.setOption("rtsp_flags", "prefer_tcp");

        } else if ("rtmp".equals(cameraDto.getUrl().substring(0, 4))) {
            // rtmp拉流缓冲区，默认3000毫秒
            grabber.setOption("rtmp_buffer", "1000");
            // 默认rtmp流为直播模式，不允许seek
            // grabber.setOption("rtmp_live", "live");

        } else if ("desktop".equals(cameraDto.getUrl())) {
            // 支持本地屏幕采集，可以用于监控屏幕、局域网和wifi投屏等
            grabber.setFormat("gdigrab");
            grabber.setOption("draw_mouse", "1"); // 绘制鼠标
            grabber.setNumBuffers(0);
            grabber.setOption("fflags", "nobuffer");
            grabber.setOption("framerate", "25");
            grabber.setFrameRate(25);
        }

        try {
            grabber.start();
            LogUtils.info("\r\n{}\r\n启动拉流器成功", cameraDto.getUrl());
            return grabberStatus = true;
        } catch (Exception e) {
            MediaService.cameras.remove(cameraDto.getMediaKey());
            LogUtils.error("\r\n{}\r\n启动拉流器失败，网络超时或视频源不可用", cameraDto.getUrl());
            //			LogUtils.error(e);
        }
        return grabberStatus = false;
    }

    /**
     * 创建转码推流录制器
     *
     * @return
     */
    protected boolean createTransterOrRecodeRecorder() {
        recorder = new FFmpegFrameRecorder(
                bos, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
        recorder.setFormat("flv");
        if (!transferFlag) {
            // 转码
            recorder.setInterleaved(false);
            recorder.setVideoOption("tune", "zerolatency");
            recorder.setVideoOption("preset", "ultrafast");
            recorder.setVideoOption("crf", "26");
            recorder.setVideoOption("threads", "1");
            recorder.setFrameRate(25); // 设置帧率
            recorder.setGopSize(25); // 设置gop,与帧率相同，相当于间隔1秒chan's一个关键帧
            //			recorder.setVideoBitrate(500 * 1000);// 码率500kb/s
            //			recorder.setVideoCodecName("libx264");	//javacv 1.5.5无法使用libx264名称，请使用下面方法
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            //			recorder.setAudioCodecName("aac");
            /** 启用RDOQ算法，优化视频质量 1：在视频码率和视频质量之间取得平衡 2：最大程度优化视频质量（会降低编码速度和提高码率） */
            recorder.setTrellis(1);
            recorder.setMaxDelay(0); // 设置延迟
            try {
                recorder.start();
                return recorderStatus = true;
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
                LogUtils.info("启动转码录制器失败", e1);
                MediaService.cameras.remove(cameraDto.getMediaKey());
                e1.printStackTrace();
            }
        } else {
            // 转复用
            // 不让recorder关联关闭outputStream
            recorder.setCloseOutputStream(false);
            try {
                recorder.start(grabber.getFormatContext());
                return recorderStatus = true;
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                LogUtils.warn("\r\n{}\r\n启动转复用录制器失败", cameraDto.getUrl());
                // 如果转复用失败，则自动切换到转码模式
                transferFlag = false;
                if (recorder != null) {
                    try {
                        recorder.stop();
                    } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
                    }
                }
                if (createTransterOrRecodeRecorder()) {
                    LogUtils.error("\r\n{}\r\n切换到转码模式", cameraDto.getUrl());
                    return true;
                }
                LogUtils.error("\r\n{}\r\n切换转码模式失败", cameraDto.getUrl());
                LogUtils.error(e);
            }
        }
        return recorderStatus = false;
    }

    /**
     * 是否支持flv的音视频编码
     *
     * @return
     */
    private boolean supportFlvFormatCodec() {
        int vcodec = grabber.getVideoCodec();
        int acodec = grabber.getAudioCodec();
        return (cameraDto.getType() == 0)
                && ("desktop".equals(cameraDto.getUrl())
                        || avcodec.AV_CODEC_ID_H264 == vcodec
                        || avcodec.AV_CODEC_ID_H263 == vcodec)
                && (avcodec.AV_CODEC_ID_AAC == acodec || avcodec.AV_CODEC_ID_AAC_LATM == acodec);
    }

    /** 将视频源转换为flv */
    protected void transferStream2Flv() {
        if (!createGrabber()) {
            //			super.transferCallback.start(false);
            return;
        }
        transferFlag = supportFlvFormatCodec();
        if (!createTransterOrRecodeRecorder()) {
            //			super.transferCallback.start(false);
            return;
        }

        try {
            grabber.flush();
        } catch (Exception e) {
            LogUtils.info("清空拉流器缓存失败", e);
            LogUtils.error(e);
        }
        if (header == null) {
            header = bos.toByteArray();
            //				LogUtils.info(HexUtil.encodeHexStr(header));
            bos.reset();
        }

        running = true;

        //		super.transferCallback.start(true);
        // 启动监听线程（用于判断是否需要自动关闭推流）
        listenClient();

        // 时间戳计算
        long startTime = 0;
        long videoTS = 0;
        // 累积延迟计算
        //		long latencyDifference = 0;// 累积延迟
        //		long lastLatencyDifference = 0;// 当前最新一组gop的延迟
        //		long maxLatencyThreshold = 30000000;//
        // 最大延迟阈值，如果lastLatencyDifference-latencyDifference>maxLatencyThreshold，则重启拉流器
        //		long processTime = 0;// 上一帧处理耗时，用于延迟时间补偿，处理耗时不算进累积延迟

        for (; running && grabberStatus && recorderStatus; ) {

            try {
                if (transferFlag) {
                    // 转复用
                    long startGrab = System.currentTimeMillis();
                    AVPacket pkt = grabber.grabPacket();
                    if ((System.currentTimeMillis() - startGrab) > 5000) {
                        //						doReConnect();
                        //						continue;
                        LogUtils.info("\r\n{}\r\n视频流网络异常>>>", cameraDto.getUrl());
                        closeMedia();
                        break;
                    }
                    if (null != pkt && !pkt.isNull()) {
                        if (startTime == 0) {
                            startTime = System.currentTimeMillis();
                        }
                        videoTS = 1000 * (System.currentTimeMillis() - startTime);
                        // 判断时间偏移
                        if (videoTS > recorder.getTimestamp()) {
                            // LogUtils.info("矫正时间戳: " + videoTS + " : " +
                            // recorder.getTimestamp() + "
                            // -> "
                            // + (videoTS - recorder.getTimestamp()));
                            recorder.setTimestamp((videoTS));
                        }
                        recorder.recordPacket(pkt);
                    }
                } else {
                    // 转码
                    long startGrab = System.currentTimeMillis();
                    Frame frame = grabber.grab(); // 这边判断相机断网，正常50左右，断线15000
                    if ((System.currentTimeMillis() - startGrab) > 5000) {
                        //						doReConnect();
                        //						continue;
                        LogUtils.info("\r\n{}\r\n视频流网络异常>>>", cameraDto.getUrl());
                        closeMedia();
                        break;
                    }

                    if (frame != null) {
                        if (startTime == 0) {
                            startTime = System.currentTimeMillis();
                        }
                        videoTS = 1000 * (System.currentTimeMillis() - startTime);
                        // 判断时间偏移
                        if (videoTS > recorder.getTimestamp()) {
                            // LogUtils.info("矫正时间戳: " + videoTS + " : " +
                            // recorder.getTimestamp() + "
                            // -> "
                            // + (videoTS - recorder.getTimestamp()));
                            recorder.setTimestamp((videoTS));
                        }
                        recorder.record(frame);
                    }
                }
            } catch (Exception e) {
                grabberStatus = false;
                MediaService.cameras.remove(cameraDto.getMediaKey());
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                recorderStatus = false;
                MediaService.cameras.remove(cameraDto.getMediaKey());
            }

            if (bos.size() > 0) {
                byte[] b = bos.toByteArray();
                bos.reset();

                // 发送视频到前端
                sendFrameData(b);
            }
        }

        // 启动失败，直接关闭， close包含stop和release方法。录制文件必须保证最后执行stop()方法
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
            closeMedia();
        }
        LogUtils.info("关闭媒体流-javacv，{} ", cameraDto.getUrl());
    }

    /**
     * 发送帧数据
     *
     * @param data
     */
    private void sendFrameData(byte[] data) {
        // ws
        for (Entry<String, ChannelHandlerContext> entry : wsClients.entrySet()) {
            if (entry.getValue().channel().isWritable()) {
                entry.getValue().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(data)));
            } else {
                wsClients.remove(entry.getKey());
                hasClient();
            }
        }
        // http
        for (Entry<String, ChannelHandlerContext> entry : httpClients.entrySet()) {
            if (entry.getValue().channel().isWritable()) {
                entry.getValue().writeAndFlush(Unpooled.copiedBuffer(data));
            } else {
                httpClients.remove(entry.getKey());
                hasClient();
            }
        }
    }

    /**
     * 判断有没有客户端，关闭流
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

        // 无需自动关闭
        if (!cameraDto.isAutoClose()) {
            return;
        }

        if (httpClients.isEmpty() && wsClients.isEmpty()) {
            // 等待20秒还没有客户端，则关闭推流
            if (noClient > cameraDto.getNoClientsDuration()) {
                closeMedia();
            } else {
                noClient += 1000;
                //				log.info("\r\n{}\r\n {} 秒自动关闭推拉流 \r\n", camera.getUrl(),
                // noClientsDuration-noClient);
            }
        } else {
            // 重置计时
            noClient = 0;
        }
    }

    /** 监听客户端，用于判断无人观看时自动关闭推流 */
    public void listenClient() {
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    hasClient();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        listenThread.start();
    }

    /** 重连，目前重连有些问题，停止后时间戳也变化了，发现相机连不上先直接断开，清除缓存，后续再优化 */
    //	private void doReConnect() {
    //		try {
    //			boolean createGrabber = createGrabber();
    //			while (!createGrabber) {
    //				try {
    //					Thread.sleep(5000);
    //				} catch (InterruptedException e) {
    //				}
    //				log.info("\r\n{}\r\n尝试重连>>>", camera.getUrl());
    //				createGrabber = createGrabber();
    //			}
    //
    //		} finally {
    //			try {
    //				grabber.flush();
    //			} catch (Exception e) {
    //				running = false;
    //				MediaService.cameras.remove(camera.getMediaKey());
    //			}
    //		}
    //		log.info("\r\n{}\r\n重连成功", camera.getUrl());
    //	}

    /** 关闭流媒体 */
    private void closeMedia() {
        running = false;
        MediaService.cameras.remove(cameraDto.getMediaKey());

        // 媒体异常时，主动断开前端长连接
        for (Entry<String, ChannelHandlerContext> entry : wsClients.entrySet()) {
            try {
                entry.getValue().close();
            } finally {
                wsClients.remove(entry.getKey());
            }
        }
        for (Entry<String, ChannelHandlerContext> entry : httpClients.entrySet()) {
            try {
                entry.getValue().close();
            } finally {
                httpClients.remove(entry.getKey());
            }
        }
    }

    /**
     * 新增客户端
     *
     * @param ctx netty client
     * @param ctype enum,ClientType
     */
    public void addClient(ChannelHandlerContext ctx, ClientType ctype) {
        int timeout = 0;
        while (true) {
            if (header != null) {
                if (ctx.channel().isWritable()) {
                    // 发送帧前先发送header
                    if (ClientType.HTTP.getType() == ctype.getType()) {
                        ChannelFuture future = ctx.writeAndFlush(Unpooled.copiedBuffer(header));
                        future.addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()) {
                                    httpClients.put(ctx.channel().id().toString(), ctx);
                                }
                            }
                        });
                    } else if (ClientType.WEBSOCKET.getType() == ctype.getType()) {
                        ChannelFuture future =
                                ctx.writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(header)));
                        future.addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()) {
                                    wsClients.put(ctx.channel().id().toString(), ctx);
                                }
                            }
                        });
                    }
                }

                break;
            }

            // 等待推拉流启动
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                LogUtils.error(e);
            }
            // 启动录制器失败
            timeout += 50;
            if (timeout > 30000) {
                break;
            }
        }
    }

    @Override
    public void run() {
        transferStream2Flv();
    }
}
