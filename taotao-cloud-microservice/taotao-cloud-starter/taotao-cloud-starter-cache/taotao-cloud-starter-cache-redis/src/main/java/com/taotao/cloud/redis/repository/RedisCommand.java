package com.taotao.cloud.redis.repository;

/**
 * redis常量
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-27 20:44:17
 */
public class RedisCommand {

	public static final String BITCOUNT = "BITCOUNT";

	/**
	 *  <a href="https://redis.io/commands/bitcount/#History">redis 版本 7.0以上</a>
	 */
	public enum BitMapModel {
		/**
		 *  BYTE
		 */
		BYTE,
		/**
		 * BIT
		 */
		BIT,
	}
}
