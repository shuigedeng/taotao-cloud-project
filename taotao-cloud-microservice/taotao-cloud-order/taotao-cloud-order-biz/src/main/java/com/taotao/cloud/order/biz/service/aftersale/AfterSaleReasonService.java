package com.taotao.cloud.order.biz.service.aftersale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleReasonPageQuery;
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
	Boolean editAfterSaleReason(AfterSaleReason afterSaleReason);

	/**
	 * 分页查询售后原因
	 *
	 * @param afterSaleReasonPageQuery 查询条件
	 * @return 售后原因
	 */
    IPage<AfterSaleReason> getByPage(AfterSaleReasonPageQuery afterSaleReasonPageQuery);

}
