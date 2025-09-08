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

package com.taotao.cloud.sys.biz.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
/**
 * 文件服务信息
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 21:26
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

	UploadFileVO uploadFile(String type, MultipartFile file);

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
	List<String> testMybatisQueryStructure();

	boolean testSeata();

    void test(long fileId);
}
