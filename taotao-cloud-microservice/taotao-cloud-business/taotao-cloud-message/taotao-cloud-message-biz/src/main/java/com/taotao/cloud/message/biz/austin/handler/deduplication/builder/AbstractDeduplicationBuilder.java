package com.taotao.cloud.message.biz.austin.handler.deduplication.builder;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.deduplication.DeduplicationHolder;
import com.taotao.cloud.message.biz.austin.handler.deduplication.DeduplicationParam;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 3y
 * @date 2022/1/19
 */
public abstract class AbstractDeduplicationBuilder implements Builder {

	protected Integer deduplicationType;

	@Autowired
	private DeduplicationHolder deduplicationHolder;

	@PostConstruct
	public void init() {
		deduplicationHolder.putBuilder(deduplicationType, this);
	}

	public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig,
			TaskInfo taskInfo) {
		JSONObject object = JSONObject.parseObject(duplicationConfig);
		if (object == null) {
			return null;
		}
		DeduplicationParam deduplicationParam = JSONObject.parseObject(
				object.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
		if (deduplicationParam == null) {
			return null;
		}
		deduplicationParam.setTaskInfo(taskInfo);
		return deduplicationParam;
	}

}
