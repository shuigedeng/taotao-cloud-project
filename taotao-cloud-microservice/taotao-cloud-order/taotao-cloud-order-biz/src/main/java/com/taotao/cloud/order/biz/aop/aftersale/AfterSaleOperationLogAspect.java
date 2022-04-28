package com.taotao.cloud.order.biz.aop.aftersale;

import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.spel.SpelUtil;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
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
 * @since 2022-04-28 08:56:31
 */
@Aspect
@Component
public class AfterSaleOperationLogAspect {

	@Autowired
	private ApplicationEventPublisher publisher;

	@AfterReturning(returning = "rvt", pointcut = "@annotation(com.taotao.cloud.order.biz.aop.aftersale.AfterSaleLogPoint)")
	public void afterReturning(JoinPoint joinPoint, Object rvt) {
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

			Map<String, String> afterSaleLogPoints = spelFormat(joinPoint, rvt);
			AfterSaleLog afterSaleLog = new AfterSaleLog(afterSaleLogPoints.get("sn"), id, role,
				userName, afterSaleLogPoints.get("description"));

			publisher.publishEvent(new AfterSaleLogEvent(afterSaleLog));
		} catch (Exception e) {
			LogUtil.error("售后日志错误", e);
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param joinPoint 切点
	 * @return 方法描述
	 */
	public static Map<String, String> spelFormat(JoinPoint joinPoint, Object rvt) {
		Map<String, String> result = new HashMap<>(2);
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		AfterSaleLogPoint afterSaleLogPoint = signature.getMethod()
			.getAnnotation(AfterSaleLogPoint.class);
		String description = SpelUtil.compileParams(joinPoint, rvt,
			afterSaleLogPoint.description());
		String sn = SpelUtil.compileParams(joinPoint, rvt, afterSaleLogPoint.sn());
		result.put("description", description);
		result.put("sn", sn);
		return result;

	}
}
