/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.common.config;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.*;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/** 程序注解配置 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan({"com.platform.modules.**.dao"})
// 扫描spring工具类
@ComponentScan(basePackages = {"org.dromara.hutoolextra.spring"})
public class ApplicationConfig {

    /** 时区配置 */
    @Bean
    public Jackson2JsonMapperBuilderCustomizer jacksonJsonMapperCustomization() {
        return builder -> builder.timeZone(TimeZone.getDefault());
    }

    /**
     * 序列化枚举值为数据库存储值
     *
     * @return
     */
    @Bean
    public Jackson2JsonMapperBuilderCustomizer customizer() {
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Bean
    public JsonMapper jsonMapper() {
        final JsonMapper jsonMapper = new JsonMapper();
        // 忽略未知的枚举字段
        jsonMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        // 忽略多余的字段不参与序列化
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略null属性字段
        //        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // null属性字段转""
        jsonMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
                arg1.writeString("");
            }
        });
        SimpleModule simpleModule = new SimpleModule();
        // 格式化Long
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 格式化时间
        simpleModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateUtil.format(date, DatePattern.NORM_DATETIME_FORMAT));
            }
        });
        // 注册 module
        jsonMapper.registerModule(simpleModule);
        return jsonMapper;
    }
}
