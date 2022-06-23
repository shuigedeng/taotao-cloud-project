package com.taotao.cloud.promotion.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 砍价活动商品数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:36:49
 */
public interface KanJiaActivityGoodsMapper extends BaseMapper<KanjiaActivityGoods> {

	/**
	 * 获取砍价商品VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 砍价商品VO分页
	 */
	@Select("SELECT * FROM tt_kanjia_activity_goods ${ew.customSqlSegment}")
	IPage<KanjiaActivityGoodsListVO> kanjiaActivityGoodsVOPage(IPage<KanjiaActivityGoods> page,
															   @Param(Constants.WRAPPER) Wrapper<KanjiaActivityGoods> queryWrapper);

}
