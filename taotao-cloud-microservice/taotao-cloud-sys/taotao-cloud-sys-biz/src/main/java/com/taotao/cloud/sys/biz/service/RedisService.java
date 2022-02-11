package com.taotao.cloud.sys.biz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * RedisService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:19:39
 */
public interface RedisService {

	/**
	 * findById
	 *
	 * @param key
	 * @return
	 */
	Page findByKey(String key, Pageable pageable);

	/**
	 * 查询验证码的值
	 *
	 * @param key
	 * @return
	 */
	String getCodeVal(String key);

	/**
	 * 保存验证码
	 *
	 * @param key
	 * @param val
	 */
	void saveCode(String key, Object val);

	/**
	 * delete
	 *
	 * @param key
	 */
	void delete(String key);

	/**
	 * 清空所有缓存
	 */
	void flushdb();
}
