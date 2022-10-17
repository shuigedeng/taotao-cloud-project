package com.taotao.cloud.office.convert.config;

/**
 * <p>
 * 全局常用变量
 * </p>
 *
 * @description
 * @since 2019/10/12 14:47
 */
public class Constants {

	// ===============================================================================
	// ============================ ↓↓↓↓↓↓ 文件系列 ↓↓↓↓↓↓ ============================
	// ===============================================================================

	/**
	 * 系统分隔符
	 */
	public static String SYSTEM_SEPARATOR = "/";

	/**
	 * 获取项目根目录
	 */
	public static String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", SYSTEM_SEPARATOR);

	/**
	 * 临时文件相关
	 */
	public final static String DEFAULT_FOLDER_TMP = PROJECT_ROOT_DIRECTORY + "/tmp";
	public final static String DEFAULT_FOLDER_TMP_GENERATE = PROJECT_ROOT_DIRECTORY + "/tmp-generate";

}
