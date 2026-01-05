package com.taotao.cloud.media.biz.ffmpeg;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/video")
public class VideoController {
    
    @Autowired
    private VideoService videoService;
    
    @Autowired
    private FFmpegCommander ffmpegCommander;
    
    @GetMapping("/version")
    public String getFFmpegVersion() {
        String version = ffmpegCommander.getVersion();
        return "{\"version\": \"" + version + "\"}";
    }
    
    @PostMapping("/convert")
    public ResponseEntity<Resource> convertFormat(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format,
            HttpServletResponse response) throws IOException {
        
        log.info("收到转换请求：{} → {}", file.getOriginalFilename(), format);
        
        File converted = videoService.convertFormat(file, format);
        
        return buildFileResponse(converted, 
                "converted." + format, 
                MediaType.APPLICATION_OCTET_STREAM);
    }
    
    @PostMapping("/thumbnail")
    public ResponseEntity<Resource> extractThumbnail(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "second", defaultValue = "5") int second) throws IOException {
        
        File thumbnail = videoService.extractThumbnail(file, second);
        
        return buildFileResponse(thumbnail,
                "thumbnail.jpg",
                MediaType.IMAGE_JPEG);
    }
    
    @PostMapping("/compress")
    public ResponseEntity<Resource> compressVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bitrate", defaultValue = "1000") int bitrate) throws IOException {
        
        File compressed = videoService.compressVideo(file, bitrate);
        
        return buildFileResponse(compressed,
                "compressed.mp4",
                MediaType.APPLICATION_OCTET_STREAM);
    }
    
    @PostMapping("/merge")
    public ResponseEntity<Resource> mergeVideoAudio(
            @RequestParam("video") MultipartFile video,
            @RequestParam("audio") MultipartFile audio) throws IOException {
        
        File merged = videoService.mergeVideoAudio(video, audio);
        
        return buildFileResponse(merged,
                "merged.mp4",
                MediaType.APPLICATION_OCTET_STREAM);
    }
    
    private ResponseEntity<Resource> buildFileResponse(File file, 
                                                       String filename,
                                                       MediaType mediaType) {
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        // 文件下载完成后自动删除（深藏功与名）
        file.deleteOnExit();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }
}
