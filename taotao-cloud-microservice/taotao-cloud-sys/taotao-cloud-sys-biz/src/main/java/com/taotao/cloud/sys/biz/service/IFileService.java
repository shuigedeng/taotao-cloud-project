package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.biz.entity.file.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileService
 *
 * @author shuigedeng
 * @since 2020/11/12 21:26
 * @version 2022.03
 */
public interface IFileService {
	/**
	 * 上传文件
	 *
	 * @param uploadFile 文件内容
	 * @return 文件信息
	 * @since 2020/11/20 上午11:00
	 */
	File upload(MultipartFile uploadFile);

	/**
	 * 根据id查询文件信息
	 *
	 * @param id id
	 * @return 文件信息
	 * @since 2020/11/20 上午11:14
	 */
	File findFileById(Long id);

	// /**
	//  * 删除文件
	//  *
	//  * @param objectName
	//  * @author shuigedeng
	//  * @since 2020/9/9 11:17
	//  */
	// Boolean delete(String objectName);
	//
	// /**
	//  * 查询oss上的所有文件
	//  *
	//  * @param
	//  * @author shuigedeng
	//  * @since 2020/9/9 11:20
	//  */
	// List<OSSObjectSummary> list();
	//
	// /**
	//  * 根据文件名下载oss上的文件
	//  *
	//  * @param outputStream
	//  * @param objectName
	//  * @author shuigedeng
	//  * @since 2020/9/9 11:23
	//  */
	// void exportOssFile(ServletOutputStream outputStream, String objectName);
}
