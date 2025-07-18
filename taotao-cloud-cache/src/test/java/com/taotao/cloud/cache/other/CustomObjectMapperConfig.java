package com.taotao.cloud.cache.other;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomObjectMapperConfig {

    public static ObjectMapper createCustomObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new CustomBeanDeserializerModifier());
        
        mapper.registerModule(module);
        return mapper;
    }

		public static void main(String[] args) throws Exception {
			ObjectMapper mapper = CustomObjectMapperConfig.createCustomObjectMapper();

			String json = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
			User user = mapper.readValue(json, User.class);

			System.out.println(user.getEmail()); // 输出: JOHN@EXAMPLE.COM
		}
}
