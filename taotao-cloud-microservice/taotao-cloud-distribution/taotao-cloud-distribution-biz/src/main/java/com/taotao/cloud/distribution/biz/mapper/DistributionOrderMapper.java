package com.taotao.cloud.distribution.biz.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.distribution.biz.entity.DistributionOrder;
import org.apache.ibatis.annotations.Update;

/**
 * 分销订单数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 11:27:46
 */
public interface DistributionOrderMapper extends BaseMapper<DistributionOrder> {

	/**
	 * 修改分销员提现金额
	 *
	 * @param distributionOrderStatus 分销订单状态
	 * @param settleCycle             时间
	 */
	@Update("""
		UPDATE li_distribution AS d 
		SET d.can_rebate = ( d.can_rebate + (SELECT SUM( dorder.rebate )
		 											FROM li_distribution_order AS dorder 
		 											WHERE dorder.distribution_order_status = #{distributionOrderStatus} AND dorder.settle_cycle< #{settleCycle} AND dorder.distribution_id = d.id 
		 											)
		 					)
		            ,d.commission_frozen = (d.commission_frozen - (SELECT SUM( dorder.rebate ) 
		            												FROM li_distribution_order AS dorder
		            												 WHERE dorder.distribution_order_status = #{distributionOrderStatus} AND dorder.settle_cycle< #{settleCycle} AND dorder.distribution_id = d.id 
		            						) 
		            		)
		""")
	void rebate(String distributionOrderStatus, DateTime settleCycle);

}
