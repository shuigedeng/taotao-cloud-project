package com.taotao.cloud.data.sync.batch.mybatis;

import static com.taotao.cloud.common.utils.lang.StringUtils.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/

public class MybatisReadListener implements ItemReadListener<BlogInfo> {

	private Logger logger = LoggerFactory.getLogger(MybatisReadListener.class);


	@Override
	public void beforeRead() {
	}

	@Override
	public void afterRead(BlogInfo item) {
	}

	@Override
	public void onReadError(Exception ex) {
		try {
			logger.info(format("%s%n", ex.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
