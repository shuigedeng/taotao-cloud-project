package com.taotao.cloud.message.biz.austin.handler.deduplication.builder;


import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.deduplication.DeduplicationParam;

/**
 * @author luohaojie
 * @date 2022/1/18
 */
public interface Builder {

	String DEDUPLICATION_CONFIG_PRE = "deduplication_";

	/**
	 * 根据配置构建去重参数
	 *
	 * @param deduplication
	 * @param taskInfo
	 * @return
	 */
	DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
