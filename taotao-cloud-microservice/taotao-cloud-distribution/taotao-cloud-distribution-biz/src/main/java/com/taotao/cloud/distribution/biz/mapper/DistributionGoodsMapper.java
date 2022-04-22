package com.taotao.cloud.distribution.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.distribution.api.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.entity.DistributionGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 分销商品数据处理层
 */
public interface DistributionGoodsMapper extends BaseMapper<DistributionGoods> {

	/**
	 * 获取分销员未选择商品VO分页
	 *
	 * @param page           分页
	 * @param queryWrapper   查询条件
	 * @param distributionId 分销员ID
	 * @return 分销员未选择商品VO分页
	 */
	@Select("""
		SELECT dg.*
		FROM li_distribution_goods dg
		WHERE dg.id NOT IN(SELECT distribution_goods_id
		 					FROM li_distribution_selected_goods
		  					WHERE distribution_id=${distributionId}
		  				 )
		${ew.customSqlSegment}
		""")
	IPage<DistributionGoodsVO> notSelectGoods(IPage<DistributionGoodsVO> page, @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper, String distributionId);

	/**
	 * 获取分销员已选择分销商品VO分页
	 *
	 * @param page           分页
	 * @param queryWrapper   查询条件
	 * @param distributionId 分销员ID
	 * @return 分销员已选择分销商品VO分页
	 */
	@Select("""
		SELECT dg.* 
		FROM li_distribution_goods dg 
		WHERE dg.id IN (SELECT distribution_goods_id 
						FROM li_distribution_selected_goods 
						WHERE distribution_id=${distributionId}
					)
		 ${ew.customSqlSegment}
		""")
	IPage<DistributionGoodsVO> selectGoods(IPage<DistributionGoodsVO> page, @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper, String distributionId);

	/**
	 * 获取分销商品VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 分销商品VO分页
	 */
	@Select("""
		SELECT dg.* 
		FROM li_distribution_goods dg 
		${ew.customSqlSegment}
		""")
	IPage<DistributionGoodsVO> getDistributionGoodsVO(IPage<DistributionGoodsVO> page, @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper);

}
