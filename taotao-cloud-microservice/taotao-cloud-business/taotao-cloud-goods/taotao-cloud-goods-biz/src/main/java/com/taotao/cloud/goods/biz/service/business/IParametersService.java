package com.taotao.cloud.goods.biz.service.business;

import com.taotao.cloud.goods.biz.model.entity.Parameters;
import com.taotao.cloud.web.base.service.BaseSuperService;

/**
 * 商品参数业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:58
 */
public interface IParametersService extends BaseSuperService<Parameters, Long> {

	/**
	 * 更新参数组信息
	 *
	 * @param parameters 参数组信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:58
	 */
	Boolean updateParameter(Parameters parameters);

}
