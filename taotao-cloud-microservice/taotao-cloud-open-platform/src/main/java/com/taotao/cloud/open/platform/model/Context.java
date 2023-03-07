package com.taotao.cloud.open.platform.model;

import java.util.Collection;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 上下文对象，保存程序运行所需数据
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:12:00
 */
@Data
@Component
public class Context {

	/**
	 * api处理器集合
	 */
	private Collection<ApiHandler> apiHandlers;
}
