package com.taotao.cloud.sys.biz.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文件上传工具
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:17:07
 */
@Component
public class FileUploaderUtils {

	@Value("${castle.upload.fileServerPath:upload/file/}")
	private String originalFilePath;
	@Value("${castle.upload.fileDomain:http://up.hcses.cn/pdftools}")
	private String fileDomain;

	/**
	 * 获取文件后缀，无后缀时返回空
	 *
	 * @param fileName 文件名称
	 * @return 文件后缀
	 */
	public static String getSuffix(String fileName) {
		int suffixIndex = fileName.lastIndexOf(".");
		if (suffixIndex == -1) {
			return "";
		} else {
			return fileName.substring(suffixIndex + 1);
		}
	}

	/**
	 * 生成随机文件名
	 *
	 * @param suffix 后缀
	 * @return 文件名
	 * @since 2022-03-25 14:18:00
	 */
	public String getRandomFileName(String suffix) {
		//生成uuid
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//文件路径
		return DateUtil.format(new Date(), "yyyyMMdd") + File.separator + uuid + (
			StrUtil.isEmpty(suffix) ? "" : ("." + suffix));
	}

	/**
	 * 上传文件
	 *
	 * @param data     数据
	 * @param fileName 文件名称
	 * @return 上传路径
	 * @since 2022-03-25 14:18:25
	 */
	public String upload(byte[] data, String fileName) {
		return upload(new ByteArrayInputStream(data), fileName);
	}

	/**
	 * 上传文件
	 *
	 * @param inputStream 数据流
	 * @param fileName    文件名称
	 * @return 上传路径
	 * @since 2022-03-25 14:18:25
	 */
	public String upload(InputStream inputStream, String fileName) {
		File file = new File(originalFilePath + File.separator + fileName);
		try {
			FileUtils.copyToFile(inputStream, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDomain + "/" + fileName;
	}
}
