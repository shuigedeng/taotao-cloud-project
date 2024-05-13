package com.taotao.cloud.tx.rm.aspect;

import com.taotao.cloud.tx.rm.connection.ZhuziConnection;
import com.taotao.cloud.tx.rm.transactional.ZhuziTxParticipant;
import java.sql.Connection;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// 剥夺并接管Spring事务控制权的切面
@Aspect
@Component
public class ZhuziDataSourceAspect {

	@Around("execution(* javax.sql.DataSource.getConnection(..))")
	public Connection dataSourceAround(ProceedingJoinPoint proceedingJoinPoint)
		throws Throwable {
		System.out.println("事务切面成功拦截，正在接管控制权......");

		// 如果当前调用事务接口的线程正在参与分布式事务，
		// 则返回自定义的Connection对象接管事务控制权
		if (ZhuziTxParticipant.getCurrent() != null) {
			System.out.println("返回自定义的Connection对象.......");
			Connection connection = (Connection) proceedingJoinPoint.proceed();
			return new ZhuziConnection(connection, ZhuziTxParticipant.getCurrent());
		}

		// 如果当前线程没有参与分布式事务，让其正常提交/回滚事务
		System.out.println("返回JDBC的Connection对象.............");
		return (Connection) proceedingJoinPoint.proceed();
	}
}
