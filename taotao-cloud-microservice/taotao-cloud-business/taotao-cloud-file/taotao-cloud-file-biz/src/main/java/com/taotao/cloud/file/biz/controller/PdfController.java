package com.taotao.cloud.file.biz.controller;

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.file.biz.util.FileUploaderUtils;
import com.taotao.cloud.file.biz.util.PdfUtils;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 【必须】程序运行所在环境安装 LibreOffice，PDF 转换基于 LibreOffice 完成(OpenOffice 也一样可用)
 * 安装参考教程：http://wiki.nooss.cn/archives/420.html Linux
 * 安装字体(不安装会出现乱码问题)：http://wiki.nooss.cn/archives/406.html
 * <p>
 * > 【必须】程序运行所在环境安装 LibreOffice，PDF 转换基于 LibreOffice 完成(OpenOffice 也一样可用) >
 * 安装参考教程：http://wiki.nooss.cn/archives/420.html > Linux
 * 安装字体(不安装会出现乱码问题)：http://wiki.nooss.cn/archives/406.html
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-pdf管理API", description = "工具管理端-pdf管理API")
@RequestMapping("/sys/tools/file/pdf")
public class PdfController {

	private final FileUploaderUtils fileUploaderUtils;
	private final PdfUtils pdfUtils;

	@Operation(summary = "文件上传", description = "文件上传")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/upload")
	public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file)
		throws Exception {
		if (file.isEmpty()) {
			throw new BusinessException("请上传文件");
		}

		String fileName = file.getOriginalFilename();
		String urlFileName = fileUploaderUtils.getRandomFileName(
			FileUploaderUtils.getSuffix(fileName));
		String url = fileUploaderUtils.upload(file.getBytes(), urlFileName);
		Map<String, Object> data = new HashMap<>(1);
		data.put("src", url);
		return Result.success(data);
	}

	@Operation(summary = "文件上传并转为PDF", description = "文件上传并转为PDF")
	@RequestLogger("文件上传并转为PDF")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/topdf")
	public Result<Map<String, Object>> toPdf(@RequestParam("file") MultipartFile file)
		throws Exception {
		if (file.isEmpty()) {
			throw new BusinessException("请上传文件");
		}
		String fileName = file.getOriginalFilename();
		String urlFileName = fileUploaderUtils.getRandomFileName(
			FileUploaderUtils.getSuffix(fileName));
		String originalUrl = fileUploaderUtils.upload(file.getBytes(), urlFileName);
		pdfUtils.toPdf(pdfUtils.getServerPath(originalUrl), pdfUtils.getTargetFolder(originalUrl));
		Map<String, Object> data = new HashMap<>();
		data.put("src", originalUrl);
		data.put("pdfPath", pdfUtils.getPDFUrl(originalUrl));
		return Result.success(data);
	}

	@Operation(summary = "文件上传并转为图片PNG格式", description = "文件上传并转为图片PNG格式")
	@RequestLogger("文件上传并转为图片PNG格式")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/topng")
	public Result<Map<String, Object>> toPng(@RequestParam("file") MultipartFile file)
		throws Exception {
		if (file.isEmpty()) {
			throw new BusinessException("请上传文件");
		}
		String fileName = file.getOriginalFilename();
		String urlFileName = fileUploaderUtils.getRandomFileName(
			FileUploaderUtils.getSuffix(fileName));
		String originalUrl = fileUploaderUtils.upload(file.getBytes(), urlFileName);
		pdfUtils.toPdf(pdfUtils.getServerPath(originalUrl), pdfUtils.getTargetFolder(originalUrl));

		int page = pdfUtils.pdf2Image(pdfUtils.getServerPath(pdfUtils.getPDFUrl(originalUrl)),
			pdfUtils.getTargetFolder(originalUrl), 96);
		Map<String, Object> data = new HashMap<>();
		data.put("src", originalUrl);
		data.put("pdfPath", pdfUtils.getPDFUrl(originalUrl));
		data.put("pngNum", page);
		data.put("pngList", pdfUtils.getPngUrl(originalUrl, page));
		return Result.success(data);
	}

}
