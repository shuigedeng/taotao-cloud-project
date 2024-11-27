package com.taotao.cloud.media.biz.streammediaservices;

/**
 * 通过 javacv 将 媒体转流封装为flv格式
 * <p/>
 * 支持转复用和转码
 * <p/>
 * 转复用 流来源是视频H264格式,音频AAC格式
 * <p/>
 * 转码  流来源不是视频H264格式,音频AAC格式 转码为视频H264格式,音频AAC格式
 *
 * @author LGC
 */
@Slf4j
public class MediaTransferFlvByJavacv implements MediaTransfer, Runnable {
    static {
        // 设置日志级别
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();
    }

    /**
     * 相机
     */
    private final CameraDTO camera;
    /**
     * http客户端
     */
    private final ConcurrentHashMap<String, ChannelHandlerContext> httpClients = new ConcurrentHashMap<>();
    /**
     * ws客户端
     */
    private final ConcurrentHashMap<String, ChannelHandlerContext> wsClients = new ConcurrentHashMap<>();
    /**
     * 运行状态
     */
    private volatile boolean running = false;
    /**
     * 拉流器创建状态
     */
    private boolean grabberStatus = false;
    /**
     * 推流录制器创建状态
     */
    private boolean recorderStatus = false;
    /**
     * 拉流器
     */
    private FFmpegFrameGrabber grabber;
    /**
     * 推流录制器
     */
    private FFmpegFrameRecorder recorder;
    /**
     * true:转复用,false:转码 默认转码
     */
    private boolean transferFlag = false;
    /**
     * flv header 转FLV格式的头信息 如果有第二个客户端播放首先要返回头信息
     */
    private byte[] header = null;
    /**
     * 视频输出流
     */
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    /**
     * 当前http连接 当前ws连接 在线人数
     */
    private int hcSize, wcSize = 0;

    /**
     * 没有客户端时候的计时
     */
    private long noClient = 0;


    public MediaTransferFlvByJavacv(CameraDTO camera) {
        super();
        this.camera = camera;
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

    public boolean isRecorderStatus() {
        return recorderStatus;
    }

    /**
     * 创建拉流器
     */
    private boolean createGrabber() {
        // 拉流器
        grabber = new FFmpegFrameGrabber(camera.getUrl());
        // 像素格式
        grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        // 超时时间(15秒)
        grabber.setOption("stimeout", camera.getNetTimeout());
//        grabber.setOption("threads", "1"); //线程数
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1024000");
        // 读写超时，适用于所有协议的通用读写超时
        grabber.setOption("rw_timeout", camera.getReadOrWriteTimeout());
        // 探测视频流信息，为空默认5000000微秒
        grabber.setOption("probesize", camera.getReadOrWriteTimeout());
        // 解析视频流信息，为空默认5000000微秒
        grabber.setOption("analyzeduration", camera.getReadOrWriteTimeout());

        // 如果为rtsp流，增加配置
        if ("rtsp".equals(camera.getUrl().substring(0, 4))) {
            // 设置打开协议tcp / udp
            grabber.setOption("rtsp_transport", "tcp");
            // 首选TCP进行RTP传输
            grabber.setOption("rtsp_flags", "prefer_tcp");

        } else if ("rtmp".equals(camera.getUrl().substring(0, 4))) {
            // rtmp拉流缓冲区，默认3000毫秒
            grabber.setOption("rtmp_buffer", "1000");
            // 默认rtmp流为直播模式，不允许seek
            grabber.setOption("rtmp_live", "live");
        }
        try {
            grabber.start();
            log.info("\r\n{}\r\n启动拉流器成功", camera.getUrl());
            return grabberStatus = true;
        } catch (Exception e) {
            MediaService.mediaTransferMap.remove(camera.getMediaKey());
            log.error("\r\n{}\r\n启动拉流器失败，网络超时或视频源不可用", camera.getUrl(), e);
        }
        return grabberStatus = false;
    }

    /**
     * 创建转码推流录制器
     */
    private boolean createRecorder() {
        recorder = new FFmpegFrameRecorder(byteArrayOutputStream, grabber.getImageWidth(), grabber.getImageHeight(),
                grabber.getAudioChannels());
        recorder.setFormat("flv"); // 视频封装格式
        if (!transferFlag) {
            // 转码
            log.info("转码模式");
            recorder.setInterleaved(true);//允许交叉
            recorder.setVideoOption("tune", "zerolatency");// 编码延时 zerolatency(零延迟)
            recorder.setVideoOption("preset", "ultrafast");// 编码速度 ultrafast(极快) fast(快)
            recorder.setVideoOption("crf", "28");// 编码质量 有效范围为0到63，数字越大表示质量越低，从主观上讲，18~28是一个合理的范围。18被认为是视觉无损的
//            recorder.setVideoOption("threads", "1"); // 线程数
            recorder.setFrameRate(25);// 设置帧率
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setGopSize(50);// 设置gop,与帧率相同，相当于间隔1秒一个关键帧
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // 像素格式
            recorder.setVideoBitrate(grabber.getVideoBitrate());// 视频码率
//   recorder.setVideoBitrate(600 * 1000);// 码率600kb/s
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 视频编码
            recorder.setVideoBitrate(grabber.getVideoBitrate()); // 视频比特率
            if (grabber.getAudioChannels() > 0) {
                recorder.setAudioChannels(grabber.getAudioChannels());
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            }
            // 启用RDOQ算法，优化视频质量 1：在视频码率和视频质量之间取得平衡 2：最大程度优化视频质量（会降低编码速度和提高码率）
            recorder.setTrellis(1);
            recorder.setMaxDelay(0);// 设置延迟
            try {
                recorder.start();
                log.info("\r\n{}\r\n启动转码录制器成功", camera.getUrl());
                return recorderStatus = true;
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
                log.error("启动转码录制器失败", e1);
                MediaService.mediaTransferMap.remove(camera.getMediaKey());
            }
        } else {
            // 转复用
            log.info("转复用模式");
            // 不让recorder关联关闭outputStream
            recorder.setCloseOutputStream(false);
            try {
                recorder.start(grabber.getFormatContext());
                log.info("\r\n{}\r\n启动转复用录制器成功", camera.getUrl());
                return recorderStatus = true;
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                log.warn("\r\n{}\r\n启动转复用录制器失败", camera.getUrl());
                // 如果转复用失败，则自动切换到转码模式
                transferFlag = false;
                log.info("转复用失败，自动切换到转码模式");
                if (recorder != null) {
                    try {
                        recorder.close();
                    } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
                    }
                }
                if (createRecorder()) {
                    log.error("\r\n{}\r\n切换到转码模式", camera.getUrl());
                    return true;
                }
                log.error("\r\n{}\r\n切换转码模式失败", camera.getUrl(), e);
            }
        }
        return recorderStatus = false;
    }

