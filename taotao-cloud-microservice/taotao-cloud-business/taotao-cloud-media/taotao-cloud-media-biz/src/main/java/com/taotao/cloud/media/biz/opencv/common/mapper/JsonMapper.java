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

package com.taotao.cloud.media.biz.opencv.common.mapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser.Feature;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.SerializerProvider;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.util.JSONPObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.StringEscapeUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper. 封装不同的输出风格, 使用不同的builder函数创建实例. 创建者 张志朋 创建时间
 * 2017年9月28日
 */
public class JsonMapper extends JsonMapper {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private static JsonMapper mapper;

    public JsonMapper() {
        this(Include.NON_NULL);
    }

    public JsonMapper(Include include) {
        // 设置输出时包含属性的风格
        if (include != null) {
            this.setSerializationInclusion(include);
        }
        // 允许单引号、允许不带引号的字段名称
        this.enableSimple();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JacksonException {
                jgen.writeString("");
            }
        });
        // 统一默认Date类型转换格式。如果设置，Bean中的@JsonFormat将无效
        final String dataFormat = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        if (StringUtils.isNotBlank(dataFormat)) {
            this.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
                @Override
                public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
                        throws IOException, JacksonException {
                    if (value != null) {
                        jgen.writeString(sdf.format(value));
                    }
                }
            }));
        }
        // 进行HTML解码。
        this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JacksonException {
                if (value != null) {
                    jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
                }
            }
        }));
        // 设置时区
        this.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    /** 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用. */
    public static JsonMapper getInstance() {
        if (mapper == null) {
            mapper = new JsonMapper().enableSimple();
        }
        return mapper;
    }

    /** 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。 */
    public static JsonMapper nonDefaultMapper() {
        if (mapper == null) {
            mapper = new JsonMapper(Include.NON_DEFAULT);
        }
        return mapper;
    }

    /** Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]". */
    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     *
     * <p>如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * <p>如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 構造泛型的Collection Type如: ArrayList<MyBean>,
     * 则调用constructCollectionType(ArrayList.class,MyBean.class) HashMap<String,MyBean>,
     * 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /** 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性. */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        } catch (JacksonException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /** 輸出JSONP格式數據. */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後,
     * 所有的讀寫動作之前調用.
     */
    public JsonMapper enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    public JsonMapper enableJaxbAnnotation() {
        // JaxbAnnotationModule module = new JaxbAnnotationModule();
        // this.registerModule(module);
        return this;
    }

    /** 允许单引号 允许不带引号的字段名称 */
    public JsonMapper enableSimple() {
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    /** 取出Mapper做进一步的设置或使用其他序列化API. */
    public JsonMapper getMapper() {
        return this;
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

    /**
     * 对象转换为JSONP字符串
     *
     * @param object
     * @return
     */
    public static String toJsonPString(String functionName, Object object) {
        return JsonMapper.getInstance().toJsonP(functionName, object);
    }

    /**
     * JSON字符串转换为对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJsonString(String jsonString, Class<?> clazz) {
        return (T) JsonMapper.getInstance().fromJson(jsonString, clazz);
    }

    /** 测试 */
    // public static void main(String[] args) {
    //	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    //	Map<String, Object> map = new HashMap<String, Object>();
    //	map.put("id", 1);
    //	map.put("pId", -1);
    //	map.put("name", "根节点");
    //	list.add(map);
    //	map = new HashMap<String, Object>();
    //	map.put("id", 2);
    //	map.put("pId", 1);
    //	map.put("name", "你好");
    //	map.put("open", true);
    //	list.add(map);
    //	String json = JsonMapper.toJsonString(list);
    //	LogUtils.info(json);
    //	Map<String, Object> map2 = JsonMapper
    //			.fromJsonString(
    //					"{extendS1:{title:'站牌号',sort:1,type:'text',maxlength:0,maxlength:30}, "
    //							+ "extendS2:{title:'规模分类',sort:2,type:'dict',dictType:'scope_category'}}",
    //					Map.class);
    //	LogUtils.info(map2);
    //
    //	String aaString = "返回值";
    //	LogUtils.info(JsonMapper.toJsonString(aaString));
    // }

}
