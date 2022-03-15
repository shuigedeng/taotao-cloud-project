package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.biz.entity.Parameters;

/**
 * 商品参数业务层
 */
public interface ParametersService extends IService<Parameters> {

	/**
	 * 更新参数组信息
	 *
	 * @param parameters 参数组信息
	 * @return 是否更新成功
	 */
	Boolean updateParameter(Parameters parameters);

}
