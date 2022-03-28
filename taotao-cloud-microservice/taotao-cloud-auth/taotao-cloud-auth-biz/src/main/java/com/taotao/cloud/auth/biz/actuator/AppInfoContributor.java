package com.taotao.cloud.auth.biz.actuator;

import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * AppInfoContributor
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-28 11:03:03
 */
@Component
public class AppInfoContributor implements InfoContributor {

	public AppInfoContributor(@Value("${release:1}") String release, @Value("${build:7}")String build) {
		LOGGER.debug("in AppInfoContributor release=" + release + " build=" + build);
		appInfoMap.put("release", release);
		appInfoMap.put("build", build);
	}

	// http://127.0.0.1:9000/springauthserver/actuator/info
	@Override
	public void contribute(Builder builder) {
		LogUtil.debug("in contribute");
		builder.withDetails(appInfoMap);
	}

}
