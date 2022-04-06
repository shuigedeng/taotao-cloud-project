package com.taotao.cloud.order.biz.mapper.aftersale;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVOVO123;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 售后数据处理层
 */
public interface AfterSaleMapper extends BaseMapper<AfterSale> {

	/**
	 * 获取售后VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 售后VO分页
	 */
	@Select("SELECT * FROM li_after_sale ${ew.customSqlSegment}")
	IPage<AfterSaleVOVO123> queryByParams(IPage<AfterSaleVOVO123> page,
		@Param(Constants.WRAPPER) Wrapper<AfterSaleVOVO123> queryWrapper);

	/**
	 * 根据售后编号获取售后VO
	 *
	 * @param sn 售后编号
	 * @return 售后VO
	 */
	@Select("SELECT * FROM li_after_sale WHERE sn=#{sn}")
	AfterSaleVOVO123 getAfterSaleVO(String sn);
}
