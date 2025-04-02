package com.taotao.cloud.tx.rm.aspect;

import com.taotao.cloud.tx.rm.annotation.DistributedTransactional;
import com.taotao.cloud.tx.rm.transactional.TransactionalType;
import com.taotao.cloud.tx.rm.transactional.TtcTx;
import com.taotao.cloud.tx.rm.transactional.TtcTxParticipant;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

// 负责拦截自定义注解的切面
@Aspect
@Component
public class TtcTransactionalAspect implements Ordered {

	@Around("@annotation(com.taotao.cloud.tx.rm.annotation.DistributedTransactional)")
	public Integer invoke(ProceedingJoinPoint proceedingJoinPoint) {
		System.out.println("分布式事务注解生效，切面成功拦截............");

		// 获取对应注解的业务方法，以及方法上的注解对象
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedTransactional zta = method.getAnnotation(DistributedTransactional.class);

		// 创建事务组
		String groupId = "";
		// 如果目前触发切面的方法，是一组全局事务的第一个子事务
		if (zta.isStart()) {
			// 则向事务管理者注册一个事务组
			groupId = TtcTxParticipant.createTtcTransactionalManagerGroup();
		}
		// 否则获取当前事务所属的事务组ID
		else {
			groupId = TtcTxParticipant.getCurrentGroupId();
		}

		// 创建子事务
		TtcTx ttcTx = TtcTxParticipant.createTransactional(groupId);

		// spring会开启MySQL事务
		try {
			//执行spring切面（dataSource切面），执行具体的业务方法
			Object result = proceedingJoinPoint.proceed();

			// 没有抛出异常证明该事务可以提交，把子事务添加进事务组
			TtcTxParticipant.addTtcTransactional(ttcTx, zta.isEnd(),
				TransactionalType.commit);

			// 返回执行成功的结果
			return (Integer) result;
		}
		catch (Exception e) {
			e.printStackTrace();
			// 抛出异常证明该事务需要回滚，把子事务添加进事务组
			TtcTxParticipant.addTtcTransactional(ttcTx, zta.isEnd(),
				TransactionalType.rollback);
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
			// 把子事务添加进事务组,抛出异常证明该事务需要回滚
			TtcTxParticipant.addTtcTransactional(ttcTx, zta.isEnd(),
				TransactionalType.rollback);
			// 返回执行失败的结果
			return -1;
		}
		return -1;
	}

	// 设置优先级，让前面拦截事务的切面先执行
	@Override
	public int getOrder() {
		return 10000;
	}
}
