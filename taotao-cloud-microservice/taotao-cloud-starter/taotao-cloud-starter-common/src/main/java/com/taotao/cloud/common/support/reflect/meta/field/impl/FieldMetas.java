package com.taotao.cloud.common.support.reflect.meta.field.impl;


import com.taotao.cloud.common.constant.MethodConst;
import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.support.reflect.meta.field.IFieldMeta;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.reflect.ClassUtil;
import com.taotao.cloud.common.utils.reflect.ReflectFieldUtil;
import com.taotao.cloud.common.utils.reflect.ReflectMethodUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 字段元数据工具类
 */
public final class FieldMetas {

    private FieldMetas() {
    }

    /**
     * 构建字段元数据列表
     *
     * @param clazz    类信息
     * @param instance 实例对象
     * @return 结果列表
     */
    public static List<IFieldMeta> buildFieldsMetaList(final Class clazz,
                                                       final Object instance) {
        ArgUtil.notNull(clazz, "clazz");

        final List<Field> fieldList = ClassUtil.getModifyableFieldList(clazz);
        return buildFieldsMetaList(fieldList, instance);
    }

    /**
     * 构建字段元数据列表
     *
     * @param clazz 类信息
     * @return 结果列表
     */
    public static List<IFieldMeta> buildFieldsMetaList(final Class clazz) {
        return buildFieldsMetaList(clazz, null);
    }

    /**
     * 构建字段元数据列表
     *
     * @param fieldList 字段列表
     * @param instance  实例信息
     * @return 结果列表
     */
    private static List<IFieldMeta> buildFieldsMetaList(final List<Field> fieldList,
                                                        final Object instance) {
        return CollectionUtil.toList(fieldList, new IHandler<Field, IFieldMeta>() {
            @Override
            public IFieldMeta handle(Field field) {
                IFieldMeta fieldMeta = new FieldMeta();
                fieldMeta.setName(field.getName());
                fieldMeta.setType(field.getType());
                fieldMeta.setField(field);
                fieldMeta.setComponentType(ReflectFieldUtil.getComponentType(field));

                // 设置字段的值
                if (ObjectUtil.isNotNull(instance)) {
                    fieldMeta.setValue(ReflectFieldUtil.getValue(field, instance));
                }
                return fieldMeta;
            }
        });
    }

    /**
     * 构建读取方法元数据列表
     *
     * @param clazz 类信息
     * @return 结果列表
     */
    public static List<IFieldMeta> buildReadMethodsMetaList(final Class clazz) {
        return buildReadMethodsMetaList(clazz, null);
    }

    /**
     * 构建读取方法元数据列表
     * <p>
     * （1）只处理 get 开头的方法
     * （2）只处理无参方法
     * （3）排除 getClass 方法
     * （4）如果是 boolean 类型，则读取 isXXX 方法
     *
     * @param clazz    类信息
     * @param instance 实例对象
     * @return 结果列表
     */
    public static List<IFieldMeta> buildReadMethodsMetaList(final Class clazz,
                                                            final Object instance) {
        ArgUtil.notNull(clazz, "clazz");

        List<Method> methodList = ClassUtil.getMethodList(clazz);
        return buildMethodsMetaList(methodList, instance, new ICondition<Method>() {
            @Override
            public boolean condition(Method method) {
                final String methodName = method.getName();
                Class returnType = method.getReturnType();

                // read 参数必须为空，避免如 isProxyClass 这种方法
                final Class[] paramTypes = method.getParameterTypes();
                if(ArrayUtil.isNotEmpty(paramTypes)) {
                    return false;
                }

                if (boolean.class == returnType) {
                    return methodName.startsWith(MethodConst.IS_PREFIX);
                }

                return methodName.startsWith(MethodConst.GET_PREFIX)
                        && !methodName.equals(MethodConst.GET_CLASS_METHOD);
            }
        });
    }

    /**
     * 构建写入方法元数据列表
     * <p>
     * （1）只处理 set 开头的方法
     * （2）只处理无参方法
     *
     * @param clazz 类信息
     * @return 结果列表
     */
    public static List<IFieldMeta> buildWriteMethodsMetaList(final Class clazz) {
        return buildWriteMethodsMetaList(clazz, null);
    }

    /**
     * 构建写入方法元数据列表
     * <p>
     * （1）只处理 set 开头的方法
     * （2）只处理当个参数方法
     *
     * @param clazz    类信息
     * @param instance 实例对象
     * @return 结果列表
     */
    public static List<IFieldMeta> buildWriteMethodsMetaList(final Class clazz,
                                                             final Object instance) {
        ArgUtil.notNull(clazz, "clazz");

        List<Method> methodList = ClassUtil.getMethodList(clazz);
        return buildMethodsMetaList(methodList, instance, new ICondition<Method>() {
            @Override
            public boolean condition(Method method) {
                final Class[] paramTypes = method.getParameterTypes();
                final String methodName = method.getName();
                return methodName.startsWith(MethodConst.SET_PREFIX)
                        && paramTypes.length == 1;
            }
        });
    }

    /**
     * 构建字段元数据列表
     * （1）只处理 get 开头的方法
     * （2）只处理无参方法
     * （3）排除 getClass 方法
     * （4）如果是 boolean 类型，则读取 isXXX 方法
     *
     * @param methodList      方法列表
     * @param instance        实例信息
     * @param methodCondition 方法满足的条件
     * @return 结果列表
     */
    private static List<IFieldMeta> buildMethodsMetaList(final List<Method> methodList,
                                                         final Object instance,
                                                         final ICondition<Method> methodCondition) {
        //1. 方法级别的过滤，只获取以 get 开头的方法
        List<Method> getMethodList = CollectionUtil.conditionList(methodList, methodCondition);

        return CollectionUtil.toList(getMethodList, new IHandler<Method, IFieldMeta>() {
            @Override
            public IFieldMeta handle(Method method) {
                final String methodName = method.getName();
                // 获取字段名称
                String fieldName;
                if (methodName.startsWith(MethodConst.IS_PREFIX)) {
                    fieldName = StringUtil.firstToLowerCase(methodName.substring(2));
                } else {
                    fieldName = StringUtil.firstToLowerCase(methodName.substring(3));
                }
                FieldMeta fieldMeta = new FieldMeta();
                fieldMeta.setName(fieldName);

                // 如果是读取
                if (methodName.startsWith(MethodConst.IS_PREFIX)
                        || methodName.startsWith(MethodConst.GET_PREFIX)) {
                    fieldMeta.setType(method.getReturnType());
                    fieldMeta.setComponentType(ReflectMethodUtil.getGenericReturnParamType(method, 0));
                } else {
                    // 直接取第一个参数信息
                    fieldMeta.setType(method.getParameterTypes()[0]);
                    // 基于参数的第一个类型
                    Class componentType = ReflectMethodUtil.getParamGenericType(method, 0, 0);
                    fieldMeta.setComponentType(componentType);
                }

                if (ObjectUtil.isNotNull(instance)) {
                    final Object value = ReflectMethodUtil.invoke(instance, method, new Object[]{});
                    fieldMeta.setValue(value);
                }
                return fieldMeta;
            }
        });
    }

}
