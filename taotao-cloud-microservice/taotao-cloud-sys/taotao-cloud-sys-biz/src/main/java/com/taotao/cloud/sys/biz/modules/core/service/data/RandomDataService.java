package com.taotao.cloud.sys.biz.modules.core.service.data;

import com.sanri.tools.modules.core.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 随机数据生成
 */
@Slf4j
@Component
public class RandomDataService {
    /**
     * 给一个类型注入数据，在指定的类加载器中去查找类
     * @param className
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public Object randomData(String className,ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> clazz = classLoader.loadClass(className);
        return populateDataStart(clazz);
    }

    public Object populateDataStart(Class<?> clazz ){
        typeMapping.get().clear();
        return populateData(clazz);
    }

    /**
     * 给一个类型注入数据
     * @param clazz
     * @return
     */
    protected Object populateData(Class<?> clazz){
        if(isPrimitiveExtend(clazz)){
            return populateDataOrigin(null,clazz);
        }
        return populateDataComplex(clazz);
    }

    private final ThreadLocal<Map<String, Class<?>>> typeMapping = ThreadLocal.withInitial(HashMap::new);

    /**
     * 这个方法可以注入简单类型和复杂类型,注入单个类型
     * @param type
     * @param columnName
     * @return
     */
    private Object populateData(Type type,String columnName){
        if(type instanceof Class){
            Class propertyType = (Class) type;
            if (isPrimitiveExtend(propertyType)){
                return populateDataOrigin(columnName,propertyType);
            }
            return populateDataComplex(propertyType);
        }
        // 接下来这些都是 ParameterizedType
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // rawType 认为一定是 class ,其实还是有风险的,先这样吧
            Class propertyType = (Class) parameterizedType.getRawType();
            if(propertyType == List.class || propertyType == Set.class){
                return populateCollectionData(parameterizedType, columnName);
            }
            if(propertyType == Map.class){
                return populateMapData(parameterizedType, columnName);
            }

            final TypeVariable[] typeParameters = propertyType.getTypeParameters();
            for (int i = 0; i < parameterizedType.getActualTypeArguments().length; i++) {
                final Type actualTypeArgument = parameterizedType.getActualTypeArguments()[i];
                final TypeVariable typeParameter = typeParameters[i];
                typeMapping.get().put(typeParameter.getName(), (Class<?>) actualTypeArgument);
            }
            // 注入 Bean
            return populateData(propertyType);
        }

        if (type instanceof TypeVariable){
            final Map<String, Class<?>> classMap = typeMapping.get();
            final Class<?> clazz = classMap.get(type.getTypeName());
            if (clazz != null){
                return populateData(clazz);
            }
        }

        if (type instanceof GenericArrayType){
            GenericArrayType genericArrayType = (GenericArrayType) type;
            final Type genericComponentType = genericArrayType.getGenericComponentType();
            final Map<String, Class<?>> classMap = typeMapping.get();
            final Class<?> clazz = classMap.get(genericComponentType.getTypeName());
            if (clazz != null){
                final int arraySize = RandomUtils.nextInt(2, 10);
                final Object array = Array.newInstance(clazz, arraySize);
                for (int i = 0; i < arraySize; i++) {
                    Array.set(array,i,populateData(clazz));
                }
                return array;
            }
        }

