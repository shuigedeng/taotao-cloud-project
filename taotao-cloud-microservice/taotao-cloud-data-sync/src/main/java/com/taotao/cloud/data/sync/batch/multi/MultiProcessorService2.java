package com.taotao.cloud.data.sync.batch.multi;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author : dylanz
 * @since : 08/26/2020
 */
public class MultiProcessorService2 implements ItemProcessor<String, String> {

	@Override
	public String process(String data) throws Exception {
		return data + " dylanz";
	}
}