    /**
     * 是否支持Flv格式编解码器
     */
    private boolean supportFlvFormatCodec() {
        int videoCodec = grabber.getVideoCodec();
        int audioCodec = grabber.getAudioCodec();
        return (camera.getType() == 0)
                && (avcodec.AV_CODEC_ID_H264 == videoCodec)
                && (avcodec.AV_CODEC_ID_AAC == audioCodec || grabber.getAudioChannels() == 0);
    }

    /**
     * 发送帧数据
     */
    private void sendFrameData(byte[] data) {
        // http
        for (Entry<String, ChannelHandlerContext> entry : httpClients.entrySet()) {
            try {
                entry.getValue().writeAndFlush(Unpooled.copiedBuffer(data));
            } catch (java.lang.Exception e) {
                httpClients.remove(entry.getKey());
                hasClient();
                log.error("关闭 http 客户端异常", e);
            }
        }
        // ws
        for (Entry<String, ChannelHandlerContext> entry : wsClients.entrySet()) {
            try {
                entry.getValue().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(data)));
            } catch (java.lang.Exception e) {
                wsClients.remove(entry.getKey());
                hasClient();
                log.error("关闭 ws 客户端异常", e);
            }
        }

    }

    /**
     * 将视频流转换为flv
     */
    private void transferStream2Flv() {
        if (!createGrabber()) {
            return;
        }
        transferFlag = supportFlvFormatCodec();
        if (!createRecorder()) {
            return;
        }

        try {
            grabber.flush();
        } catch (Exception e) {
            log.info("清空拉流器缓存失败", e);
        }
        if (header == null) {
            header = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
        }

        running = true;

        // 启动监听线程 自动关闭推流
        listenClient();

        int nullNumber = 0;
        for (; running && grabberStatus && recorderStatus; ) {
            try {
                // 转复用
                if (transferFlag) {
                    // 判断读空包
                    AVPacket pkt = grabber.grabPacket();
                    if (null != pkt && !pkt.isNull()) {
                        recorder.recordPacket(pkt);
                        // 发送数据帧到客户端
                        if (byteArrayOutputStream.size() > 0) {
                            byte[] b = byteArrayOutputStream.toByteArray();
                            byteArrayOutputStream.reset();
                            // 发送帧数据
                            sendFrameData(b);
                        }
                        avcodec.av_packet_unref(pkt);
                    } else {
                        nullNumber++;
                        log.info("转复用空包：{},url:{}", nullNumber, camera.getUrl());
                        if (nullNumber > 200) {
                            break;
                        }
                    }
                }
                // 转码
                else {
                    // 判断读空包
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        recorder.record(frame);
                        // 发送数据帧到客户端
                        if (byteArrayOutputStream.size() > 0) {
                            byte[] b = byteArrayOutputStream.toByteArray();
                            byteArrayOutputStream.reset();
                            // 发送帧数据
                            sendFrameData(b);
                        }
                    } else {
                        nullNumber++;
                        log.info("转码空包：{},url:{}", nullNumber, camera.getUrl());
                        if (nullNumber > 200) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("转码或转复用异常", e);
                grabberStatus = false;
                MediaService.mediaTransferMap.remove(camera.getMediaKey());
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                log.error("转码或转复用异常", e);
                recorderStatus = false;
                MediaService.mediaTransferMap.remove(camera.getMediaKey());
            }
        }
        try {
            recorder.close();
            grabber.close();
            byteArrayOutputStream.close();
        } catch (java.lang.Exception e) {
            log.error("关闭媒体流Exception", e);
        } finally {
            closeMedia();
        }
        log.info("关闭媒体流 url，{} ", camera.getUrl());
    }


    /**
     * 判断有没有客户端， 自动关闭推流
     */
    private void hasClient() {
        int newHcSize = httpClients.size();
        int newWcSize = wsClients.size();
        if (hcSize != newHcSize || wcSize != newWcSize) {
            hcSize = newHcSize;
            wcSize = newWcSize;
            log.info("\r\n{}\r\nhttp连接数：{}, ws连接数：{} \r\n", camera.getUrl(), newHcSize, newWcSize);
        }

        // 无需自动关闭
        if (!camera.isAutoClose()) {
            return;
        }

        if (httpClients.isEmpty() && wsClients.isEmpty()) {
            // 等待1分钟还没有客户端，则关闭推流
            if (noClient > camera.getAutoCloseDuration()) {
                closeMedia();
            } else {
                // 增加1秒
                noClient += 1;
                log.info("\r\n{}\r\n {} 秒后自动关闭推拉流 \r\n", camera.getUrl(), camera.getAutoCloseDuration() - noClient);
            }
        } else {
            // 重置计时
            noClient = 0;
        }
    }

    /**
     * 监听客户端，自动关闭推流
     */
    private void listenClient() {
        /**
         * 监听线程，用于监听状态
         */
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                hasClient();
            }
        }).start();
    }


    /**
     * 关闭流媒体
     */
    private void closeMedia() {
        running = false;
        MediaService.mediaTransferMap.remove(camera.getMediaKey());
        // 媒体异常时，主动断开前端长连接
        for (Entry<String, ChannelHandlerContext> entry : httpClients.entrySet()) {
            try {
                entry.getValue().close();
            } finally {
                httpClients.remove(entry.getKey());
            }
        }
        for (Entry<String, ChannelHandlerContext> entry : wsClients.entrySet()) {
            try {
                entry.getValue().close();
            } finally {
                wsClients.remove(entry.getKey());
            }
        }
    }


    /**
     * 新增客户端
     */
    public void addClient(ChannelHandlerContext ctx, ClientType clientType) {
        int timeout = 0;
        while (true) {
            try {
                if (header != null) {
                    try {
                        // 发送帧前先发送header
                        if (ClientType.HTTP.getType() == clientType.getType()) {
                            ChannelFuture future = ctx.writeAndFlush(Unpooled.copiedBuffer(header));
                            future.addListener(future1 -> {
                                if (future1.isSuccess()) {
                                    httpClients.put(ctx.channel().id().toString(), ctx);
                                }
                            });
                        } else if (ClientType.WEBSOCKET.getType() == clientType.getType()) {
                            ChannelFuture future = ctx
                                    .writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(header)));
                            future.addListener(future12 -> {
                                if (future12.isSuccess()) {
                                    wsClients.put(ctx.channel().id().toString(), ctx);
                                }
                            });
                        }

                    } catch (java.lang.Exception e) {
                        log.info("添加客户端异常", e);
                    }
                    break;
                }

                // 等待推拉流启动
                Thread.sleep(50);
                // 启动录制器失败
                timeout += 50;
                if (timeout > 30000) {
                    break;
                }
            } catch (java.lang.Exception e) {
                log.error("添加客户端异常", e);
            }
        }
    }


    /**
     * 移除客户端
     */
    public void removeClient(ChannelHandlerContext ctx, ClientType clientType) {
        if (ClientType.HTTP.getType() == clientType.getType()) {
            httpClients.remove(ctx.channel().id().toString(), ctx);
            hasClient();
        } else if (ClientType.WEBSOCKET.getType() == clientType.getType()) {
            wsClients.remove(ctx.channel().id().toString(), ctx);
            hasClient();
        }
        ctx.close();

    }


    @Override
    public void run() {
        transferStream2Flv();
    }

}
