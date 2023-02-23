package com.taotao.cloud.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.payment.biz.entity.RefundLog;

/**
 * 退款日志 业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 16:46:59
 */
public interface RefundLogService extends IService<RefundLog> {
	/**
	 * 根据售后sn查询退款日志
	 *
	 * @param sn
	 * @return {@link RefundLog }
	 * @since 2022-05-30 16:46:59
	 */
	RefundLog queryByAfterSaleSn(String sn);
}
