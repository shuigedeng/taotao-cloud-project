package com.taotao.cloud.message.biz.austin.handler.deduplication;

import com.taotao.cloud.message.biz.austin.handler.deduplication.builder.Builder;
import com.taotao.cloud.message.biz.austin.handler.deduplication.service.DeduplicationService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;


/**
 * @author huskey
 * @date 2022/1/18
 */
@Service
public class DeduplicationHolder {

	private final Map<Integer, Builder> builderHolder = new HashMap<>(4);
	private final Map<Integer, DeduplicationService> serviceHolder = new HashMap<>(4);

	public Builder selectBuilder(Integer key) {
		return builderHolder.get(key);
	}

	public DeduplicationService selectService(Integer key) {
		return serviceHolder.get(key);
	}

	public void putBuilder(Integer key, Builder builder) {
		builderHolder.put(key, builder);
	}

	public void putService(Integer key, DeduplicationService service) {
		serviceHolder.put(key, service);
	}
}
