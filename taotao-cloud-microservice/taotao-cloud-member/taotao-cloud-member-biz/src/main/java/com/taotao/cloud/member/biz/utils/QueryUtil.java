package com.taotao.cloud.member.biz.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.api.model.query.EvaluationPageQuery;
import java.util.Objects;

public class QueryUtil {
	 /**
	  * 构造查询条件
	  *
	  * @return 查询条件
	  * @author shuigedeng
	  * @since 2022/3/14 11:22
	  */
	 public static <T> QueryWrapper<T> evaluationQueryWrapper(EvaluationPageQuery evaluationPageQuery) {
	 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getStartTime()) && StringUtils.isNotEmpty(evaluationPageQuery.getEndTime())) {
	 		queryWrapper.between("create_time", evaluationPageQuery.getStartTime(), evaluationPageQuery.getEndTime());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getGrade())) {
	 		queryWrapper.eq("grade", evaluationPageQuery.getGrade());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getGoodsName())) {
	 		queryWrapper.like("goods_name", evaluationPageQuery.getGoodsName());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getStoreName())) {
	 		queryWrapper.like("store_name", evaluationPageQuery.getStoreName());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getMemberName())) {
	 		queryWrapper.like("member_name", evaluationPageQuery.getMemberName());
	 	}
	 	if (Objects.nonNull(evaluationPageQuery.getGoodsId())) {
	 		queryWrapper.eq("goods_id", evaluationPageQuery.getGoodsId());
	 	}
	 	if (Objects.nonNull(evaluationPageQuery.getStoreId())) {
	 		queryWrapper.eq("store_id", evaluationPageQuery.getStoreId());
	 	}
	 	if (Objects.nonNull(evaluationPageQuery.getMemberId())) {
	 		queryWrapper.eq("member_id", evaluationPageQuery.getMemberId());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getHaveImage())) {
	 		queryWrapper.eq("have_image", evaluationPageQuery.getHaveImage());
	 	}
	 	if (StringUtils.isNotEmpty(evaluationPageQuery.getStatus())) {
	 		queryWrapper.eq("status", evaluationPageQuery.getStatus());
	 	}
	 	queryWrapper.eq("delete_flag", false);
	 	queryWrapper.orderByDesc("create_time");
	 	return queryWrapper;
	 }
}
