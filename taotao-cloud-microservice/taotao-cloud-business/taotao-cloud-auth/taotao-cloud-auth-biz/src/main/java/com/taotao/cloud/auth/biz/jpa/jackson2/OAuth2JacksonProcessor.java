
package com.taotao.cloud.auth.biz.jpa.jackson2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 相关 Jackson 处理器 </p>
 *
 * 
 * @date : 2023/4/29 16:05
 */
public class OAuth2JacksonProcessor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2JacksonProcessor.class);

    private final ObjectMapper objectMapper;

    public OAuth2JacksonProcessor() {

        objectMapper = new ObjectMapper();

        ClassLoader classLoader = OAuth2JacksonProcessor.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);

		Module module = loadAndGetInstance("com.taotao.cloud.auth.biz.jpa.jackson2.FormOAuth2PhoneLoginJackson2Module", classLoader);
		securityModules.add(module);

        objectMapper.registerModules(securityModules);
        objectMapper.registerModules(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModules(new HerodotusJackson2Module());
        objectMapper.registerModules(new OAuth2TokenJackson2Module());
    }

	private static Module loadAndGetInstance(String className, ClassLoader loader) {
		try {
			Class<? extends Module> securityModule = (Class<? extends Module>) ClassUtils.forName(className, loader);
			if (securityModule != null) {
				return securityModule.newInstance();
			}
		}
		catch (Exception ex) {
		}
		return null;
	}

    public Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing parseMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing writeMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
