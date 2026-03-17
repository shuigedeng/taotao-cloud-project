package com.taotao.cloud.shortlink.biz.infrastructure.other1;

import java.util.Optional;

public interface UrlMapService {
  /**
   * 解码
   * @param longUrl
   * @return
   */
  String encode(String longUrl);

	/**
	 * 解码
	 * @param shortKey 需要进行解码的短链接 Key 值
	 * @return
	 */
	Optional<String> decode(String shortKey);
}
