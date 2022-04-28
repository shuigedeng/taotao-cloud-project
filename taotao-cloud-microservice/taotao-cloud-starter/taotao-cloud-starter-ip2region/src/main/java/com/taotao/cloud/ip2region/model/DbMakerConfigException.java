package com.taotao.cloud.ip2region.model;

import java.io.Serial;

/**
 * configuration exception
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:27:58
 */
public class DbMakerConfigException extends Exception {

	@Serial
	private static final long serialVersionUID = 4495714680349884838L;

	public DbMakerConfigException(String info) {
		super(info);
	}

	public DbMakerConfigException(Throwable res) {
		super(res);
	}

	public DbMakerConfigException(String info, Throwable res) {
		super(info, res);
	}
}
