package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.biz.utils.FileUploaderUtils;
import com.taotao.cloud.sys.biz.utils.PdfUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 【必须】程序运行所在环境安装 LibreOffice，PDF 转换基于 LibreOffice 完成(OpenOffice 也一样可用)
 * 安装参考教程：http://wiki.nooss.cn/archives/420.html Linux 安装字体(不安装会出现乱码问题)：http://wiki.nooss.cn/archives/406.html
 * <p>
 * > 【必须】程序运行所在环境安装 LibreOffice，PDF 转换基于 LibreOffice 完成(OpenOffice 也一样可用) >
 * 安装参考教程：http://wiki.nooss.cn/archives/420.html > Linux 安装字体(不安装会出现乱码问题)：http://wiki.nooss.cn/archives/406.html
 */
@RestController
@RequestMapping("/sys/tools/docpreview")
public class PdfToolsController {

	@Autowired
	private FileUploaderUtils fileUploaderUtils;
	@Autowired
	private PdfUtils pdfUtils;

	/**
	 * 文件上传
	 */
	@PostMapping("upload")
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


	/**
	 * 文件上传并转为PDF
	 */
	@PostMapping("topdf")
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

	/**
	 * 文件上传并转为图片PNG格式
	 */
	@PostMapping("topng")
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
