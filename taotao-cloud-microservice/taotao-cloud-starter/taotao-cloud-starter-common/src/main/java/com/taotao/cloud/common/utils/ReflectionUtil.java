/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.exception.BaseException;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ReflectionUtils
 *
 * @author dengtao
 * @version 1.0
 * @date 2019-07-31 09:54
 **/
@UtilityClass
public class ReflectionUtil {

    /**
     * classForName
     *
     * @param type 类型
     * @return java.lang.Class<?>
     * @author dengtao
     * @date 2020/10/15 15:28
     * @since v1.0
     */
    public Class<?> classForName(String type) {
        try {
            return Class.forName(type);
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * tryClassForName
     *
     * @param type 类型
     * @return java.lang.Class<?>
     * @author dengtao
     * @date 2020/10/15 15:28
     * @since v1.0
     */
    public Class<?> tryClassForName(String type) {
        try {
            return Class.forName(type);
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * 获取类中方法
     *
     * @param cls        类
     * @param methodName 方法名
     * @return java.lang.reflect.Method
     * @author dengtao
     * @date 2020/10/15 15:28
     * @since v1.0
     */
    public Method findMethod(Class<?> cls, String methodName) {
        Method find = null;
        while (cls != null) {
            for (val methods : new Method[][]{cls.getMethods(), cls.getDeclaredMethods()}) {
                for (val method : methods) {
                    if (method.getName().equalsIgnoreCase(methodName)) {
                        find = method;
                        break;
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return find;
    }

    /**
     * 获取类中方法
     *
     * @param cls        类型
     * @param methodName 方法名
     * @param argsTypes  参数类型
     * @return java.lang.reflect.Method
     * @author dengtao
     * @date 2020/10/15 15:29
     * @since v1.0
     */
    public Method findMethod0(Class<?> cls, String methodName, Class<?>... argsTypes) throws NoSuchMethodException, SecurityException {
        Method find = null;
        if (cls != null) {
            find = cls.getMethod(methodName, argsTypes);
        }
        return find;
    }

    /**
     * 调用对象方法
     *
     * @param obj          对象
     * @param methodName   方法名
     * @param param        参数
     * @param defaultValue 默认值
     * @return T
     * @author dengtao
     * @date 2020/10/15 15:29
     * @since v1.0
     */
    public <T> T tryCallMethod(Object obj, String methodName, Object[] param, T defaultValue) {
        try {
            if (obj != null) {
                val find = findMethod(obj.getClass(), methodName);
                if (find != null) {
                    if (!find.isAccessible()) {
                        find.setAccessible(true);
                    }
                    return (T) find.invoke(obj, param);
                }
            }
            return defaultValue;
        } catch (Exception exp) {
            return defaultValue;
        }
    }

    /**
     * 调用对象方法
     *
     * @param obj        对象
     * @param methodName 方法名
     * @param param      参数
     * @return java.lang.Object
     * @author dengtao
     * @date 2020/10/15 15:30
     * @since v1.0
     */
    public Object callMethod(Object obj, String methodName, Object[] param) {
        try {
            val find = findMethod(obj.getClass(), methodName);
            if (find != null) {
                return find.invoke(obj, param);
            }
            throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * 调用对象方法
     *
     * @param clazz      类型
     * @param methodName 方法名
     * @param params     参数
     * @return java.lang.Object
     * @author dengtao
     * @date 2020/10/15 15:30
     * @since v1.0
     */
    public Object callMethod(Class<?> clazz, String methodName, Object[] params) {
        try {
            val find = findMethod(clazz, methodName);
            if (find != null) {
                return find.invoke(null, params);
            }
            throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * 调用对象方法
     *
     * @param clazz      类型
     * @param methodName 方法名
     * @param params     参数
     * @param paramTypes 参数类型
     * @return java.lang.Object
     * @author dengtao
     * @date 2020/10/15 15:30
     * @since v1.0
     */
    public Object callMethodWithParams(Class<?> clazz, String methodName, Object[] params, Class<?>... paramTypes) {
        try {
            val find = findMethod0(clazz, methodName, paramTypes);
            if (find != null) {
                return find.invoke(null, params);
            }
            throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * 调用对象方法
     *
     * @param object     对象
     * @param methodName 方法名
     * @param params     参数
     * @param paramTypes 参数类型
     * @return java.lang.Object
     * @author dengtao
     * @date 2020/10/15 15:30
     * @since v1.0
     */
    public Object callMethodWithParams(Object object, String methodName, Object[] params, Class<?>... paramTypes) {
        try {
            val find = findMethod0(object.getClass(), methodName, paramTypes);
            if (find != null) {
                return find.invoke(object, params);
            }
            throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * 获取字段
     *
     * @param cls  类型
     * @param name 字段名称
     * @return java.lang.reflect.Field
     * @author dengtao
     * @date 2020/10/15 15:31
     * @since v1.0
     */
    public Field findField(Class<?> cls, String name) {
        Field find = null;
        while (cls != null) {
            for (val fields : new Field[][]{cls.getFields(), cls.getDeclaredFields()}) {
                for (val field : fields) {
                    if (field.getName().equalsIgnoreCase(name)) {
                        find = field;
                        return find;
                        //break;
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return find;
    }

    /**
     * 获取字段值
     *
     * @param obj  对象
     * @param name 字段名称
     * @return java.lang.reflect.Field
     * @author dengtao
     * @date 2020/10/15 15:31
     * @since v1.0
     */
    public <T> T getFieldValue(Object obj, String name) {
        try {
            val find = findField(obj.getClass(), name);
            if (find != null) {
                if (!find.isAccessible()) {
                    find.setAccessible(true);
                }
                return (T) find.get(obj);
            }
            throw new Exception("未找到字段" + StringUtil.nullToEmpty(name));
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * 获取字段值
     *
     * @param obj          对象
     * @param name         字段名称
     * @param defaultValue 默认值
     * @return java.lang.reflect.Field
     * @author dengtao
     * @date 2020/10/15 15:31
     * @since v1.0
     */
    public <T> T tryGetFieldValue(Object obj, String name, T defaultValue) {
        try {
            if (obj != null) {
                val find = findField(obj.getClass(), name);
                if (find != null) {
                    if (!find.isAccessible()) {
                        find.setAccessible(true);
                    }
                    return (T) find.get(obj);
                }
            }
            return defaultValue;
        } catch (Exception exp) {
            return defaultValue;
        }
    }

    /**
     * 获取静态字段
     *
     * @param cls          对象全路径
     * @param name         字段名称
     * @param defaultValue 默认值ø
     * @return T
     * @author dengtao
     * @date 2020/10/15 15:33
     * @since v1.0
     */
    public <T> T tryGetStaticFieldValue(String cls, String name, T defaultValue) {
        try {
            return tryGetStaticFieldValue(Class.forName(cls), name, defaultValue);
        } catch (Exception exp) {
            return defaultValue;
        }
    }

    /**
     * 获取静态字段
     *
     * @param cls          类型
     * @param name         字段名称
     * @param defaultValue 默认值ø
     * @return T
     * @author dengtao
     * @date 2020/10/15 15:33
     * @since v1.0
     */
    public <T> T tryGetStaticFieldValue(Class<?> cls, String name, T defaultValue) {
        try {
            if (cls != null) {
                val find = findField(cls, name);
                if (find != null) {
                    if (!find.isAccessible()) {
                        find.setAccessible(true);
                    }
                    return (T) find.get(cls);
                }
            }
            return defaultValue;
        } catch (Exception exp) {
            return defaultValue;
        }
    }

    /**
     * 设置字段
     *
     * @param field 字段
     * @param obj   对象
     * @param value 值
     * @return void
     * @author dengtao
     * @date 2020/10/15 15:34
     * @since v1.0
     */
    public void setFieldValue(Field field, Object obj, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(obj, value);
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * 获取值
     *
     * @param obj  对象
     * @param path 路径
     * @param deft deft
     * @return T
     * @author dengtao
     * @date 2020/10/15 15:34
     * @since v1.0
     */
    public <T> T tryGetValue(Object obj, String path, T deft) {
        if (obj == null || path == null || path.length() == 0) {
            return deft;
        }
        Object object = obj;
        for (String name : path.split("\\.")) {
            if (object == null) {
                break;
            }
            Object value = tryGetFieldValue(object, name, null);
            if (value == null) {
                object = tryCallMethod(object, name, null, null);
            } else {
                object = value;
            }
        }
        return object == null ? deft : (T) object;
    }

    /**
     * 获取值
     *
     * @param obj  对象
     * @param path 路径
     * @return T
     * @author dengtao
     * @date 2020/10/15 15:34
     * @since v1.0
     */
    public <T> T tryGetValue(Object obj, String path) {
        return tryGetValue(obj, path, null);
    }
}
