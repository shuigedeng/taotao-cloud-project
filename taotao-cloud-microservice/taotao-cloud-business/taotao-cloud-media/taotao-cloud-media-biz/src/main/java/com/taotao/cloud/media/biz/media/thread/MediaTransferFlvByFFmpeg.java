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
import com.taotao.cloud.media.biz.media.common.MediaConstant;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import com.taotao.cloud.media.biz.media.service.MediaService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.bytedeco.javacv.FrameGrabber.Exception;

/**
 * 使用ffmpeg推拉流，可以说无敌了
 *
 * <p>优点：支持各种杂七杂八的流，兼容性比较好，稳定，不容易出错，自身带有重连机制，可以自己使用命令封装 缺点：系统会存在多个ffmpeg进程, 无法直接操作帧，延迟优化没javacv方便
 */
public class MediaTransferFlvByFFmpeg extends MediaTransfer {

    /** 相机 */
    private CameraDto cameraDto;

    private List<String> command = new ArrayList<>();

    private ServerSocket tcpServer = null;

    private Process process;
    private Thread inputThread;
    private Thread errThread;
    private Thread outputThread;
    private Thread listenThread;
    private boolean running = false; // 启动
    private boolean enableLog = true;

    // 记录当前
    long currentTimeMillis = System.currentTimeMillis();

    public MediaTransferFlvByFFmpeg(final String executable) {
        command.add(executable);
        buildCommand();
    }

    public MediaTransferFlvByFFmpeg(CameraDto cameraDto) {
        command.add(System.getProperty(MediaConstant.ffmpegPathKey));
        this.cameraDto = cameraDto;
        buildCommand();
    }

    public MediaTransferFlvByFFmpeg(final String executable, CameraDto cameraDto) {
        command.add(executable);
        this.cameraDto = cameraDto;
        buildCommand();
    }

