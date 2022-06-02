package com.taotao.cloud.operation.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.biz.entity.Special;

/**
 * 专题活动业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 15:06:11
 */
public interface SpecialService extends IService<Special> {

	/**
	 * 添加专题活动
	 *
	 * @param special 专题活动
	 * @return {@link Special }
	 * @since 2022-06-02 15:06:11
	 */
	Special addSpecial(Special special);

	/**
	 * 删除专题活动
	 *
	 * @param id 活动ID
	 * @return boolean
	 * @since 2022-06-02 15:06:11
	 */
	boolean removeSpecial(String id);

}
