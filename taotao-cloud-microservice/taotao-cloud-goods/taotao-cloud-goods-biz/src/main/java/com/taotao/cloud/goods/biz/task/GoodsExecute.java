package com.taotao.cloud.goods.biz.task;

import cn.hutool.core.convert.Convert;
import com.taotao.cloud.goods.biz.service.business.IGoodsService;
import com.taotao.cloud.job.xxl.timetask.EveryDayExecute;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationApi;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 商品定时器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:54:37
 */
@Component
public class GoodsExecute implements EveryDayExecute {

	/**
	 * 成员评价映射器 会员评价
	 */
	@Resource
	private IFeignMemberEvaluationApi feignMemberEvaluationApi;
	/**
	 * 货物映射器 商品
	 */
	@Resource
	private IGoodsService goodsService;

	/**
	 * 查询已上架的商品的评价数量并赋值
	 */
	@Override
	public void execute() {
		//查询上次统计到本次的评价数量
		List<Map<String, Object>> list = feignMemberEvaluationApi.memberEvaluationNum();

		for (Map<String, Object> map : list) {
			goodsService.addGoodsCommentNum(Convert.toInt(map.get("num").toString()),
					Convert.toLong(map.get("goods_id")));
		}

	}
}
