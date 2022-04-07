package com.taotao.cloud.order.biz.service.aftersale;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import java.util.List;

/**
 * 售后原因业务层
 */
public interface AfterSaleReasonService extends IService<AfterSaleReason> {

	/**
	 * 获取售后原因列表
	 *
	 * @param serviceType 售后类型
	 * @return 售后原因列表
	 */
	List<AfterSaleReason> afterSaleReasonList(String serviceType);


	/**
	 * 修改售后原因
	 *
	 * @param afterSaleReason 售后原因
	 * @return 售后原因
	 */
	AfterSaleReason editAfterSaleReason(AfterSaleReason afterSaleReason);

}
