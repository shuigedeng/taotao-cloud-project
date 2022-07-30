package com.taotao.cloud.common.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import com.taotao.cloud.common.utils.reflect.ClassUtil;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.util.ClassUtils;

/**
 * bean对象序列化规则
 *
 * @author dry
 * @since 2022-04-30 16:21:28
 */
public class MyBeanSerializerModifier extends BeanSerializerModifier {
 
    /**
     * 更改每个bean的序列化属性
     *
     * @param config
     * @param beanDesc
     * @param beanProperties
     * @return
     */
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        //循环所有的beanProperties
        for (BeanPropertyWriter writer : beanProperties) {
            //给writer注册一个自己的nullSerializer
            if (isArrayType(writer)) {
                //这里使用单例的原因是每个被序列化的bean都要执行一次这个方法,且这个类里面的方法是线程安全的,如果每次都new,想想就挺可怕的
                writer.assignNullSerializer(NullArrayJsonSerializer.INSTANCE);
            } else if (isMapType(writer)) {
                writer.assignNullSerializer(NullMapJsonSerializer.INSTANCE);
            } else if (isBooleanType(writer)) {
	            writer.assignNullSerializer(NullPrimitiveWrapperBooleanJsonSerializer.INSTANCE);
            } else if (isDoubleType(writer) || isFloatType(writer) || isDoubleType(writer) || isIntegerType(writer) || isLongType(writer) || isShortType(writer) ) {
	            writer.assignNullSerializer(NullPrimitiveWrapperNumberJsonSerializer.INSTANCE);
            }

			//else {
			//	//除了list和map外其它的都赋值为"",要是还想加别的类型,可以再这里再写else if
            //    writer.assignNullSerializer(NullObjectJsonSerializer.INSTANCE);
            //}
        }

        return beanProperties;
    }
 
 
    /**
     * 是否是数组
     *
     * @param writer
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> rawClass = writer.getType().getRawClass();

        return rawClass.isArray() || Collection.class.isAssignableFrom(rawClass);
    }
 
    /**
     * 是否是map
     *
     * @param writer
     */
    private boolean isMapType(BeanPropertyWriter writer) {
        Class<?> rawClass = writer.getType().getRawClass();
        return Map.class.isAssignableFrom(rawClass);
    }

	private boolean isBooleanType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Boolean.class);
	}
	private boolean isDoubleType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Double.class);
	}
	private boolean isFloatType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Float.class);
	}
	private boolean isIntegerType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Integer.class);
	}
	private boolean isLongType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Long.class);
	}
	private boolean isShortType(BeanPropertyWriter writer) {
		Class<?> rawClass = writer.getType().getRawClass();
		return ClassUtils.isAssignable(rawClass,Short.class);
	}
}
