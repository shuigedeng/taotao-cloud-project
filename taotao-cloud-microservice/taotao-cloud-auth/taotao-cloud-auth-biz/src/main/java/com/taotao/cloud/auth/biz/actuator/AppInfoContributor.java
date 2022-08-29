package com.taotao.cloud.auth.biz.actuator;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.HashMap;
import java.util.Map;
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
	public static final Map<String, Object> appInfoMap = new HashMap<>();

	public AppInfoContributor(@Value("${release:1}") String release,
		@Value("${build:7}") String build) {
		LogUtils.debug("in AppInfoContributor release=" + release + " build=" + build);
		appInfoMap.put("release", release);
		appInfoMap.put("build", build);
	}

	// http://127.0.0.1:9000/springauthserver/actuator/info
	@Override
	public void contribute(Builder builder) {
		LogUtils.debug("in contribute");
		builder.withDetails(appInfoMap);
	}

}
