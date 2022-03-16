package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.entity.sensitive.SensitiveWord;

/**
 * 敏感词业务层
 */
public interface ISensitiveWordService extends IService<SensitiveWord> {

	/**
	 * 重新写入缓存
	 */
	void resetCache();

}
