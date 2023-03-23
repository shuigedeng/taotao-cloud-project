package com.taotao.cloud.file.biz.largefile.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.file.biz.largefile.po.FileDownloadRequest;
import com.taotao.cloud.file.biz.largefile.po.FileUpload;
import com.taotao.cloud.file.biz.largefile.po.FileUploadRequest;
import com.taotao.cloud.file.biz.largefile.service.FileService;
import com.taotao.cloud.file.biz.largefile.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
@Slf4j
public class FileController {


	@Autowired
	private FileService fileService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;


	@GetMapping(value = "/")
	public String gotoPage() {
		return "index";
	}

	@GetMapping(value = "/uploadFile")
	public String gotoFilePage() {
		return "upload";
	}

	@GetMapping(value = "/oss/upload")
	public String gotoOssPage() {
		return "ossUpload";
	}


	@PostMapping(value = "/upload")
	@ResponseBody
	public Result<FileUpload> upload(FileUploadRequest fileUploadRequestDTO) throws IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileUpload fileUploadDTO = null;
		if (isMultipart) {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start("upload");
			if (fileUploadRequestDTO.getChunk() != null && fileUploadRequestDTO.getChunks() > 0) {
				fileUploadDTO = fileService.sliceUpload(fileUploadRequestDTO);
			} else {
				fileUploadDTO = fileService.upload(fileUploadRequestDTO);
			}
			stopWatch.stop();
			log.info("{}", stopWatch.prettyPrint());

			return Result.success(fileUploadDTO);
		}

		throw new RuntimeException("上传失败");
	}

	@RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
	@ResponseBody
	public Result<FileUpload> checkFileMd5(String md5, String path) throws IOException {
		FileUploadRequest param = new FileUploadRequest().setPath(path).setMd5(md5);
		FileUpload fileUploadDTO = fileService.checkFileMd5(param);

		return Result.success(fileUploadDTO);
	}

	@PostMapping("/download")
	public void download(FileDownloadRequest requestDTO) {
		try {
			FileUtil.downloadFile(requestDTO.getName(), requestDTO.getPath(), request, response);
		} catch (FileNotFoundException e) {
			log.error("download error:" + e.getMessage(), e);
			throw new RuntimeException("文件下载失败");
		}
	}


}
