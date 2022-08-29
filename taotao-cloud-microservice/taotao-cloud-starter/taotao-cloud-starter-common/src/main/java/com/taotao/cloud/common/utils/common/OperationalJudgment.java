package com.taotao.cloud.common.utils.common;


import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.reflect.ReflectionUtils;
import java.util.Objects;

/**
 * 全局统一判定是否可操作某属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:39:01
 */
public final class OperationalJudgment {

	/**
	 * 需要判定的对象必须包含属性 memberId，storeId 代表判定的角色
	 *
	 * @param object 判定的对象
	 * @param <T>    判定处理对象
	 * @return 处理结果
	 */
	public static <T> T judgment(T object) {
		return judgment(object, "memberId", "storeId");
	}

	/**
	 * 需要判定的对象必须包含属性 memberId，storeId 代表判定的角色
	 *
	 * @param object       判定对象
	 * @param buyerIdField 买家id
	 * @param storeIdField 店铺id
	 * @param <T>          范型
	 * @return 返回判定本身，防止多次查询对象
	 */
	public static <T> T judgment(T object, String buyerIdField, String storeIdField) {
		Integer type = SecurityUtils.getCurrentUser().getType();
		UserEnum userEnum = UserEnum.getEnumByCode(type);
		switch (Objects.requireNonNull(userEnum)) {
			case MANAGER:
				return object;
			case MEMBER:
				if (SecurityUtils.getCurrentUser().getUserId()
					.equals(ReflectionUtils.getFieldValue(object, buyerIdField))) {
					return object;
				} else {
					throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
				}
			case STORE:
				if (SecurityUtils.getCurrentUser().getStoreId()
					.equals(ReflectionUtils.getFieldValue(object, storeIdField))) {
					return object;
				} else {
					throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
				}
			default:
				throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		}
	}
}
