package com.taotao.cloud.office.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * ExcelAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 09:41:50
 */
@AutoConfiguration
public class ExcelAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(ExcelAutoConfiguration.class, StarterName.EXCEL_STARTER);
	}

}
