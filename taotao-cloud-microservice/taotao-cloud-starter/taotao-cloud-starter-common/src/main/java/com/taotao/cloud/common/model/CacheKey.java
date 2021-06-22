package com.taotao.cloud.common.model;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 缓存 key 封装
 *
 * @author zuihou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKey {

	/**
	 * redis key
	 */
	@NonNull
	private String key;
	/**
	 * 超时时间 秒
	 */
	private Duration expire;

	public CacheKey(final @NonNull String key) {
		this.key = key;
	}


}