    public MediaTransferFlvByFFmpeg(final String executable, CameraDto cameraDto, boolean enableLog) {
        command.add(executable);
        this.cameraDto = cameraDto;
        this.enableLog = enableLog;
        buildCommand();
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private MediaTransferFlvByFFmpeg addArgument(String argument) {
        command.add(argument);
        return this;
    }

    /**
     * 构建ffmpeg转码命令,新版javacv移除libx264，使用libopenh264 查看显卡硬件加速支持的选项ffmpeg -hwaccels 查看ffmpeg支持选项
     * linux：ffmpeg -codecs | grep cuvid， window：ffmpeg -codecs | findstr cuvid h264_nvenc ffmpeg
     * -hwaccel cuvid -c:v h264_cuvid -rtsp_transport tcp -i "rtsp地址" -c:v h264_nvenc -b:v 500k -vf
     * scale_npp=1280:-1 -y /home/2.mp4
     *
     * <p>-hwaccel cuvid：指定使用cuvid硬件加速 -c:v h264_cuvid：使用h264_cuvid进行视频解码 -c:v
     * h264_nvenc：使用h264_nvenc进行视频编码 -vf scale_npp=1280:-1：指定输出视频的宽高，注意，这里和软解码时使用的-vf scale=x:x不一样
     *
     * <p>转码期间nvidia-smi查看显卡状态 -hwaccel_device N 指定某颗GPU执行转码任务
     */
    private void buildCommand() {
        // 如果为rtsp流，增加配置
        if ("rtsp".equals(cameraDto.getUrl().substring(0, 4))) {
            this.addArgument("-rtsp_transport").addArgument("tcp");
        }
        this.addArgument("-i")
                .addArgument(cameraDto.getUrl())
                .addArgument("-max_delay")
                .addArgument("1")
                //		.addArgument("-strict").addArgument("experimental")
                .addArgument("-g")
                .addArgument("25")
                .addArgument("-r")
                .addArgument("25")
                //		.addArgument("-b").addArgument("200000")
                //		.addArgument("-filter_complex").addArgument("setpts='(RTCTIME - RTCSTART) / (TB
                // * 1000000)'")
                .addArgument("-c:v")
                .addArgument("libopenh264")
                .addArgument("-preset:v")
                .addArgument("ultrafast")
                //		.addArgument("-preset:v").addArgument("fast")
                .addArgument("-tune:v")
                .addArgument("zerolatency")
                //		.addArgument("-crf").addArgument("26")
                .addArgument("-c:a")
                .addArgument("aac")
                //		.addArgument("-qmin").addArgument("28")
                //		.addArgument("-qmax").addArgument("32")
                //		.addArgument("-b:v").addArgument("448k")
                //		.addArgument("-b:a").addArgument("64k")
                .addArgument("-f")
                .addArgument("flv");
    }

    /** 转封装命令 */
    private void buildCopyCommand() {
        this.addArgument("-rtsp_transport")
                .addArgument("tcp")
                .addArgument("-i")
                .addArgument(cameraDto.getUrl())
                .addArgument("-max_delay")
                .addArgument("1")
                .addArgument("-g")
                .addArgument("25")
                .addArgument("-r")
                .addArgument("25")
                .addArgument("-c:v")
                .addArgument("copy")
                .addArgument("-c:a")
                .addArgument("copy")
                .addArgument("-f")
                .addArgument("flv");
    }

    //	private void buildCommand() {
    //		this
    ////		.addArgument("-rtsp_transport").addArgument("tcp")
    //		.addArgument("-i").addArgument(camera.getUrl())
    //		.addArgument("-max_delay").addArgument("100")
    ////		.addArgument("-strict").addArgument("experimental")
    //		.addArgument("-g").addArgument("10")
    ////		.addArgument("-r").addArgument("25")
    ////		.addArgument("-b").addArgument("200000")
    ////		.addArgument("-filter_complex").addArgument("setpts='(RTCTIME - RTCSTART) / (TB *
    // 1000000)'")
    //		.addArgument("-c:v").addArgument("libx264")
    //		.addArgument("-preset:v").addArgument("ultrafast")
    //		.addArgument("-tune:v").addArgument("zerolatency")
    ////		.addArgument("-crf").addArgument("26")
    //		.addArgument("-c:a").addArgument("aac")
    //		.addArgument("-qmin").addArgument("28")
    //		.addArgument("-qmax").addArgument("32")
    //		.addArgument("-b:v").addArgument("448k")
    //		.addArgument("-b:a").addArgument("64k")
    //		.addArgument("-f").addArgument("flv");
    //	}

    /**
     * 执行推流
     *
     * @return
     */
    public MediaTransferFlvByFFmpeg execute() {
        String output = getOutput();
        command.add(output);

        String join = CollUtil.join(command, " ");
        LogUtils.info(join);
        try {
            process = new ProcessBuilder(command).start();
            running = true;
            listenNetTimeout();
            dealStream(process);
            outputData();
            listenClient();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return this;
    }

    /** flv数据 */
    private void outputData() {
        outputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket client = null;
                try {
                    client = tcpServer.accept();
                    DataInputStream input = new DataInputStream(client.getInputStream());

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    //					ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while (running) {

                        len = input.read(buffer);
                        if (len == -1) {
                            break;
                        }

                        bos.write(buffer, 0, len);

                        if (header == null) {
                            header = bos.toByteArray();
                            //
                            //	LogUtils.info(HexUtil.encodeHexStr(header));
                            bos.reset();
                            continue;
                        }

                        // 帧数据
                        byte[] data = bos.toByteArray();
                        bos.reset();

                        // 发送到前端
                        sendFrameData(data);
                    }

                    try {
                        client.close();
                    } catch (Exception e) {
                    }
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                    try {
                        bos.close();
                    } catch (Exception e) {
                    }

                    LogUtils.info("关闭媒体流-ffmpeg，{} ", cameraDto.getUrl());

                } catch (SocketTimeoutException e1) {
                    //					e1.printStackTrace();
                    //					超时关闭
                } catch (IOException e) {
                    //					LogUtils.error(e);
                } finally {
                    MediaService.cameras.remove(cameraDto.getMediaKey());
                    running = false;
                    process.destroy();
                    try {
                        if (null != client) {
                            client.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (null != tcpServer) {
                            tcpServer.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });

        outputThread.start();
    }

    /** 监听客户端 */
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

    /** 监听网络异常超时 */
    public void listenNetTimeout() {
        Thread listenNetTimeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if ((System.currentTimeMillis() - currentTimeMillis) > 15000) {
                        LogUtils.info("网络异常超时");
                        MediaService.cameras.remove(cameraDto.getMediaKey());
                        stopFFmpeg();
                        break;
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        listenNetTimeoutThread.setDaemon(true);
        listenNetTimeoutThread.start();
    }

    public static MediaTransferFlvByFFmpeg atPath() {
        return atPath(null);
    }

    public static MediaTransferFlvByFFmpeg atPath(final String absPath) {
        final String executable;
        if (absPath != null) {
            executable = absPath;
        } else {
            //			executable = "ffmpeg";
            executable = System.getProperty(MediaConstant.ffmpegPathKey);
        }
        return new MediaTransferFlvByFFmpeg(executable);
    }

    /**
     * 控制台输出
     *
     * @param process
     */
    private void dealStream(Process process) {
        if (process == null) {
            return;
        }
        // 处理InputStream的线程
        inputThread = new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while (running) {
                        line = in.readLine();
                        currentTimeMillis = System.currentTimeMillis();
                        if (line == null) {
                            break;
                        }
                        if (enableLog) {
                            LogUtils.info("output: " + line);
                        }
                    }
                } catch (IOException e) {
                    LogUtils.error(e);
                } finally {
                    try {
                        running = false;
                        in.close();
                    } catch (IOException e) {
                        LogUtils.error(e);
                    }
                }
            }
        };
        // 处理ErrorStream的线程
        errThread = new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while (running) {
                        line = err.readLine();
                        currentTimeMillis = System.currentTimeMillis();
                        if (line == null) {
                            break;
                        }
                        if (enableLog) {
                            LogUtils.info("ffmpeg: " + line);
                        }
                    }
                } catch (IOException e) {
                    LogUtils.error(e);
                } finally {
                    try {
                        running = false;
                        err.close();
                    } catch (IOException e) {
                        LogUtils.error(e);
                    }
                }
            }
        };

        inputThread.start();
        errThread.start();
    }

    /**
     * 输出到tcp
     *
     * @return
     */
    private String getOutput() {
        try {
            tcpServer = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
            StringBuffer sb = new StringBuffer();
            sb.append("tcp://");
            sb.append(tcpServer.getInetAddress().getHostAddress());
            sb.append(":");
            sb.append(tcpServer.getLocalPort());
            tcpServer.setSoTimeout(10000);
            return sb.toString();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        new RuntimeException("无法启用端口");
        return "";
    }

    /** 关闭 */
    public void stopFFmpeg() {
        this.running = false;
        this.process.destroy();
        LogUtils.info("关闭媒体流-ffmpeg，{} ", cameraDto.getUrl());

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

        // 无需自动关闭
        if (!cameraDto.isAutoClose()) {
            return;
        }

        if (httpClients.isEmpty() && wsClients.isEmpty()) {
            // 等待20秒还没有客户端，则关闭推流
            if (noClient > cameraDto.getNoClientsDuration()) {
                running = false;
                MediaService.cameras.remove(cameraDto.getMediaKey());
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

    public static void main(String[] args) throws Exception {
        //		ServerSocket serverSocket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
        //		LogUtils.info(serverSocket.getLocalPort());
        //		LogUtils.info(serverSocket.getInetAddress().getHostAddress());

        MediaTransferFlvByFFmpeg.atPath()
                .addArgument("-i")
                .addArgument("rtsp://admin:VZCDOY@192.168.2.84:554/Streaming/Channels/102")
                .addArgument("-g")
                .addArgument("5")
                .addArgument("-c:v")
                .addArgument("libx264")
                .addArgument("-c:a")
                .addArgument("aac")
                .addArgument("-f")
                .addArgument("flv")
                .execute();
    }
}
