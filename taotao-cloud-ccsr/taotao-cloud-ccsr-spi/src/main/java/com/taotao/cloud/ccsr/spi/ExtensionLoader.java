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

package com.taotao.cloud.ccsr.spi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * The type Extension loader.
 * This is done by loading the properties file.
 *
 * @param <T> the type parameter
 * @see <a href="https://github.com/apache/dubbo/blob/master/dubbo-common/src/main/java/org/apache/dubbo/common/extension/ExtensionLoader.java">ExtensionLoader</a>
 */
public final class ExtensionLoader<T> {

    private static final String CCSR_DIRECTORY = "META-INF/ccsr/";

    private static final Map<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    private static final Comparator<Holder<Object>> HOLDER_COMPARATOR =
            Comparator.comparing(Holder::getOrder);

    private static final Comparator<ClassEntity> CLASS_ENTITY_COMPARATOR =
            Comparator.comparing(ClassEntity::getOrder);

    private final Class<T> clazz;

    private final ClassLoader classLoader;

    private final Holder<Map<String, ClassEntity>> cachedClasses = new Holder<>();

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> joinInstances = new ConcurrentHashMap<>();

    private String cachedDefaultName;

    /**
     * Instantiates a new Extension loader.
     *
     * @param clazz the clazz.
     */
    private ExtensionLoader(final Class<T> clazz, final ClassLoader cl) {
        this.clazz = clazz;
        this.classLoader = cl;
    }

    public static Map<Class<?>, ExtensionLoader<?>> getLoaders() {
        return LOADERS;
    }

    /**
     * Gets extension loader.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param cl    the cl
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(
            final Class<T> clazz, final ClassLoader cl) {
        Objects.requireNonNull(clazz, "extension clazz is null");
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") is not interface!");
        }
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException(
                    "extension clazz ("
                            + clazz
                            + ") without @"
                            + SPI.class.getSimpleName()
                            + " Annotation");
        }

        // Double-check locking
        @SuppressWarnings("unchecked")
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
        if (Objects.isNull(extensionLoader)) {
            synchronized (LOADERS) {
                extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
                if (Objects.isNull(extensionLoader)) {
                    extensionLoader = new ExtensionLoader<>(clazz, cl);
                    LOADERS.put(clazz, extensionLoader);
                }
            }
        }
        return extensionLoader;
    }

    /**
     * Gets extension loader.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        return getExtensionLoader(clazz, ExtensionLoader.class.getClassLoader());
    }

    /**
     * Gets default join.
     *
     * @return the default join.
     */
    public T getDefaultJoin() {
        getExtensionClassesEntity();
        if (StringUtils.isBlank(cachedDefaultName)) {
            return null;
        }
        return getJoin(cachedDefaultName);
    }

    @SuppressWarnings("unchecked")
    public T getJoin(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("get join name is null");
        }

        ClassEntity classEntity = getExtensionClassesEntity().get(name);
        if (Objects.isNull(classEntity)) {
            throw new IllegalArgumentException(name + " name is error");
        }
        if (!classEntity.isSingleton()) {
            return (T) createExtension(classEntity);
        }

