package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.entity.sensitive.SensitiveWord;

/**
 * 敏感词业务层
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:33:20
 */
public interface ISensitiveWordService extends IService<SensitiveWord> {

	/**
	 * 重新写入缓存
	 *
	 * @since 2022-03-25 14:33:25
	 */
	void resetCache();

}
