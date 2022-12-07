package com.taotao.cloud.data.sync.batch.single;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class SingleProcessorService implements ItemProcessor<String, String> {

	public static String[] message;

	//在此处进行数据处理操作，如进行计算、逻辑处理、格式转换等，本例将数据变成全大写数据；
	@Override
	public String process(String data) throws Exception {
		//存储处理过的数据，可供下一个step使用
		List<String> list = new ArrayList<>();
		if (message != null) {
			list.addAll(Arrays.asList(message));
		}
		list.add(data.toUpperCase());
		message = list.toArray(new String[list.size()]);
		return data.toUpperCase();
	}
}
