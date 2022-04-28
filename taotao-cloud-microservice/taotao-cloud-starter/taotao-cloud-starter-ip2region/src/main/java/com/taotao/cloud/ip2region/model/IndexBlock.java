package com.taotao.cloud.ip2region.model;


import com.taotao.cloud.ip2region.utils.Ip2regionUtil;

/**
 * item index class
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:28:17
 */
public class IndexBlock {

	/**
	 * 长度
	 */
	private static final int LENGTH = 12;

	/**
	 * 启动ip
	 * start ip address
	 */
	private long startIp;

	/**
	 * ip结束
	 * end ip address
	 */
	private long endIp;

	/**
	 * 数据ptr
	 * data ptr and data length
	 */
	private int dataPtr;

	/**
	 * 数据len
	 * data length
	 */
	private int dataLen;

	/**
	 * 索引块
	 *
	 * @param startIp 启动ip
	 * @param endIp   ip结束
	 * @param dataPtr 数据ptr
	 * @param dataLen 数据len
	 * @return
	 * @since 2022-04-27 17:28:17
	 */
	public IndexBlock(long startIp, long endIp, int dataPtr, int dataLen) {
		this.startIp = startIp;
		this.endIp = endIp;
		this.dataPtr = dataPtr;
		this.dataLen = dataLen;
	}

	/**
	 * 开始ip
	 *
	 * @return long
	 * @since 2022-04-27 17:28:17
	 */
	public long getStartIp() {
		return startIp;
	}

	/**
	 * 设置启动ip
	 *
	 * @param startIp 启动ip
	 * @return {@link IndexBlock }
	 * @since 2022-04-27 17:28:17
	 */
	public IndexBlock setStartIp(long startIp) {
		this.startIp = startIp;
		return this;
	}

	/**
	 * 得到最终ip
	 *
	 * @return long
	 * @since 2022-04-27 17:28:17
	 */
	public long getEndIp() {
		return endIp;
	}

	/**
	 * 设置终端ip
	 *
	 * @param endIp ip结束
	 * @return {@link IndexBlock }
	 * @since 2022-04-27 17:28:17
	 */
	public IndexBlock setEndIp(long endIp) {
		this.endIp = endIp;
		return this;
	}

	/**
	 * 获取数据ptr
	 *
	 * @return int
	 * @since 2022-04-27 17:28:17
	 */
	public int getDataPtr() {
		return dataPtr;
	}

	/**
	 * 集数据ptr
	 *
	 * @param dataPtr 数据ptr
	 * @return {@link IndexBlock }
	 * @since 2022-04-27 17:28:17
	 */
	public IndexBlock setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
		return this;
	}

	/**
	 * len获取数据
	 *
	 * @return int
	 * @since 2022-04-27 17:28:17
	 */
	public int getDataLen() {
		return dataLen;
	}

	/**
	 * len组数据
	 *
	 * @param dataLen 数据len
	 * @return {@link IndexBlock }
	 * @since 2022-04-27 17:28:18
	 */
	public IndexBlock setDataLen(int dataLen) {
		this.dataLen = dataLen;
		return this;
	}

	/**
	 * 得到索引块长度
	 *
	 * @return int
	 * @since 2022-04-27 17:28:18
	 */
	public static int getIndexBlockLength() {
		return LENGTH;
	}

	/**
	 * get the bytes for storage
	 *
	 * @return {@link byte[] }
	 * @since 2022-04-27 17:28:18
	 */
	public byte[] getBytes() {
		/*
		 * +------------+-----------+-----------+
		 * | 4bytes        | 4bytes    | 4bytes    |
		 * +------------+-----------+-----------+
		 *  start ip      end ip      data ptr + len
		 */
		byte[] b = new byte[12];

		// start ip
		Ip2regionUtil.writeIntLong(b, 0, startIp);
		// end ip
		Ip2regionUtil.writeIntLong(b, 4, endIp);

		// write the data ptr and the length
		long mix = dataPtr | ((dataLen << 24) & 0xFF000000L);
		Ip2regionUtil.writeIntLong(b, 8, mix);

		return b;
	}
}
