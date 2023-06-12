/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.biz.task;
//
// import com.taotao.cloud.goods.biz.service.business.IGoodsService;
// import com.taotao.cloud.job.xxl.timetask.EveryDayExecute;
// import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationApi;
// import jakarta.annotation.Resource;
// import java.util.List;
// import java.util.Map;
// import org.springframework.stereotype.Component;
//
/// **
// * 商品定时器
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-27 16:54:37
// */
// @Component
// public class GoodsExecute implements EveryDayExecute {
//
//	/**
//	 * 成员评价映射器 会员评价
//	 */
//	@Resource
//	private IFeignMemberEvaluationApi memberEvaluationApi;
//	/**
//	 * 货物映射器 商品
//	 */
//	@Resource
//	private IGoodsService goodsService;
//
//	/**
//	 * 查询已上架的商品的评价数量并赋值
//	 */
//	@Override
//	public void execute() {
//		//查询上次统计到本次的评价数量
//		List<Map<String, Object>> list = memberEvaluationApi.memberEvaluationNum();
//
//		for (Map<String, Object> map : list) {
//			goodsService.addGoodsCommentNum(Convert.toInt(map.get("num").toString()),
//				Convert.toLong(map.get("goods_id")));
//		}
//
//	}
// }
