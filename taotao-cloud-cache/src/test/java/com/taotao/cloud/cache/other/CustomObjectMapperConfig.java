package com.taotao.cloud.cache.other;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

public class CustomJsonMapperConfig {

    public static JsonMapper createCustomJsonMapper() {
        JsonMapper mapper = JsonMapper.builder().build();
        
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new CustomBeanDeserializerModifier());
        
        mapper.registerModule(module);
        return mapper;
    }

		public static void main(String[] args) throws Exception {
			JsonMapper mapper = CustomJsonMapperConfig.createCustomJsonMapper();

			String json = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
			User user = mapper.readValue(json, User.class);

			System.out.println(user.getEmail()); // 输出: JOHN@EXAMPLE.COM
		}
}
