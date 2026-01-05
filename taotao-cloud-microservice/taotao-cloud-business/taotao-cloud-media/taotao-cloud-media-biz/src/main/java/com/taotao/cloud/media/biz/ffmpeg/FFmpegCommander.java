package com.taotao.cloud.media.biz.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FFmpegCommander {

	@Autowired
	private FFmpegConfig ffmpegConfig;

	public List<File> batchConvert( List<MultipartFile> files, String format ) {
		return files.parallelStream()  // 并行处理，更快！
			.map(file -> {
				try {
					return videoService.convertFormat(file, format);
				} catch (IOException e) {
					log.error("转换失败：{}", file.getOriginalFilename(), e);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public Map<String, Object> getVideoInfo( File videoFile ) {
		// 使用FFprobe（FFmpeg的小伙伴）获取视频信息
		List<String> commands = Arrays.asList(
			"-v", "error",
			"-select_streams", "v:0",
			"-show_entries", "stream=width,height,duration,bit_rate,codec_name",
			"-of", "json",
			videoFile.getAbsolutePath()
		);

		// 执行命令并解析JSON结果
		// 返回包含分辨率、时长、码率、编码格式等信息
	}

	// 在FFmpegCommander中添加进度解析
	private void parseProgress( String line, ProgressListener listener ) {
		// 解析FFmpeg的输出，提取进度信息
		// 示例输出：frame=  123 fps=25.1 time=00:00:04.92 bitrate= 512.0kbits/s
		if (line.contains("time=")) {
			// 这里可以解析时间，计算进度百分比
			// 实际实现需要根据视频总时长计算
		}
	}

	/**
	 * 执行FFmpeg命令
	 *
	 * @param commands 命令参数（像给厨师递菜单）
	 * @return 是否成功（厨子有没有把菜做糊）
	 */
	public boolean execute( List<String> commands ) {
		List<String> fullCommand = new ArrayList<>();
		fullCommand.add(ffmpegConfig.getPath());
		fullCommand.addAll(commands);

		log.info("FFmpeg开始干活啦！命令：{}", String.join(" ", fullCommand));

		ProcessBuilder processBuilder = new ProcessBuilder(fullCommand);
		processBuilder.redirectErrorStream(true); // 错误输出也给我看看

		try {
			Process process = processBuilder.start();

			// 读取输出，防止FFmpeg“自言自语”没人听
			try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()))) {
				String line;
				while (( line = reader.readLine() ) != null) {
					log.debug("FFmpeg悄悄说：{}", line);
				}
			}

			// 等待处理完成，别急着催
			int exitCode = process.waitFor();
			boolean success = exitCode == 0;

			if (success) {
				log.info("FFmpeg完美收工！");
			} else {
				log.error("FFmpeg罢工了！退出码：{}", exitCode);
			}

			return success;

		} catch (Exception e) {
			log.error("FFmpeg崩溃了，原因：{}", e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 获取FFmpeg版本（验明正身）
	 */
	public String getVersion() {
		try {
			Process process = new ProcessBuilder(ffmpegConfig.getPath(), "-version").start();
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
			return reader.readLine(); // 第一行就是版本信息
		} catch (Exception e) {
			return "FFmpeg可能去度假了：" + e.getMessage();
		}
	}
}
