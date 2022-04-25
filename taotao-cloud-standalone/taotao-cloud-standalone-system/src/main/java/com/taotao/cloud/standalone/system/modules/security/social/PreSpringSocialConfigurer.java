package com.taotao.cloud.standalone.system.modules.security.social;

import com.taotao.cloud.standalone.system.modules.security.handle.PreAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author shuigedeng
 * @since 2019-07-08 21:49
 * 
 */
public class PreSpringSocialConfigurer extends SpringSocialConfigurer {
	
	private String filterProcessesUrl;

	@Autowired
	private PreAuthenticationSuccessHandler preAuthenticationSuccessHandler;

	public PreSpringSocialConfigurer(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}
	
	@Override
	protected <T> T postProcess(T object) {
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		filter.setFilterProcessesUrl(filterProcessesUrl);
		filter.setSignupUrl("/socialSignUp");
		filter.setAuthenticationSuccessHandler(preAuthenticationSuccessHandler);
		return (T) filter;
	}

}
