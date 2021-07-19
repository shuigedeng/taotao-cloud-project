package com.taotao.cloud.file.constant;

/**
 * 分布式文件存储常量
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:36
 */
public class UploadFileConstant {

	private UploadFileConstant() {
	}

	public static final String BASE_UPLOAD_FILE_PREFIX = "taotao.cloud.file";
	public static final String ENABLED = "enabled";
	public static final String TYPE = "type";
	public static final String TRUE = "true";
	public static final String JOINER = ".";

	/**
	 * 阿里云存储
	 */
	public static final String DFS_ALIYUN = "aliyun";
	/**
	 * 腾讯云存储
	 */
	public static final String DFS_QCLOUD = "qcloud";
	/**
	 * 七牛云存储
	 */
	public static final String DFS_QINIU = "qiniu";
	/**
	 * fastdfs存储
	 */
	public static final String DFS_FASTDFS = "fastdfs";
	/**
	 * nginx存储
	 */
	public static final String DFS_NGINX = "nginx";
	/**
	 * 本地存储
	 */
	public static final String DFS_LOCAL = "local";
	/**
	 * ftp存储
	 */
	public static final String DFS_FTP = "ftp";
	/**
	 * 又拍云存储
	 */
	public static final String DFS_UPYUN = "upyun";
}
