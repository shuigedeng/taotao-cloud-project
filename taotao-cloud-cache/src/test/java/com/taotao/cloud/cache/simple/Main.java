package com.taotao.cloud.cache.simple;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 注册自定义模块
//        SimpleModule module = new SimpleModule();
//        module.setDeserializerModifier(new CustomBeanDeserializerModifier());
        objectMapper.registerModule(new SimpleModule(){{
			setDeserializerModifier(new BeanDeserializerModifier() {
				@Override
				public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
					JsonDeserializer<?> deserializer) {

					return super.modifyDeserializer(config, beanDesc, deserializer);
				}
			});
		}});

        // 测试JSON
        String json = "{" +
                "\"name\":\"John Doe\"," +
                "\"email\":\"john.doe@example.com\"," +
                "\"age\":30," +
                "\"address\":{" +
                "  \"street\":\"123 Main St\"," +
                "  \"city\":\"new york\"," +
                "  \"zipCode\":\"10001\"" +
                "}" +
                "}";

        // 反序列化
        User user = objectMapper.readValue(json, User.class);
        System.out.println(user);
        // 正确输出：
        // User{name='John Doe', email='JOHN.DOE@EXAMPLE.COM', age=30, 
        // address=Address{street='123 Main St', city='NEW YORK', zipCode='10001'}}
    }
}
