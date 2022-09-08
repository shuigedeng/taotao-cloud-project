package com.taotao.cloud.data.mybatisplus.dynamic.config;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 动态解析数据源 取最后一个参数
 *
 *  @DS("#last")
 */
public class DsLastParamProcessor extends DsProcessor {

	private static final String LAST_PREFIX = "#last";

	/**
	 * 匹配才会执行，否则走下一级执行器 默认有三个职责链来处理动态参数解析器 header->session->spel
	 * @param key
	 * @return 是否匹配当前执行器
	 */
	@Override
	public boolean matches(String key) {
		return key.startsWith(LAST_PREFIX);
	}

	/**
	 * 抽象最终决定数据源,这里取方法最后一个参数
	 * @param invocation 方法执行信息
	 * @param key DS注解里的内容
	 * @return 数据源名称
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		return String.valueOf(invocation.getArguments()[invocation.getArguments().length - 1]);
	}

}
