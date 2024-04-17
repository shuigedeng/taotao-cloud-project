package com.taotao.cloud.data.sync.partitioner;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.io.IOException;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

public class UserPartitioner implements Partitioner {
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> result = new HashMap<>(gridSize);

		int range = 10; //文件间隔
		int start = 1; //开始位置
		int end = 10; //结束位置
		String text = "user%s-%s.txt";

		for (int i = 0; i < gridSize; i++) {
			ExecutionContext value = new ExecutionContext();
			Resource resource = new ClassPathResource(String.format(text, start, end));
			try {
				value.putString("file", resource.getURL().toExternalForm());
			} catch (IOException e) {
				LogUtils.error(e);
			}
			start += range;
			end += range;

			result.put("user_partition_" + i, value);
		}
		return result;
	}
}
