/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.oss.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.common.exception.UploadFileException;
import com.taotao.cloud.oss.common.exception.UploadFileTypeException;
import com.taotao.cloud.oss.common.model.UploadFileInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @author shuigedeng
 * @version 2022.03
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
	 * @since 2020/10/26 10:44
	 */
	public static String extractFilename(String fileName, String extension) {
		return DateUtil.format(new Date(), UPLOAD_FILE_DATA_FORMAT) + "/" + encodingFilename(
			fileName)
			+ "." + extension;
	}

	/**
	 * 编码文件名
	 *
	 * @param fileName fileName
	 * @return java.lang.String
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
	 * @since 2020/10/26 10:44
	 */
	public static String getExtension(MultipartFile file) {
		String extension = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		if (StringUtil.isEmpty(extension)) {
			extension = MimeTypeUtil.getExtension(Objects.requireNonNull(file.getContentType()));
		}
		return extension;
	}

	/**
	 * 判断文件是否是图片
	 *
	 * @param file file
	 * @return boolean
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
			return false;
		}
	}

	/**
	 * 判断文件是否是图片
	 *
	 * @param file file
	 * @return boolean
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
			int numRead;
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

	/**
	 * File转MultipartFile
	 *
	 * @param file 文件
	 * @return MultipartFile
	 */
	public static MultipartFile fileToMultipartFile(File file) {
		FileItemFactory factory = new DiskFileItemFactory(16, null);
		FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
		int bytesRead;
		byte[] buffer = new byte[8192];
		try (FileInputStream fis = new FileInputStream(file);
			OutputStream os = item.getOutputStream()) {
			while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new CommonsMultipartFile(item);
	}

	public static MultipartFile fileToMultipartFileByMock(File file) {
		MultipartFile multipartFile;
		try {
			FileInputStream input = new FileInputStream(file);
			multipartFile = new MockMultipartFile("file",
				file.getName(), "text/plain", IOUtils.toByteArray(input));
		} catch (IOException e) {
			LogUtil.error(e);
			throw new RuntimeException(e);
		}
		return multipartFile;
	}

	public static File multipartFileToFile(String path, MultipartFile multipartFile) {
		File file = new File(path);
		try {
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
		} catch (IOException e) {
			LogUtil.error(e);
			throw new RuntimeException(e);
		}
		return file;
	}

	/**
	 * 判断文件夹是否存在
	 *
	 * @param filePath 文件地址
	 */
	public static boolean fileIsExists(String filePath) {
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 压缩文件夹
	 *
	 * @param srcDir           压缩文件夹路径
	 * @param outDir           压缩文件路径
	 * @param keepDirStructure 是否保留原来的目录结构, true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(String outDir, boolean keepDirStructure, String... srcDir) {
		try {
			OutputStream out = new FileOutputStream(new File(outDir));
			ZipOutputStream zos = null;
			try {
				zos = new ZipOutputStream(out);
				List<File> sourceFileList = new ArrayList<File>();
				for (String dir : srcDir) {
					File sourceFile = new File(dir);
					sourceFileList.add(sourceFile);
				}
				compress(sourceFileList, zos, keepDirStructure);
			} catch (Exception e) {
				throw new RuntimeException("zip error from ZipUtils", e);
			} finally {
				if (zos != null) {
					try {
						zos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("压缩失败:{}", e.getMessage());
		}
	}

	private static final int BUFFER_SIZE = 2 * 1024;

	private static void compress(List<File> sourceFileList, ZipOutputStream zos,
		boolean keepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		for (File sourceFile : sourceFileList) {
			String name = sourceFile.getName();
			if (sourceFile.isFile()) {
				zos.putNextEntry(new ZipEntry(name));
				int len;
				FileInputStream in = new FileInputStream(sourceFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			} else {
				File[] listFiles = sourceFile.listFiles();
				if (listFiles == null || listFiles.length == 0) {
					if (keepDirStructure) {
						zos.putNextEntry(new ZipEntry(name + "/"));
						zos.closeEntry();
					}
				} else {
					for (File file : listFiles) {
						if (keepDirStructure) {
							compress(file, zos, name + "/" + file.getName(),
								keepDirStructure);
						} else {
							compress(file, zos, file.getName(),
								keepDirStructure);
						}
					}
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 *
	 * @param sourceFile       源文件
	 * @param zos              zip输出流
	 * @param name             压缩后的名称
	 * @param keepDirStructure 是否保留原来的目录结构, true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name,
		boolean keepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				if (keepDirStructure) {
					zos.putNextEntry(new ZipEntry(name + "/"));
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					if (keepDirStructure) {
						compress(file, zos, name + "/" + file.getName(),
							keepDirStructure);
					} else {
						compress(file, zos, file.getName(), keepDirStructure);
					}
				}
			}
		}
	}


	/**
	 * 写入文件
	 *
	 * @param inputStream
	 * @param path
	 * @param fileName
	 */
	public static void writeFile(InputStream inputStream, String path, String fileName) {
		OutputStream os = null;
		try {
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流保存到本地文件
			File tempFile = new File(path);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			String newFileName = tempFile.getPath() + File.separator + fileName;
			LogUtil.info("保存文件：" + newFileName);
			os = new FileOutputStream(newFileName);
			// 开始读取
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			LogUtil.error("生成excel失败");
		} finally {
			// 完毕，关闭所有链接
			try {
				if (os != null) {
					os.close();
				}
				inputStream.close();
			} catch (IOException e) {
				LogUtil.error("关闭链接失败" + e.getMessage());
			}
		}
	}

	/**
	 * 保存文件
	 *
	 * @param inputStream
	 * @param path
	 * @param fileName
	 */
	public static void write(InputStream inputStream, String path, String fileName) {
		OutputStream os = null;
		long dateStr = System.currentTimeMillis();
		try {
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流保存到本地文件
			File tempFile = new File(path);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			String newFileName = tempFile.getPath() + File.separator + fileName;
			LogUtil.info("保存文件：" + newFileName);
			os = new FileOutputStream(newFileName);
			// 开始读取
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			LogUtil.error("生成excel失败");
		} finally {
			// 完毕，关闭所有链接
			try {
				if (os != null) {
					os.close();
				}
				inputStream.close();
			} catch (IOException e) {
				LogUtil.error("关闭链接失败" + e.getMessage());
			}
		}
	}


}
