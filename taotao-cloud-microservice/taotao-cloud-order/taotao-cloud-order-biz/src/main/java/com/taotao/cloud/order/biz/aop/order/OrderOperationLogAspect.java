package com.taotao.cloud.order.biz.aop.order;

import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.spel.SpelUtil;
import com.taotao.cloud.order.biz.entity.order.OrderLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单操作日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:40
 */
@Aspect
@Component
public class OrderOperationLogAspect {

	@Autowired
	private ApplicationEventPublisher publisher;

	@After("@annotation(com.taotao.cloud.order.biz.aop.order.OrderLogPoint)")
	public void doAfter(JoinPoint joinPoint) {
		try {
			SecurityUser securityUser = SecurityUtil.getUser();
			//日志对象拼接
			//默认操作人员，系统操作
			String userName = "系统操作";
			Long id = -1L;
			String role = UserEnum.SYSTEM.name();
			if (securityUser != null) {
				//日志对象拼接
				userName = securityUser.getUsername();
				id = securityUser.getUserId();
				role = UserEnum.getByCode(securityUser.getType());
			}

			Map<String, String> orderLogPoints = spelFormat(joinPoint);
			OrderLog orderLog = new OrderLog(orderLogPoints.get("orderSn"), id, role, userName,
				orderLogPoints.get("description"));

			publisher.publishEvent(new OrderLogEvent(orderLog));
		} catch (Exception e) {
			LogUtil.error("订单日志错误", e);
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param joinPoint 切点
	 * @return 方法描述
	 */
	private static Map<String, String> spelFormat(JoinPoint joinPoint) {

		Map<String, String> result = new HashMap<>(2);
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		OrderLogPoint orderLogPoint = signature.getMethod().getAnnotation(OrderLogPoint.class);
		String description = SpelUtil.compileParams(joinPoint, orderLogPoint.description());
		String orderSn = SpelUtil.compileParams(joinPoint, orderLogPoint.orderSn());

		result.put("description", description);
		result.put("orderSn", orderSn);
		return result;
	}
}
