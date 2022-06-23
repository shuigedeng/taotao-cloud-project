package com.taotao.cloud.promotion.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import org.apache.ibatis.annotations.Update;

/**
 * 秒杀活动数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:37:18
 */
public interface SeckillMapper extends BaseMapper<Seckill> {

	/**
	 * 修改秒杀活动数量
	 *
	 * @param seckillId 秒杀活动ID
	 */
	@Update("""
		UPDATE tt_seckill SET goods_num =( SELECT count( id )
		FROM tt_seckill_apply WHERE seckill_id = #{seckillId} ) 
		WHERE id = #{seckillId}
		""")
	void updateSeckillGoodsNum(String seckillId);
}
