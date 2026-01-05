package com.taotao.cloud.media.biz.ffmpeg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ffmpeg")
@Data
public class FFmpegConfig {
    /**
     * FFmpeg可执行文件路径
     * Windows: "C:/ffmpeg/bin/ffmpeg.exe"
     * Linux/Mac: "/usr/bin/ffmpeg"
     */
    private String path;
    
    /**
     * 超时时间（秒）
     * 防止视频处理变成“永恒等待”
     */
    private Long timeout = 3600L;
    
    /**
     * 线程数
     * 多线程就像多双手，干活更快！
     */
    private Integer threads = 4;
}
