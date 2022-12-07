package com.taotao.cloud.data.sync.batch.multi;

import org.springframework.batch.item.ItemReader;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class MultiReaderService1 implements ItemReader<String> {

	//在此处进行数据读取操作，如从数据库查询、从文件中读取、从变量中读取等，本例从变量中读取；
	private String[] message = {"message 1", "message 2", "message 3", "message 4", "message 5"};
	private int count = 0;

	@Override
	public String read() throws Exception {
		if (count < message.length) {
			return message[count++];
		}
		count = 0;
		return null;
	}
}
