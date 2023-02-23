package com.taotao.cloud.im.biz.platform.modules.common.controller;

import com.platform.common.exception.BaseException;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.common.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

	@Resource
	private FileService fileService;

	/**
	 * 通用上传请求
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/upload")
	public AjaxResult upload(MultipartFile file) {
		if (file == null) {
			throw new BaseException("上传文件不能为空");
		}
		return AjaxResult.success(fileService.uploadFile(file));
	}

	/**
	 * 生成视频封面图
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/uploadVideo")
	public AjaxResult createVideoCover(MultipartFile file) {
		if (file == null) {
			throw new BaseException("上传文件不能为空");
		}
		// 调用视频处理工具类
		return AjaxResult.success(fileService.uploadVideo(file));
	}

	/**
	 * 生成音频文字
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/uploadAudio")
	public AjaxResult uploadAudio(MultipartFile file) {
		if (file == null) {
			throw new BaseException("上传文件不能为空");
		}
		// 调用视频处理工具类
		return AjaxResult.success(fileService.uploadAudio(file));
	}

}
