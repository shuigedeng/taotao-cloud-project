package com.taotao.cloud.encrypt.handler;


import com.taotao.cloud.encrypt.exception.EncryptException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 签名加密处理类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:10:18
 */
public interface SignEncryptHandler {

	/**
	 * 签名处理
	 *
	 * @param proceed    处理对象
	 * @param timeout    超时时间
	 * @param timeUnit   时间戳
	 * @param signSecret 加密密钥
	 * @param jsonMap    加密报文
	 * @return {@link Object }
	 * @since 2022-07-06 15:10:18
	 */
	public Object handle(Object proceed, long timeout, TimeUnit timeUnit, String signSecret,
	                     Map<Object, Object> jsonMap) throws EncryptException;
}
