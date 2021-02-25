package com.taotao.cloud.file.pojo;

import lombok.Data;

/**
 * file实体类
 *
 * @author dengtao
 * @date 2020/11/12 15:54
 * @since v1.0
 */
@Data
public class FileInfo {

	/**
	 * 原始文件名
	 */
	private String originalFileName;

	/**
	 * 编码之后文件名称
	 */
	private String name;

	/**
	 * 文件md5
	 */
	private String fileMd5;

	/**
	 * 是否图片
	 */
	private Boolean isImg;

	/**
	 * contentType
	 */
	private String contentType;

	/**
	 * 上传文件类型
	 */
	private String fileType;

	/**
	 * 文件大小
	 */
	private long size;

	/**
	 * 访问路径
	 */
	private String url;
}
