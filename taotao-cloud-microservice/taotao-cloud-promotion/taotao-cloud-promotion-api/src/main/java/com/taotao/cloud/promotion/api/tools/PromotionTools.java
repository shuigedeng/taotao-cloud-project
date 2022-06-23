package com.taotao.cloud.promotion.api.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.web.vo.BasePromotionsVO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * 优惠活动通用验证类
 */
public class PromotionTools {

	public static final String START_TIME_COLUMN = "start_time";
	public static final String END_TIME_COLUMN = "end_time";
	public static final String PLATFORM_ID = "platform";
	public static final String PLATFORM_NAME = "platform";

	/**
	 * 参数验证
	 * 1、活动起始时间必须大于当前时间
	 * 2、验证活动开始时间是否大于活动结束时间
	 *
	 * @param startTime 活动开始时间
	 * @param endTime   活动结束时间
	 */
	public static void checkPromotionTime(LocalDateTime startTime, LocalDateTime endTime) {

		if (startTime == null) {
			throw new BusinessException(ResultEnum.PROMOTION_TIME_NOT_EXIST);
		}

		LocalDateTime now = LocalDateTime.now();

		//如果活动起始时间小于现在时间
		if (now.isAfter(startTime)) {
			throw new BusinessException(ResultEnum.PROMOTION_START_TIME_ERROR);
		}
		//如果活动结束时间小于现在时间
		if (endTime != null && now.isAfter(endTime)) {
			throw new BusinessException(ResultEnum.PROMOTION_END_TIME_ERROR);
		}

		//开始时间不能大于结束时间
		if (endTime != null && startTime.isAfter(endTime)) {
			throw new BusinessException(ResultEnum.PROMOTION_TIME_ERROR);
		}
	}


	public static <T> Consumer<QueryWrapper<T>> queryPromotionStatus(PromotionsStatusEnum promotionsStatusEnum) {
		return switch (promotionsStatusEnum) {
			case NEW ->
				(QueryWrapper<T> t) -> t.nested(i -> i.gt(START_TIME_COLUMN, new Date()).gt(END_TIME_COLUMN, new Date()));
			case START ->
				(QueryWrapper<T> t) -> t.nested(i -> i.le(START_TIME_COLUMN, new Date()).ge(END_TIME_COLUMN, new Date()));
			case END ->
				(QueryWrapper<T> t) -> t.nested(i -> i.lt(START_TIME_COLUMN, new Date()).lt(END_TIME_COLUMN, new Date()));
			case CLOSE -> (QueryWrapper<T> t) -> t.nested(i -> i.isNull(START_TIME_COLUMN).isNull(END_TIME_COLUMN));
		};
	}


	public static int nextHour(String[] totalHours, Integer timeline) {
		int nextHour = 23;
		int[] hoursSored = Arrays.stream(totalHours).mapToInt(Integer::parseInt).toArray();
		Arrays.sort(hoursSored);
		for (int i : hoursSored) {
			if (timeline < i) {
				nextHour = i;
				break;
			}
		}
		return nextHour;
	}

	public static Map<String, Object> filterInvalidPromotionsMap(Map<String, Object> map) {
		if (map == null) {
			return new HashMap<>();
		}
		//移除无效促销活动
		return map.entrySet().stream().filter(i -> {
			JSONObject promotionsObj = JSONUtil.parseObj(i.getValue());
			BasePromotionsVO basePromotions = promotionsObj.toBean(BasePromotionsVO.class);
			// todo 此处需要修改
//			if (basePromotions.getStartTime() != null && basePromotions.getEndTime() != null) {
//				return basePromotions.getStartTime().getTime() <= System.currentTimeMillis() && basePromotions.getEndTime().getTime() >= System.currentTimeMillis();
//			}
			return true;
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
