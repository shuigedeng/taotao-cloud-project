package com.taotao.cloud.log.biz.shortlink.component;

import com.taotao.cloud.log.biz.shortlink.adapter.ShortLinkGeneratorAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 短链生成器 - murmur哈希算法实现
 * <p>
 * 1.不依赖于外部服务，没有额外的网络请求
 * <p>
 * 2.非加密型哈希函数，且分散性表现良好，冲突小
 * <p>
 * 3.32位murmur算法，可生成43亿10进制，满足业务需求
 * <p>
 * 4.不暴露业务数据
 * <p>
 * 5.无法为统一字符串生成不同的code
 *
 * @since 2022/05/04
 */
@Slf4j
@Component
public class HashShortLinkGenerator implements ShortLinkGeneratorAdapter {

	/**
	 * 生成短链码 - 无法为相同字符串生成不同的短链码，可以在字符串中拼接时间戳，再生成
	 *
	 * @param originUrl 原生URL
	 * @return 短链码
	 */
	@Override
	public String createShortLinkCode(String originUrl) {
		return CommonBizUtil.encodeToBase62(CommonBizUtil.murmurHash32(originUrl));
	}
}