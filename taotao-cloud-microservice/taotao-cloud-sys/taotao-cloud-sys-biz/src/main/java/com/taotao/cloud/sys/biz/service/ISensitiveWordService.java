package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.entity.SensitiveWord;

/**
 * 敏感词业务层
 *
 * @author Bulbasaur
 * @since 2020/11/17 8:02 下午
 */
public interface ISensitiveWordService extends IService<SensitiveWord> {

	/**
	 * 重新写入缓存
	 */
	void resetCache();

}
