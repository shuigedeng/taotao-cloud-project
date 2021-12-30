package com.taotao.cloud.auth.biz.actuator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class AppInfoContributor implements InfoContributor {

	private static final Logger LOGGER = LogManager.getLogger(AppInfoContributor.class);

	private final Map<String, Object> appInfoMap = new LinkedHashMap<>();
	
	public AppInfoContributor(@Value("${release:1}") String release, @Value("${build:7}")String build) {
		LOGGER.debug("in AppInfoContributor release=" + release + " build=" + build);
		appInfoMap.put("release", release);
		appInfoMap.put("build", build);
	}

	// http://127.0.0.1:9000/springauthserver/actuator/info
	@Override
	public void contribute(Builder builder) {
		LOGGER.debug("in contribute");
		builder.withDetails(appInfoMap);
	}

}
