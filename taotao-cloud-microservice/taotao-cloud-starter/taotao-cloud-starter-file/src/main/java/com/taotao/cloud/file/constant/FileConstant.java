package com.taotao.cloud.file.constant;

/**
 * 分布式文件存储常量
 *
 * @author dengtao
 * @date 2020/10/26 09:36
 * @since v1.0
 */
public interface FileConstant {
	/**
	 * 阿里云存储
	 */
	String DFS_ALIYUN = "aliyun";
	/**
	 * 腾讯云存储
	 */
	String DFS_QCLOUD = "qcloud";
	/**
	 * 七牛云存储
	 */
	String DFS_QINIU = "qiniu";
	/**
	 * fastdfs存储
	 */
	String DFS_FASTDFS = "fastdfs";
	/**
	 * nginx存储
	 */
	String DFS_NGINX = "nginx";
	/**
	 * 本地存储
	 */
	String DFS_LOCAL = "local";
	/**
	 * ftp存储
	 */
	String DFS_FTP = "ftp";
	/**
	 * 又拍云存储
	 */
	String DFS_UPYUN = "upyun";
}
