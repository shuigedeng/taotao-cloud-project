package com.taotao.cloud.distribution.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


/**
 * 分销员数据处理层
 */
public interface DistributionMapper extends BaseMapper<Distribution> {

	/**
	 * 修改分销员可提现金额
	 *
	 * @param commissionFrozen 分销金额
	 * @param distributionId   分销员ID
	 */
	@Update("""
		UPDATE tt_distribution set commission_frozen = (commission_frozen+#{commissionFrozen}) , rebate_total=(rebate_total+#{commissionFrozen}) 
		WHERE id = #{distributionId}
		""")
	void subCanRebate(BigDecimal commissionFrozen, String distributionId);

	/**
	 * 添加分销金额
	 *
	 * @param commissionFrozen 分销金额
	 * @param distributionId   分销员ID
	 */
	@Update("""
		UPDATE tt_distribution set commission_frozen = (commission_frozen+#{commissionFrozen})
				, rebate_total=(rebate_total+#{commissionFrozen})
				, distribution_order_count=(distribution_order_count+1) WHERE id = #{distributionId}
		""")
	void addCanRebate(BigDecimal commissionFrozen, String distributionId);

}
