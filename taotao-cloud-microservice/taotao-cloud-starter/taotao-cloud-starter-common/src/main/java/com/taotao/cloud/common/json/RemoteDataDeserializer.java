package com.taotao.cloud.common.json;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.taotao.cloud.common.base.StrPool;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.model.RemoteData;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

/**
 * RemoteData 反序列化工具
 * <p>
 * 字段类型是RemoteData类型时，可以按照以下2种格式反序列化：
 * 1. 字符串形式：字段名： "XX"
 * 2. 对象形式： 字段名： {
 * "key": "XX",
 * "data": "yy"
 * }
 *
 * @author zuihou
 * @date 2019-07-25 22:15
 */
@Slf4j
public class RemoteDataDeserializer extends StdDeserializer<RemoteData<?, ?>> {
    public static final RemoteDataDeserializer INSTANCE = new RemoteDataDeserializer();
    private static final String REMOTE_DATA_KEY_FIELD = "key";
    private static final String REMOTE_DATA_DATA_FIELD = "data";

    public RemoteDataDeserializer() {
        super(RemoteData.class);
    }

    @Override
    public RemoteData<?, ?> deserialize(JsonParser jp, DeserializationContext context) {
        try {
            // 读取
            JsonNode node = jp.getCodec().readTree(jp);
            // 当前字段
            String currentName = jp.currentName();
            // 当前对象
            Object currentValue = jp.getCurrentValue();
            // 在对象中找到改字段
            String keyVal = getValByNode(node, REMOTE_DATA_KEY_FIELD);
            String dataVal = getValByNode(node, REMOTE_DATA_DATA_FIELD);

            if (currentValue == null) {
                return null;
            }
            Field field = ReflectUtil.getField(currentValue.getClass(), currentName);
            if (field == null) {
                return null;
            }

            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;

                // 得到泛型里的class类型对象
                return new RemoteData<>(convert(pt.getActualTypeArguments()[0], keyVal), convert(pt.getActualTypeArguments()[1], dataVal));
            }

            return null;
        } catch (Exception e) {
            log.warn("解析RemoteData字段失败", e);
            throw new BaseException("解析RemoteData字段失败:" + e.getMessage());
        }
    }


    protected String getValByNode(JsonNode node, String fieldName) {
        JsonNode keyNode = node.get(fieldName);
        return keyNode != null ? keyNode.asText() : node.asText();
    }

    protected <T> T convert(Type type, Object val) {
        if (val == null) {
            return null;
        }
        if (StrPool.NULL.equals(val)) {
            return null;
        }
        if (StrPool.EMPTY.equals(val)) {
            if (StrPool.STRING_TYPE_NAME.equals(type.getTypeName())) {
                return (T) StrPool.EMPTY;
            }
            return null;
        }

        return Convert.convert(type, val);
    }


}
