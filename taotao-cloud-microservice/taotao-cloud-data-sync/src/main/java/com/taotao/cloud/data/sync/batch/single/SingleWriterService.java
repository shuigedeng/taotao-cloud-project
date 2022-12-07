package com.taotao.cloud.data.sync.batch.single;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class SingleWriterService implements ItemWriter<String> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//在此处进行数据输出操作，如写入数据库、写入文件、打印log等，本例为打印log；
	public void write(List<? extends String> messages) throws Exception {
		for (String message : messages) {
			logger.info("Writing data: " + message);
		}
	}

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		List<? extends String> messages = chunk.getItems();
		for (String message : messages) {
			logger.info("Writing data: " + message);
			System.out.println("Writing data:111111 " + message);
		}
	}
}
