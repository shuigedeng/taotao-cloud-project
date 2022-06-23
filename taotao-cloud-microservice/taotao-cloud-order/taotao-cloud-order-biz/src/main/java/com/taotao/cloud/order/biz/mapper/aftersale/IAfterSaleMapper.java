package com.taotao.cloud.order.biz.mapper.aftersale;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.api.web.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 售后数据处理层
 *
 * @author shuigedeng
 */
public interface IAfterSaleMapper extends BaseMapper<AfterSale> {

	/**
	 * 获取售后VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 售后VO分页
	 */
	@Select("SELECT * FROM tt_after_sale ${ew.customSqlSegment}")
	IPage<AfterSale> queryByParams(IPage<AfterSaleVO> page,
		@Param(Constants.WRAPPER) Wrapper<AfterSaleVO> queryWrapper);
}
