package com.taotao.cloud.ip2region.model;

import java.io.IOException;

/**
 * dbreader
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:28:05
 */
public interface DBReader {

	/**
	 * 完整
	 *
	 * @return {@link byte[] }
	 * @since 2022-04-27 17:28:05
	 */
	byte[] full() throws IOException;

	/**
	 * 读完全
	 *
	 * @param pos    pos
	 * @param buf    缓冲区
	 * @param offset 抵消
	 * @param length 长度
	 * @since 2022-04-27 17:28:06
	 */
	void readFully(long pos, byte[] buf, int offset, int length) throws IOException;

	/**
	 * 关闭
	 *
	 * @since 2022-04-27 17:28:06
	 */
	void close() throws IOException;
}
