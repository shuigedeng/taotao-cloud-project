package com.taotao.cloud.file.base;

import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件上传接口
 *
 * @author dengtao
 * @date 2020/10/26 10:42
 * @since v1.0
 */
public interface FileUpload {

	/**
	 * 文件上传接口
	 *
	 * @param file 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/11/12 16:01
	 * @since v1.0
	 */
	FileInfo upload(File file) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file
	 * @param fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/11/12 17:03
	 * @since v1.0
	 */
	FileInfo upload(File file, String fileKey) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/11/12 17:47
	 * @since v1.0
	 */
	FileInfo upload(MultipartFile file) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file
	 * @param fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/11/12 17:47
	 * @since v1.0
	 */
	FileInfo upload(MultipartFile file, String fileKey) throws FileUploadException;

	/**
	 * 删除文件
	 *
	 * @param fileInfo
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @date 2020/11/12 17:47
	 * @since v1.0
	 */
	FileInfo delete(FileInfo fileInfo) throws FileUploadException;
}
