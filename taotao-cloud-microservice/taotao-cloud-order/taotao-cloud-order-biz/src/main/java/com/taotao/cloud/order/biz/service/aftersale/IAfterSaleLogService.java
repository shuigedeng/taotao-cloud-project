package com.taotao.cloud.order.biz.service.aftersale;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;

import java.util.List;

/**
 * 订单日志业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:07
 */
public interface IAfterSaleLogService extends IService<AfterSaleLog> {

	/**
	 * 获取售后日志
	 *
	 * @param sn 售后编号
	 * @return {@link List }<{@link AfterSaleLog }>
	 * @since 2022-04-28 08:49:07
	 */
	List<AfterSaleLog> getAfterSaleLog(String sn);
}
