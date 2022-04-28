package com.taotao.cloud.ip2region.model;

/**
 * database configuration class
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:27:49
 */
public class DbConfig {

	/**
	 * 总头大小
	 * total header data block size
	 */
	private int totalHeaderSize;

	/**
	 * 索引块大小
	 * max index data block size u should always choice the fastest read block size
	 */
	private int indexBlockSize;

	/**
	 * construct method
	 *
	 * @param totalHeaderSize totalHeaderSize
	 * @return
	 * @since 2022-04-27 17:27:49
	 */
	public DbConfig(int totalHeaderSize) throws DbMakerConfigException {
		if ((totalHeaderSize % 8) != 0) {
			throw new DbMakerConfigException("totalHeaderSize must be times of 8");
		}
		this.totalHeaderSize = totalHeaderSize;
		// 4 * 1024
		this.indexBlockSize = 4096;
	}

	/**
	 * 数据库配置
	 *
	 * @return
	 * @since 2022-04-27 17:27:49
	 */
	public DbConfig() throws DbMakerConfigException {
		this(8192);
	}

	/**
	 * 得到总头大小
	 *
	 * @return int
	 * @since 2022-04-27 17:27:49
	 */
	public int getTotalHeaderSize() {
		return totalHeaderSize;
	}

	/**
	 * 设置总头大小
	 *
	 * @param totalHeaderSize 总头大小
	 * @return {@link DbConfig }
	 * @since 2022-04-27 17:27:49
	 */
	public DbConfig setTotalHeaderSize(int totalHeaderSize) {
		this.totalHeaderSize = totalHeaderSize;
		return this;
	}

	/**
	 * 得到索引块大小
	 *
	 * @return int
	 * @since 2022-04-27 17:27:49
	 */
	public int getIndexBlockSize() {
		return indexBlockSize;
	}

	/**
	 * 设置索引块大小
	 *
	 * @param dataBlockSize 数据块大小
	 * @return {@link DbConfig }
	 * @since 2022-04-27 17:27:50
	 */
	public DbConfig setIndexBlockSize(int dataBlockSize) {
		this.indexBlockSize = dataBlockSize;
		return this;
	}
}
