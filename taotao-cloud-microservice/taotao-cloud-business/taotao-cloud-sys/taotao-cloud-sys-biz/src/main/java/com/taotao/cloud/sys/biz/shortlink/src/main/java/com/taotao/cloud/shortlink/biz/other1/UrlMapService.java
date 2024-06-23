package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other1;

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
