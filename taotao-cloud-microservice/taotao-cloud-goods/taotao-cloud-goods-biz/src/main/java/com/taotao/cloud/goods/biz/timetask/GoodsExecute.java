package com.taotao.cloud.goods.biz.timetask;

import cn.hutool.core.convert.Convert;
import com.taotao.cloud.goods.biz.mapper.GoodsMapper;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationService;
import com.taotao.cloud.web.timetask.EveryDayExecute;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品定时器
 */
@Component
public class GoodsExecute implements EveryDayExecute {
	/**
	 * 会员评价
	 */
	@Resource
	private IFeignMemberEvaluationService memberEvaluationMapper;
	/**
	 * 商品
	 */
	@Resource
	private GoodsMapper goodsMapper;

	/**
	 * 查询已上架的商品的评价数量并赋值
	 */
	@Override
	public void execute() {
		//查询上次统计到本次的评价数量
		List<Map<String, Object>> list = memberEvaluationMapper.memberEvaluationNum().data();

		for (Map<String, Object> map : list) {
			goodsMapper.addGoodsCommentNum(Convert.toInt(map.get("num").toString()), Convert.toLong(map.get("goods_id")));
		}

	}
}
