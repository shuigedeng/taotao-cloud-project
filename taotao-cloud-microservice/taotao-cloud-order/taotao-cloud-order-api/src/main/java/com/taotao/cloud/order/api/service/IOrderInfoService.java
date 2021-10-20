package com.taotao.cloud.order.api.service;


import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;
import java.util.List;

/**
 * 订单管理service
 *
 * @author shuigedeng
 * @since 2020/4/30 11:03
 */
public interface IOrderInfoService<T extends BaseSuperEntity<T, I>, I extends Serializable> extends
	BaseSuperService<T, I> {

	Boolean existByCode(String code);

}

