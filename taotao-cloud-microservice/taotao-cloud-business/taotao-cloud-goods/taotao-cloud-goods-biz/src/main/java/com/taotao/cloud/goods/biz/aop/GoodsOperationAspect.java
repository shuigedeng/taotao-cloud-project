package com.taotao.cloud.goods.biz.aop;

import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.spel.SpelUtils;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 订单操作日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:40
 */
@Aspect
@Component
public class GoodsOperationAspect {

	@Autowired
	private ApplicationEventPublisher publisher;

	@After("@annotation(com.taotao.cloud.goods.biz.aop.GoodsLogPoint)")
	public void doAfter(JoinPoint joinPoint) {
	}

}
