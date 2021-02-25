package com.taotao.cloud.file.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.taotao.cloud.file.exception.FileTypeException;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author dengtao
 * @date 2020/10/26 11:10
 * @since v1.0
 */
public class FileUtil {

	private final static Logger log = LoggerFactory.getLogger(File.class);

	private FileUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 获取文件信息
	 *
	 * @param multipartFile 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/10/26 10:43
	 * @since v1.0
	 */
	public static FileInfo getMultipartFileInfo(MultipartFile multipartFile) {
		try {
			FileInfo fileInfo = new FileInfo();

			String md5 = fileMd5(multipartFile.getInputStream());
			fileInfo.setFileMd5(md5);

			String originalFilename = multipartFile.getOriginalFilename();
			fileInfo.setOriginalFileName(originalFilename);

			assert originalFilename != null;
			File file = new File(originalFilename);
			cn.hutool.core.io.FileUtil.writeFromStream(multipartFile.getInputStream(), file);

			String extName = cn.hutool.core.io.FileUtil.extName(file);
			fileInfo.setName(extractFilename(originalFilename, extName));
			fileInfo.setContentType(multipartFile.getContentType());
			fileInfo.setIsImg(isImage(file));
			fileInfo.setSize(multipartFile.getSize());
			fileInfo.setFileType(FileTypeUtil.getType(multipartFile.getInputStream()));
			return fileInfo;
		} catch (IOException e) {
			throw new FileUploadException("文件解析失败");
		}
	}

	/**
	 * 获取文件信息
	 *
	 * @param file 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/10/26 10:43
	 * @since v1.0
	 */
	public static FileInfo getFileInfo(File file) {
		try {
			FileInfo fileInfo = new FileInfo();
			String md5 = fileMd5(new FileInputStream(file));
			fileInfo.setOriginalFileName(file.getName());
			fileInfo.setName(file.getName());
			fileInfo.setFileMd5(md5);
			fileInfo.setContentType(new MimetypesFileTypeMap().getContentType(file));
			fileInfo.setIsImg(isImage(file));
			fileInfo.setSize(cn.hutool.core.io.FileUtil.size(file));
			fileInfo.setFileType(cn.hutool.core.io.FileUtil.getType(file));
			return fileInfo;
		} catch (Exception e) {
			throw new FileUploadException("文件解析失败");
		}
	}

	/**
	 * 文件的md5
	 *
	 * @param inputStream inputStream
	 * @return java.lang.String
	 * @author dengtao
	 * @date 2020/10/26 10:43
	 * @since v1.0
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
	 * @author dengtao
	 * @date 2020/10/26 10:43
	 * @since v1.0
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
			log.error("saveFile-error", e);
			return null;
		}
	}

	/**
	 * 删除本地文件
	 *
	 * @param path 文件路径
	 * @return boolean
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
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
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
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
			throw new FileTypeException("文件格式错误");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileTypeException("文件格式错误");
		}
	}

	/**
	 * 获取文件名
	 *
	 * @param file 文件对象
	 * @return java.lang.String
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
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
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
	 */
	public static String extractFilename(String fileName, String extension) {
		return DateUtil.format(new Date(), "yyyy/MM/dd/HH/mm") + "/" + encodingFilename(fileName) + "." + extension;
	}

	/**
	 * 编码文件名
	 *
	 * @param fileName
	 * @return java.lang.String
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
	 */
	private static String encodingFilename(String fileName) {
		fileName = fileName.replace("_", " ");
		fileName = SecureUtil.md5().digestHex(fileName + System.nanoTime() + "tt");
		return fileName;
	}

	/**
	 * 获取文件名的后缀
	 *
	 * @param file
	 * @return java.lang.String
	 * @author dengtao
	 * @date 2020/10/26 10:44
	 * @since v1.0
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
	 * @param file
	 * @return boolean
	 * @author dengtao
	 * @date 2020/10/26 10:45
	 * @since v1.0
	 */
	public static boolean isImage(File file) {
		if (!file.exists()) {
			return false;
		}
		BufferedImage image = null;
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
	 * @param file
	 * @return boolean
	 * @author dengtao
	 * @date 2020/10/26 10:45
	 * @since v1.0
	 */
	public static boolean isImage(MultipartFile file) {
		if (file == null) {
			return false;
		}
		BufferedImage image = null;
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
			System.out.println("file too big...");
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
