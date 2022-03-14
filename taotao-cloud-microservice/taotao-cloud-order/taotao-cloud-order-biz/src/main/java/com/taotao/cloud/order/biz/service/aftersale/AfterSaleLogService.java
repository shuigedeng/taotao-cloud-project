package com.taotao.cloud.order.biz.service.aftersale;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import java.util.List;

/**
 * 订单日志业务层
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:20:47
 */
public interface AfterSaleLogService extends IService<AfterSaleLog> {

	/**
	 * 获取售后日志
	 *
	 * @param sn 售后编号
	 * @return 售后日志列表
	 */
	List<AfterSaleLog> getAfterSaleLog(String sn);
}
