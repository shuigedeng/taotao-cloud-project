package com.taotao.cloud.message.biz.austin.handler.deduplication.builder;

import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.AnchorState;
import com.taotao.cloud.message.biz.austin.common.enums.DeduplicationType;
import com.taotao.cloud.message.biz.austin.handler.deduplication.DeduplicationParam;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * @author huskey
 * @date 2022/1/18
 */

@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

	public FrequencyDeduplicationBuilder() {
		deduplicationType = DeduplicationType.FREQUENCY.getCode();
	}

	@Override
	public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
		DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType,
			deduplication, taskInfo);
		if (deduplicationParam == null) {
			return null;
		}
		deduplicationParam.setDeduplicationTime(
			(DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
		deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
		return deduplicationParam;
	}
}
