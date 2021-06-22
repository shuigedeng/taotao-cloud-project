package com.taotao.cloud.common.model;

import static com.taotao.cloud.common.base.StrPool.COLON;

import cn.hutool.core.util.StrUtil;
import java.time.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * hash 缓存 key 封装
 *
 * @author zuihou
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CacheHashKey extends CacheKey {

	/**
	 * redis hash field
	 */
	@NonNull
	private Object field;

	public CacheHashKey(@NonNull String key, final @NonNull Object field) {
		super(key);
		this.field = field;
	}

	public CacheHashKey(@NonNull String key, final @NonNull Object field, Duration expire) {
		super(key, expire);
		this.field = field;
	}

	public CacheKey tran() {
		return new CacheKey(StrUtil.join(COLON, getKey(), getField()), getExpire());
	}
}
