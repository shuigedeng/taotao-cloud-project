package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.biz.model.entity.Parameters;

/**
 * 商品参数业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:58
 */
public interface IParametersService extends IService<Parameters> {

	/**
	 * 更新参数组信息
	 *
	 * @param parameters 参数组信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:58
	 */
	Boolean updateParameter(Parameters parameters);

}
