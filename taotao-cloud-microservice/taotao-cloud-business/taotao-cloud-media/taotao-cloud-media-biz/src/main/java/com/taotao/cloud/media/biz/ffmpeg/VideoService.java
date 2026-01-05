package com.taotao.cloud.media.biz.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VideoService {
    
    @Autowired
    private FFmpegCommander ffmpegCommander;
    
    // 临时文件存放目录（像快递的临时存放点）
    private final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/video-process/";
    
    public VideoService() {
        // 确保临时目录存在
        new File(TEMP_DIR).mkdirs();
    }
    
    /**
     * 转换视频格式（像把中文翻译成英文）
     * @param inputFile 输入文件
     * @param targetFormat 目标格式（mp4, avi, mov...）
     */
    public File convertFormat(MultipartFile inputFile, String targetFormat) throws IOException {
        log.info("开始格式转换：{} → {}", 
                 getFileExtension(inputFile.getOriginalFilename()), 
                 targetFormat);
        
        // 1. 保存上传的文件（像把食材先放到厨房）
        File input = saveTempFile(inputFile);
        
        // 2. 准备输出文件（准备好盘子）
        String outputFileName = UUID.randomUUID() + "." + targetFormat;
        File output = new File(TEMP_DIR + outputFileName);
        
        // 3. 构建FFmpeg命令菜单
        List<String> commands = Arrays.asList(
            "-i", input.getAbsolutePath(),     // 输入文件
            "-threads", "4",                   // 用4个线程
            "-preset", "fast",                 // 快速预设
            "-c:v", "libx264",                 // 视频编码
            "-c:a", "aac",                     // 音频编码
            "-y",                              // 覆盖输出文件（别问我是否确定）
            output.getAbsolutePath()           // 输出文件
        );
        
        // 4. 让FFmpeg大厨开始烹饪
        boolean success = ffmpegCommander.execute(commands);
        
        // 5. 清理临时文件（洗盘子）
        input.delete();
        
        if (success && output.exists()) {
            log.info("格式转换成功！文件大小：{} MB", 
                     output.length() / (1024 * 1024));
            return output;
        } else {
            throw new RuntimeException("转换失败，FFmpeg可能去做美甲了");
        }
    }
    
    /**
     * 提取视频缩略图（给视频拍证件照）
     */
    public File extractThumbnail(MultipartFile videoFile, int second) throws IOException {
        log.info("正在给视频拍第{}秒的证件照...", second);
        
        File input = saveTempFile(videoFile);
        String outputFileName = UUID.randomUUID() + ".jpg";
        File output = new File(TEMP_DIR + outputFileName);
        
        List<String> commands = Arrays.asList(
            "-i", input.getAbsolutePath(),
            "-ss", String.valueOf(second),    // 跳转到指定秒数
            "-vframes", "1",                  // 只要1帧
            "-vf", "scale=320:-1",           // 缩放到宽度320，高度自动
            "-y",
            output.getAbsolutePath()
        );
        
        boolean success = ffmpegCommander.execute(commands);
        input.delete();
        
        if (success && output.exists()) {
            log.info("缩略图生成成功！");
            return output;
        }
        throw new RuntimeException("拍照失败，视频可能害羞了");
    }
    
    /**
     * 压缩视频（给视频减肥）
     */
    public File compressVideo(MultipartFile videoFile, int targetBitrate) throws IOException {
        log.info("开始给视频减肥，目标比特率：{}k", targetBitrate);
        
        File input = saveTempFile(videoFile);
        long originalSize = input.length();
        
        String outputFileName = UUID.randomUUID() + "_compressed.mp4";
        File output = new File(TEMP_DIR + outputFileName);
        
        List<String> commands = Arrays.asList(
            "-i", input.getAbsolutePath(),
            "-threads", "4",
            "-b:v", targetBitrate + "k",      // 目标视频比特率
            "-b:a", "128k",                   // 音频比特率
            "-y",
            output.getAbsolutePath()
        );
        
        boolean success = ffmpegCommander.execute(commands);
        input.delete();
        
        if (success && output.exists()) {
            long compressedSize = output.length();
            double ratio = (1.0 - (double)compressedSize/originalSize) * 100;
            log.info("减肥成功！原大小：{}MB，现大小：{}MB，瘦身：{:.1f}%",
                     originalSize/(1024*1024),
                     compressedSize/(1024*1024),
                     ratio);
            return output;
        }
        throw new RuntimeException("减肥失败，视频可能偷吃宵夜了");
    }
    
    /**
     * 合并视频和音频（像给电影配音）
     */
    public File mergeVideoAudio(MultipartFile videoFile, 
                                MultipartFile audioFile) throws IOException {
        log.info("开始给视频配音...");
        
        File video = saveTempFile(videoFile);
        File audio = saveTempFile(audioFile);
        
        String outputFileName = UUID.randomUUID() + "_merged.mp4";
        File output = new File(TEMP_DIR + outputFileName);
        
        List<String> commands = Arrays.asList(
            "-i", video.getAbsolutePath(),
            "-i", audio.getAbsolutePath(),
            "-c:v", "copy",                   // 视频流直接复制（不重新编码）
            "-c:a", "aac",                    // 音频重新编码
            "-map", "0:v:0",                  // 取第一个文件的视频
            "-map", "1:a:0",                  // 取第二个文件的音频
            "-shortest",                      // 以最短的流为准
            "-y",
            output.getAbsolutePath()
        );
        
        boolean success = ffmpegCommander.execute(commands);
        video.delete();
        audio.delete();
        
        if (success && output.exists()) {
            log.info("配音成功！新视频诞生了");
            return output;
        }
        throw new RuntimeException("合并失败，可能视频和音频在闹离婚");
    }
    
    private File saveTempFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(TEMP_DIR + fileName);
        Files.copy(file.getInputStream(), path);
        return path.toFile();
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "unknown";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
