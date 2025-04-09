
package com.taotao.cloud.ccsr.core.serializer;

import com.taotao.cloud.ccsr.spi.SPI;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serialization interface.
 */
@SPI
public interface Serializer {

    Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>(8);

    /**
     * Deserialize the data.
     *
     * @param data byte[]
     * @param <T>  class type
     * @return target object instance
     */
    <T> T deserialize(byte[] data);

    /**
     * Deserialize the data.
     *
     * @param data byte[]
     * @param cls  class
     * @param <T>  class type
     * @return target object instance
     */
    <T> T deserialize(byte[] data, Class<T> cls);

    /**
     * Deserialize the data.
     *
     * @param data byte[]
     * @param type data type
     * @param <T>  class type
     * @return target object instance
     */
    <T> T deserialize(byte[] data, Type type);

    /**
     * Deserialize the data.
     *
     * @param data          byte[]
     * @param classFullName class full name
     * @param <T>           class type
     * @return target object instance
     */
    @SuppressWarnings("unchecked")
    default <T> T deserialize(byte[] data, String classFullName) {
        try {
            Class<?> cls = CLASS_CACHE.computeIfAbsent(classFullName, name -> {
                try {
                    return Class.forName(classFullName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            return (T) deserialize(data, cls);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * Serialize the object.
     *
     * @param obj target obj
     * @return byte[]
     */
    <T> byte[] serialize(T obj);

    /**
     * The name of the serializer implementer.
     *
     * @return name
     */
    String name();

}
