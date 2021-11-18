/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.oss.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.exception.UploadFileTypeException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

/**
 * 文件工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 11:10
 */
public class FileUtil {

	public static final String UPLOAD_FILE_PARSE_ERROR_MESSAGE = "解析文件失败";
	public static final String UPLOAD_FILE_SAVE_ERROR_MESSAGE = "保存文件失败";
	public static final String UPLOAD_FILE_FORMAT_ERROR_MESSAGE = "文件格式错误";
	public static final String UPLOAD_FILE_DATA_FORMAT = "yyyy/MM/dd/HH/mm";
	public static final String UPLOAD_FILE_TOO_BIG = "文件过大";

	private FileUtil() {
	}

	/**
	 * 获取文件信息
	 *
	 * @param multipartFile 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/10/26 10:43
	 */
	public static UploadFileInfo getMultipartFileInfo(MultipartFile multipartFile) {
		try {
			UploadFileInfo uploadFileInfo = new UploadFileInfo();

			String md5 = fileMd5(multipartFile.getInputStream());
			uploadFileInfo.setFileMd5(md5);

			String originalFilename = multipartFile.getOriginalFilename();
			uploadFileInfo.setOriginalFileName(originalFilename);

			assert originalFilename != null;
			File file = new File(originalFilename);
			cn.hutool.core.io.FileUtil.writeFromStream(multipartFile.getInputStream(), file);

			String extName = cn.hutool.core.io.FileUtil.extName(file);
			uploadFileInfo.setName(extractFilename(originalFilename, extName));
			uploadFileInfo.setContentType(multipartFile.getContentType());
			uploadFileInfo.setImg(isImage(file));
			uploadFileInfo.setSize(multipartFile.getSize());
			uploadFileInfo.setFileType(FileTypeUtil.getType(multipartFile.getInputStream()));
			return uploadFileInfo;
		} catch (IOException e) {
			throw new UploadFileException(UPLOAD_FILE_PARSE_ERROR_MESSAGE);
		}
	}

	/**
	 * 获取文件信息
	 *
	 * @param file 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/10/26 10:43
	 */
	public static UploadFileInfo getFileInfo(File file) {
		try {
			UploadFileInfo uploadFileInfo = new UploadFileInfo();
			String md5 = fileMd5(new FileInputStream(file));
			uploadFileInfo.setOriginalFileName(file.getName());
			uploadFileInfo.setName(file.getName());
			uploadFileInfo.setFileMd5(md5);
			uploadFileInfo.setContentType(new MimetypesFileTypeMap().getContentType(file));
			uploadFileInfo.setImg(isImage(file));
			uploadFileInfo.setSize(cn.hutool.core.io.FileUtil.size(file));
			uploadFileInfo.setFileType(cn.hutool.core.io.FileUtil.getType(file));
			return uploadFileInfo;
		} catch (Exception e) {
			throw new UploadFileException(UPLOAD_FILE_PARSE_ERROR_MESSAGE);
		}
	}

	/**
	 * 文件的md5
	 *
	 * @param inputStream inputStream
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:43
	 */
	public static String fileMd5(InputStream inputStream) {
		return DigestUtil.md5Hex(inputStream);
	}

	/**
	 * 保存文件到本地
	 *
	 * @param file 文件对象
	 * @param path 保存路径
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:43
	 */
	public static String saveFile(MultipartFile file, String path) {
		try {
			File targetFile = new File(path);
			if (targetFile.exists()) {
				return path;
			}
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			file.transferTo(targetFile);
			return path;
		} catch (Exception e) {
			LogUtil.error(UPLOAD_FILE_SAVE_ERROR_MESSAGE, e);
			return null;
		}
	}

	/**
	 * 删除本地文件
	 *
	 * @param path 文件路径
	 * @return boolean
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			boolean flag = file.delete();
			if (flag) {
				File[] files = file.getParentFile().listFiles();
				if (files == null || files.length == 0) {
					file.getParentFile().delete();
				}
			}
			return flag;
		}
		return false;
	}

	/**
	 * 判断文件格式
	 *
	 * @param file        文件对象
	 * @param acceptTypes 接受类型
	 * @return com.taotao.cloud.file.pojo.ResultBody
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	public static Boolean validType(MultipartFile file, String[] acceptTypes) {
		if (ArrayUtil.isEmpty(acceptTypes)) {
			return Boolean.TRUE;
		}

		try {
			String type = FileTypeUtil.getType(file.getInputStream());
			if (StrUtil.isBlank(type)) {
				type = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
			}
			if (ArrayUtil.contains(acceptTypes, type)) {
				return Boolean.TRUE;
			}
			throw new UploadFileTypeException(UPLOAD_FILE_FORMAT_ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UploadFileTypeException(UPLOAD_FILE_FORMAT_ERROR_MESSAGE);
		}
	}

	/**
	 * 获取文件名
	 *
	 * @param file 文件对象
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	public static String extractFilename(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String extension = getExtension(file);
		return extractFilename(fileName, extension);
	}

	/**
	 * 编码文件名
	 *
	 * @param fileName  文件名称
	 * @param extension 扩展名称
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	public static String extractFilename(String fileName, String extension) {
		return DateUtil.format(new Date(), UPLOAD_FILE_DATA_FORMAT) + "/" + encodingFilename(fileName)
			+ "." + extension;
	}

	/**
	 * 编码文件名
	 *
	 * @param fileName fileName
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	private static String encodingFilename(String fileName) {
		fileName = fileName.replace("_", " ");
		fileName = SecureUtil.md5().digestHex(fileName + System.nanoTime() + "tt");
		return fileName;
	}

	/**
	 * 获取文件名的后缀
	 *
	 * @param file file
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/10/26 10:44
	 */
	public static String getExtension(MultipartFile file) {
		String extension = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		if (StringUtils.isEmpty(extension)) {
			extension = MimeTypeUtil.getExtension(Objects.requireNonNull(file.getContentType()));
		}
		return extension;
	}

	/**
	 * 判断文件是否是图片
	 *
	 * @param file file
	 * @return boolean
	 * @author shuigedeng
	 * @since 2020/10/26 10:45
	 */
	public static boolean isImage(File file) {
		if (!file.exists()) {
			return false;
		}
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			return image != null && image.getWidth() > 0 && image.getHeight() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断文件是否是图片
	 *
	 * @param file file
	 * @return boolean
	 * @author shuigedeng
	 * @since 2020/10/26 10:45
	 */
	public static boolean isImage(MultipartFile file) {
		if (file == null) {
			return false;
		}
		BufferedImage image;
		try {
			image = ImageIO.read(file.getInputStream());
			return image != null && image.getWidth() > 0 && image.getHeight() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] getFileByteArray(File file) {
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			LogUtil.error(UPLOAD_FILE_TOO_BIG);
			return null;
		}
		byte[] buffer = null;
		try (FileInputStream fi = new FileInputStream(file)) {
			buffer = new byte[(int) fileSize];
			int offset = 0;
			int numRead = 0;
			while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
				offset += numRead;
			}
			// 确保所有数据均被读取
			if (offset != buffer.length) {
				throw new IOException("Could not completely read file "
					+ file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
