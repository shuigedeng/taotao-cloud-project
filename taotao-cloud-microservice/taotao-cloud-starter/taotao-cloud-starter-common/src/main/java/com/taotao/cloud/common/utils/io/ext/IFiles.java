package com.taotao.cloud.common.utils.io.ext;

/**
 * 文件接口
 */
public interface IFiles {

	/**
	 * 读取文件内容 兼容性处理：如果 endIndex 超过文档的长度，则以文档长度为准
	 *
	 * @param startIndex 开始下标
	 * @param endIndex   结束下标
	 * @return 字节内容
	 */
	byte[] read(final long startIndex, final long endIndex);

	/**
	 * 读取文件内容
	 *
	 * @param startIndex 开始下标
	 * @param endIndex   结束下标
	 * @param charset    文件编码
	 * @return 字字符串内容
	 */
	String read(final long startIndex, final long endIndex, final String charset);

	/**
	 * 写入文件内容 兼容性处理：如果 endIndex 超过文档的长度，则以文档长度为准
	 *
	 * @param startIndex 开始下标
	 * @param bytes      字节内容
	 */
	void write(final long startIndex, final byte[] bytes);

}
