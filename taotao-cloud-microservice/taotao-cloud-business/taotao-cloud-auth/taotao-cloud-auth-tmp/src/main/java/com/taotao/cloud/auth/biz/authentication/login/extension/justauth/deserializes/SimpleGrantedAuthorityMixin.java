package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.deserializes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Jackson Mixin class helps in serialize/deserialize
 * {@link org.springframework.security.core.authority.SimpleGrantedAuthority}.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new CoreJackson2Module());
 * </pre>
 *
 * @author Jitendra Singh
 * @since 4.2
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
		getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SimpleGrantedAuthorityMixin {

	/**
	 * Mixin Constructor.
	 * @param role the role
	 */
	@JsonCreator
	public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {
	}

}
