package com.taotao.cloud.common.utils.reflect;


import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 内省工具类
 */
public final class PropertyDescriptorUtil {

    private PropertyDescriptorUtil(){}

    /**
     * 获取属性描述类
     * @param beanClass 类
     * @param propertyName 属性值
     * @return 性描述
     */
    public static PropertyDescriptor getPropertyDescriptor(final Class beanClass,
                                                           final String propertyName) {
        ArgUtil.notNull(beanClass, "beanClass");
        ArgUtil.notEmpty(propertyName, "propertyName");

        try {
            return new PropertyDescriptor(propertyName, beanClass);
        } catch (IntrospectionException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取属性读方法
     * @param beanClass 对象类型
     * @param propertyName 属性名称
     * @return 属性读方法
     */
    public static Method getReadMethod(final Class<?> beanClass,
                                       final String propertyName) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);

        return propertyDescriptor.getReadMethod();
    }

    /**
     * 获取属性写方法
     * @param beanClass 对象类型
     * @param propertyName 属性名称
     * @return 属性写方法
     */
    public static Method getWriteMethod(final Class beanClass,
                                       final String propertyName) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);

        return propertyDescriptor.getWriteMethod();
    }

    /**
     * 获取属性类型
     * @param beanClass 对象类型
     * @param propertyName 属性名称
     * @return 属性字段类型
     */
    public static Class<?> getPropertyType(final Class beanClass,
                                        final String propertyName) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);

        return propertyDescriptor.getPropertyType();
    }

    /**
     * 获取所有属性描述列表
     * @param beanClass 对象信息
     * @return 列表
     */
    public static List<PropertyDescriptor> getAllPropertyDescriptorList(final Class beanClass) {
        ArgUtil.notNull(beanClass, "beanClass");

        List<Field> fieldList = ClassUtil.getAllFieldList(beanClass);
        return CollectionUtil.toList(fieldList, field -> {
            String fieldName = field.getName();
            return getPropertyDescriptor(beanClass, fieldName);
        });
    }

    /**
     * 设置属性值
     *
     * @param bean       对象
     * @param descriptor 描述类
     * @param value      待设置的值
     * @return 是否设置成功
     */
    public static boolean setPropertyValue(Object bean,
                                           PropertyDescriptor descriptor,
                                           Object value) {
        try {
            Method setMethod = descriptor.getWriteMethod();//从属性描述器中获取 set 方法
            if (setMethod == null) {
                return false;
            }

            //调用 set 方法将传入的value值保存属性中去
            setMethod.invoke(bean, value);
            return true;
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 设置属性值
     *
     * @param bean       对象
     * @param descriptorName 描述名称
     * @param value      待设置的值
     * @return 是否设置成功
     */
    public static boolean setPropertyValue(Object bean,
                                           String descriptorName,
                                           Object value) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean.getClass(), descriptorName);
        return setPropertyValue(bean, propertyDescriptor, value);
    }

    /**
     * 获取属性值
     *
     * @param bean       对象
     * @param descriptor 描述类
     * @return 对应的值
     */
    public static Object getPropertyValue(Object bean,
                                           PropertyDescriptor descriptor) {
        try {
            Method readMethod = descriptor.getReadMethod();//从属性描述器中获取 set 方法
            if (readMethod == null) {
                return null;
            }

            return readMethod.invoke(bean);
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取属性值
     *
     * @param bean       对象
     * @param descriptorName 描述名称
     * @return 对应的配置值
     */
    public static Object getPropertyValue(Object bean,
                                           String descriptorName) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean.getClass(), descriptorName);
        return getPropertyValue(bean, propertyDescriptor);
    }

}
