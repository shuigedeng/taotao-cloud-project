package com.taotao.cloud.data.sync.batch.multi;

import org.springframework.batch.item.ItemReader;

/**
 * @author : dylanz
 * @since : 08/26/2020
 */
public class MultiReaderService2 implements ItemReader<String> {

	private int count = 0;

	@Override
	public String read() throws Exception {
		if (MultiProcessorService1.message != null
			&& count < MultiProcessorService1.message.length) {
			return MultiProcessorService1.message[count++];
		}
		count = 0;
		return null;
	}
}