        Holder<Object> objectHolder = cachedInstances.get(name);
        if (Objects.isNull(objectHolder)) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            objectHolder = cachedInstances.get(name);
        }

        Object value = objectHolder.getValue();
        if (Objects.isNull(value)) {
            synchronized (objectHolder) {
                // 加锁后再次判断值持有器中的值是否存在，不存在的时候则进行实现类实例化
                value = objectHolder.getValue();
                if (Objects.isNull(value)) {
                    createExtension(name, objectHolder);
                    value = objectHolder.getValue();
                }
            }
        }
        return (T) value;
    }

    /**
     * Get all join spi.
     *
     * @return list joins
     */
    @SuppressWarnings("unchecked")
    public List<T> getJoins() {
        Map<String, ClassEntity> extensionClassesEntity = this.getExtensionClassesEntity();
        if (extensionClassesEntity.isEmpty()) {
            return Collections.emptyList();
        }
        if (Objects.equals(extensionClassesEntity.size(), cachedInstances.size())) {
            return (List<T>)
                    this.cachedInstances.values().stream()
                            .sorted(HOLDER_COMPARATOR)
                            .map(Holder::getValue)
                            .collect(Collectors.toList());
        }
        List<T> joins = new ArrayList<>();
        List<ClassEntity> classEntities =
                extensionClassesEntity.values().stream().sorted(CLASS_ENTITY_COMPARATOR).toList();
        classEntities.forEach(
                v -> {
                    T join = this.getJoin(v.getName());
                    joins.add(join);
                });
        return joins;
    }

    private Object createExtension(final ClassEntity classEntity) {
        Class<?> aClass = classEntity.getClazz();
        if (aClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        try {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Extension class ("
                            + classEntity.getName()
                            + ") does not have a no-argument constructor",
                    e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(
                    "Extension instance(name: "
                            + classEntity.getName()
                            + ", class: "
                            + aClass
                            + ") could not be instantiated: "
                            + e.getMessage(),
                    e);
        }
    }

    private void createExtension(final String name, final Holder<Object> holder) {
        ClassEntity classEntity = getExtensionClassesEntity().get(name);
        if (Objects.isNull(classEntity)) {
            throw new IllegalArgumentException(name + " name is error");
        }
        Class<?> aClass = classEntity.getClazz();
        // 如果实现类实例缓存中已经存在，则直接封装为值包装器返回，否则进行实例化
        Object o = joinInstances.get(aClass);
        if (Objects.isNull(o)) {
            o = createExtension(classEntity);
            if (classEntity.isSingleton()) {
                joinInstances.putIfAbsent(aClass, o);
                o = joinInstances.get(aClass);
            }
        }
        holder.setOrder(classEntity.getOrder());
        holder.setValue(o);
    }

    /**
     * Gets extension classes.
     *
     * @return the extension classes
     */
    public Map<String, Class<?>> getExtensionClasses() {
        Map<String, ClassEntity> classes = this.getExtensionClassesEntity();
        return classes.values().stream()
                .collect(
                        Collectors.toMap(ClassEntity::getName, ClassEntity::getClazz, (a, b) -> a));
    }

    /**
     * 加载所有扩展类信息，这里采用了DCL（双重锁校验）防止并发加载
     */
    private Map<String, ClassEntity> getExtensionClassesEntity() {
        Map<String, ClassEntity> classes = cachedClasses.getValue();
        if (Objects.isNull(classes)) {
            synchronized (cachedClasses) {
                // 加锁后再检查一次缓存
                classes = cachedClasses.getValue();
                if (Objects.isNull(classes)) {
                    // 最终确认缓存不存在，则进行加载，并且标记顺序号为0
                    classes = loadExtensionClass();
                    cachedClasses.setValue(classes);
                    cachedClasses.setOrder(0);
                }
            }
        }
        return classes;
    }

    /**
     * 加载当前ExtensionLoader中clazz的所有SPI的实现类
     */
    private Map<String, ClassEntity> loadExtensionClass() {
        SPI annotation = clazz.getAnnotation(SPI.class);
        if (Objects.nonNull(annotation)) {
            // 这里就是前面提到，如果@SPI注解的value()方法非空白返回值会作为默认实现的别名
            // 也就是如果只使用了@SPI，那么就无法获取默认实现
            // 如果使用了@SPI("xxx")，可以通过别名xxx去映射和获取默认实现
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                cachedDefaultName = value;
            }
        }
        // 初始化一个Hashmap容器用于存储加载的实现类信息，这个变量会透传到下一个方法链
        Map<String, ClassEntity> classes = new HashMap<>(16);
        // 加载目录中的属性文件
        loadDirectory(classes);
        return classes;
    }

    /**
     * 加载目录中的配置文件，并且加载文件中的实现类，目标目录：META-INF/ccsr/
     * Load files under CCSR_DIRECTORY.
     */
    private void loadDirectory(final Map<String, ClassEntity> classes) {
        // 文件名 => META-INF/ccsr/$className
        String fileName = CCSR_DIRECTORY + clazz.getName();
        try {
            // 这里使用类加载器加载文件资源，如果传入的类加载器为空会使用系统类加载器
            Enumeration<URL> urls =
                    Objects.nonNull(this.classLoader)
                            ? classLoader.getResources(fileName)
                            : ClassLoader.getSystemResources(fileName);
            // 遍历解析的文件URL集合
            if (Objects.nonNull(urls)) {
                while (urls.hasMoreElements()) {
                    // 通过文件URL加载资源
                    URL url = urls.nextElement();
                    loadResources(classes, url);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("load extension class error", ex);
        }
    }

    /**
     * 加载文件资源，解析文件并且加载实现类存储到classes中
     */
    private void loadResources(final Map<String, ClassEntity> classes, final URL url)
            throws IOException {
        // 读取URL文件资源，加载到Properties中，每行格式为name=classPath
        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach(
                    (k, v) -> {
                        String name = (String) k;
                        String classPath = (String) v;
                        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                            try {
                                // 基于name和classPath进行类加载
                                loadClass(classes, name, classPath);
                            } catch (ClassNotFoundException e) {
                                throw new IllegalStateException(
                                        "load extension resources error", e);
                            }
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("load extension resources error", e);
        }
    }

    /**
     * 基于name（别名）和classPath（类全限定名称）进行类加载
     */
    private void loadClass(
            final Map<String, ClassEntity> classes, final String name, final String classPath)
            throws ClassNotFoundException {
        // 类初始化，并且确定实现类必须是当前@SPI注解标识接口的子类
        Class<?> subClass =
                Objects.nonNull(this.classLoader)
                        ? Class.forName(classPath, true, this.classLoader)
                        : Class.forName(classPath);
        if (!clazz.isAssignableFrom(subClass)) {
            throw new IllegalStateException(
                    "load extension resources error," + subClass + " subtype is not of " + clazz);
        }
        // 实现类必须存在注解@Join
        if (!subClass.isAnnotationPresent(Join.class)) {
            throw new IllegalStateException(
                    "load extension resources error,"
                            + subClass
                            + " without @"
                            + Join.class
                            + " annotation");
        }
        // 如果缓存中不存在同样别名的实现类才进行缓存，已经存在则校验旧的类型和当前实现类型是否一致
        ClassEntity oldClassEntity = classes.get(name);
        if (Objects.isNull(oldClassEntity)) {
            // 创建类信息实体保存别名、顺序号和实现类并且缓存，映射关系：别名 -> 类信息实体
            Join joinAnnotation = subClass.getAnnotation(Join.class);
            ClassEntity classEntity =
                    new ClassEntity(
                            name, joinAnnotation.order(), subClass, joinAnnotation.isSingleton());
            classes.put(name, classEntity);
        } else if (!Objects.equals(oldClassEntity.getClazz(), subClass)) {
            throw new IllegalStateException(
                    "load extension resources error,Duplicate class "
                            + clazz.getName()
                            + " name "
                            + name
                            + " on "
                            + oldClassEntity.getClazz().getName()
                            + " or "
                            + subClass.getName());
        }
    }

    /**
     * 值持有器，用来存储泛型值和值加载顺序
     * The type Holder.
     *
     * @param <T> the type parameter.
     */
    private static final class Holder<T> {

        /**
         * 这里的值引用是volatile修饰，便于某线程更改,另一线程马上读到最新的值
         */
        private volatile T value;

        /**
         * 顺序
         */
        private Integer order;

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(final T value) {
            this.value = value;
        }

        /**
         * set order.
         *
         * @param order order.
         */
        public void setOrder(final Integer order) {
            this.order = order;
        }

        /**
         * get order.
         *
         * @return order.
         */
        public Integer getOrder() {
            return order;
        }
    }

    /**
     * 类实体，主要存放加载的实现类的信息
     */
    private static final class ClassEntity {

        /**
         * 名称，这里是指SPI实现类的别名，不是类名
         */
        private final String name;

        /**
         * 加载顺序号
         */
        private final Integer order;

        /**
         * 是否为单实例
         */
        private final Boolean isSingleton;

        /**
         * SPI实现类
         */
        private Class<?> clazz;

        private ClassEntity(
                final String name,
                final Integer order,
                final Class<?> clazz,
                final boolean isSingleton) {
            this.name = name;
            this.order = order;
            this.clazz = clazz;
            this.isSingleton = isSingleton;
        }

        /**
         * get class.
         *
         * @return class.
         */
        public Class<?> getClazz() {
            return clazz;
        }

        /**
         * set class.
         *
         * @param clazz class.
         */
        public void setClazz(final Class<?> clazz) {
            this.clazz = clazz;
        }

        /**
         * get name.
         *
         * @return name.
         */
        public String getName() {
            return name;
        }

        /**
         * get order.
         *
         * @return order.
         * @see Join#order()
         */
        public Integer getOrder() {
            return order;
        }

        /**
         * Obtaining this class requires creating a singleton object, or multiple instances.
         *
         * @return true or false.
         * @see Join#isSingleton()
         */
        public Boolean isSingleton() {
            return isSingleton;
        }
    }
}