        log.error("不支持的类型:[{}]",type);
        return null;
    }

    /**
     * 注入 map 数据
     * @param writeMethod
     * @param columnName
     * @return
     */
    private Map populateMapData(ParameterizedType parameterType, String columnName){
        Map map = new HashMap();

        Class keyTypeArgument = (Class) parameterType.getActualTypeArguments()[0];
        Class valueTypeArgument = (Class) parameterType.getActualTypeArguments()[1];

        // 随机创建 2~ 10 个键值对
        int count = RandomUtils.nextInt(2, 10);

        for (int i = 0; i < count; i++) {
            Object key = populateData(keyTypeArgument, columnName);
            Object value = populateData(valueTypeArgument, columnName);
            map.put(key,value);
        }
        return map;
    }

    /**
     * 注入复杂对象值,只能注入复杂类型 ; 这个是真正的主入口,注入一个复杂类型对象数据
     * @param clazz
     * @return
     */
    private Object populateDataComplex(Class<?> clazz) {
        Object object = ReflectUtils.newInstance(clazz);

        PropertyDescriptor[] beanSetters = ReflectUtils.getBeanSetters(clazz);
        for (PropertyDescriptor beanSetter : beanSetters) {
            Method writeMethod = beanSetter.getWriteMethod();
            String columnName = beanSetter.getName();
            Type genericParameterType = writeMethod.getGenericParameterTypes()[0];
            Object populateData = populateData(genericParameterType, columnName);

            ReflectionUtils.invokeMethod(writeMethod, object, populateData);
        }
        return object;
    }

    /**
     * 对于 set , list  等 注入数据
     * @param writeMethod
     * @param list
     * @param columnName
     * @return
     */
    private Collection populateCollectionData(ParameterizedType genericParameterType, String columnName) {
        Collection collection = null;
        Class rawType = (Class) genericParameterType.getRawType();
        if(rawType == List.class){
            collection = new ArrayList();
        }else if(rawType == Set.class){
            collection = new HashSet();
        }

        Class typeArgument = (Class) genericParameterType.getActualTypeArguments()[0];
        // 每个 List 创建 随机 2 ~ 10 条数据
        int count = RandomUtils.nextInt(2, 10);
        for (int i = 0; i < count; i++) {
            Object populateData = populateData(typeArgument, columnName);
            collection.add(populateData);
        }

        return collection;
    }

    /**
     * 对某一列注入原始类型的数据
     * @param columnName
     * @param propertyType
     * @return
     */
    protected Object populateDataOrigin( String columnName, Class<?> propertyType) {
        columnName = Objects.toString(columnName,"");

        if(propertyType == String.class){
            int randomLength = RandomUtils.nextInt(5,20);
            String value = RandomStringUtils.randomAlphabetic(randomLength);
            String lowerCase = columnName.toLowerCase();
            if(lowerCase.contains("ip")){
                value = "114.114.114.114";
            }else
            if(lowerCase.contains("idcard")){
                value = RandomUtil.idcard();
            }else
            if(lowerCase.contains("mail")){
                value = RandomUtil.email(30);
            }else
            if(lowerCase.contains("phone") ){
                value = RandomUtil.phone();
            }else
            if(lowerCase.contains("name") || lowerCase.contains("user")){
                value = RandomUtil.username();
            }else
            if(lowerCase.contains("address")){
                value = RandomUtil.address();
            }else
            if(lowerCase.contains("uuid")){
                value = UUID.randomUUID().toString().replace("-","");
            }else
                if(lowerCase.contains("id")){
                    value = RandomUtils.nextInt(10,10000) + "";
                }else
            if(lowerCase.contains("job")){
                value = RandomUtil.job();
            }else
            if(lowerCase.contains("status") || lowerCase.contains("state") || lowerCase.contains("type")){
                value = RandomUtil.status("1","2","3");
            }else
            if(lowerCase.contains("time") || (lowerCase.contains("date") && !"update".equals(lowerCase)) || lowerCase.contains("birthday")){
                // 这里可以做时间格式转换,获取字段上的配置
                value =  DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(RandomUtil.date());
            } else
            if(lowerCase.contains("lat")){
                String randomLongLat = RandomUtil.randomLongLat(25, 115, 26, 160);
                value = StringUtils.split(randomLongLat,",")[1];
            }else
            if(lowerCase.contains("lon") || lowerCase.contains("lang")){
                String randomLongLat = RandomUtil.randomLongLat(25, 115, 26, 160);
                value = StringUtils.split(randomLongLat,",")[0];
            }else if(lowerCase.contains("pic") || lowerCase.contains("photo")){
                value = RandomUtil.photoURL();
            }else if(lowerCase.contains("num")){
                value = RandomUtils.nextInt(10,1000) + "";
            }else if(lowerCase.contains("url")){
                value = RandomUtil.url();
            }
            else if(lowerCase.contains("no") || lowerCase.contains("code")){
                value = RandomStringUtils.randomNumeric(4);
            }

            return value;
        }
        if(propertyType == Date.class){
            return RandomUtil.date();
        }
        if(propertyType == BigDecimal.class){
            BigDecimal bigDecimal = new BigDecimal(RandomUtils.nextDouble(100,2000000));
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
            return bigDecimal;
        }
        if (propertyType == Integer.class || propertyType == int.class){
            Integer num = RandomUtils.nextInt(10,1000);
            if(columnName.toLowerCase().contains("age")) {
                num = RandomUtils.nextInt(20,150);
            }
            return num;
        }
        if(propertyType == Long.class ||  propertyType == long.class){
            return RandomUtils.nextLong(10,10000000);
        }
        if(propertyType == Float.class || propertyType == float.class){
            return RandomUtils.nextFloat(10,1000);
        }
        if(propertyType == Double.class || propertyType == double.class){
            return RandomUtils.nextDouble(10,1000);
        }
        if(propertyType == Boolean.class || propertyType == boolean.class){
//            return RandomUtils.nextInt(1,1000) & 2 ;
            return RandomUtils.nextBoolean();
        }
        if(propertyType == Short.class || propertyType == short.class){
            int nexInt = RandomUtils.nextInt(129, 500);
            return new Integer(nexInt).shortValue();
        }
        if(propertyType == Character.class || propertyType == char.class){
            return RandomStringUtils.randomAlphanumeric(1).charAt(0);
        }
        if(propertyType == Byte.class || propertyType == byte.class){
            return RandomUtils.nextBytes(1)[0];
        }

        // 数组只能支持一维数组,不要使用二维数组
        if(propertyType.isArray()){
            Class<?> componentType = propertyType.getComponentType();
            int count = RandomUtils.nextInt(2, 10);
            Object arr = Array.newInstance(componentType, count);

            for (int i = 0; i < count; i++) {
                Object value = populateData(componentType);
                Array.set(arr,i,value);
            }
            return arr;
        }

        log.error("无法处理的类型[{}]",propertyType);
        return null;
    }


    /**
     * 判断是否是原始型扩展
     * 包含 原始型及包装类,String,Date,BigDecimal,Array 数组也当原始型处理,因为数组类型必须要匹配,不能自动拆箱装箱
     * @param propertyType
     * @return
     */
    private boolean isPrimitiveExtend(Class<?> propertyType) {
        return ClassUtils.isPrimitiveOrWrapper(propertyType) || propertyType == String.class || propertyType == Date.class || propertyType == BigDecimal.class || propertyType.isArray();
    }
}
