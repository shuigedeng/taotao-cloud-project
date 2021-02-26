package com.taotao.cloud.file.base;

import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/10/26 10:42
 */
public interface FileUpload {

	/**
	 * 文件上传接口
	 *
	 * @param file 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @since 2020/11/12 16:01
	 */
	FileInfo upload(File file) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file    file
	 * @param fileKey fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @since 2020/11/12 17:03
	 */
	FileInfo upload(File file, String fileKey) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file file
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @since 2020/11/12 17:47
	 */
	FileInfo upload(MultipartFile file) throws FileUploadException;

	/**
	 * 文件上传接口
	 *
	 * @param file    file
	 * @param fileKey fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @since 2020/11/12 17:47
	 */
	FileInfo upload(MultipartFile file, String fileKey) throws FileUploadException;

	/**
	 * 删除文件
	 *
	 * @param fileInfo fileInfo
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author dengtao
	 * @since 2020/11/12 17:47
	 */
	FileInfo delete(FileInfo fileInfo) throws FileUploadException;
}
