package com.taotao.cloud.data.sync.batch.mybatis;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
public class MybatisWriteListener implements ItemWriteListener<BlogInfo> {

	private Logger logger = LoggerFactory.getLogger(MybatisWriteListener.class);

	@Override
	public void beforeWrite(Chunk<? extends BlogInfo> items) {
		ItemWriteListener.super.beforeWrite(items);
	}

	@Override
	public void afterWrite(Chunk<? extends BlogInfo> items) {
		ItemWriteListener.super.afterWrite(items);
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends BlogInfo> items) {
		try {
			logger.info(format("%s%n", exception.getMessage()));
			for (BlogInfo message : items) {
				logger.info(format("Failed writing BlogInfo : %s", message.toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		ItemWriteListener.super.onWriteError(exception, items);
	}
}
