package com.taotao.cloud.rpc.common.spi;

import com.taotao.cloud.rpc.common.spi.Activate;
import com.taotao.cloud.rpc.common.spi.AdaptiveExtensionFactory;
import com.taotao.cloud.rpc.common.spi.ExtensionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展点加载器
 * <p>
 * SPI 微内核的核心实现，对标 Dubbo ExtensionLoader。
 * <ul>
 *   <li>通过 {@link ExtensionLoader#getExtensionLoader(Class)} 获取某个 SPI 接口的加载器（单例）</li>
 *   <li>通过 {@link #getExtension(String)} 按 key 加载具体扩展实现</li>
 *   <li>通过 {@link #getDefaultExtension()} 获取默认扩展（@SPI 注解值）</li>
 *   <li>通过 {@link #getActivateExtension(String...)} 加载激活的扩展集</li>
 * </ul>
 *
 * <p>资源文件路径：META-INF/ttc-rpc/&lt;完整接口类名&gt;
 * <pre>
 * failover=com.taotao.cloud.rpc.cluster.FailoverCluster
 * </pre>
 */
public class ExtensionLoader<T> {

    private static final Logger log = LogManager.getLogger(ExtensionLoader.class);

    /** 资源目录 */
    private static final String ttcrpc_DIRECTORY = "META-INF/ttc-rpc/";

    /** 所有接口的加载器缓存（接口 Class -> ExtensionLoader） */
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    /**
     * 单接口扩展缓存（name -> Class）
     */
    private final ConcurrentMap<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();

    /**
     * 单接口扩展实例缓存（name -> 实例）
     */
    private final ConcurrentMap<String, Object> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 激活扩展缓存（group -> Map&lt;name, instance&gt;）
     */
    private final ConcurrentMap<String, Map<String, Class<?>>> cachedActivates = new ConcurrentHashMap<>();

    /**
     * @SPI 默认扩展名
     */
    private volatile String cachedDefaultName;

    /**
     * 接口 Class
     */
    private final Class<?> type;

    /**
     * 对象工厂（注入用）
     */
    private final ExtensionFactory objectFactory;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
        this.objectFactory = (type == ExtensionFactory.class ? null : new AdaptiveExtensionFactory());
    }

    /**
     * 获取某个 SPI 接口的扩展加载器（全局单例）
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not annotated with @SPI");
        }
        ExtensionLoader<T> loader = (ExtensionLoader<T>) LOADERS.get(type);
        if (loader == null) {
            LOADERS.putIfAbsent(type, new ExtensionLoader<>(type));
            loader = (ExtensionLoader<T>) LOADERS.get(type);
        }
        return loader;
    }

    /**
     * 按 name 获取扩展实例
     */
    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name == null");
        }
        if ("true".equals(name)) {
            return getDefaultExtension();
        }
        Object instance = cachedInstances.get(name);
        if (instance == null) {
            synchronized (cachedInstances) {
                instance = cachedInstances.get(name);
                if (instance == null) {
                    Class<?> clazz = getExtensionClass(name);
                    if (clazz == null) {
                        throw new IllegalStateException("No such extension: " + name + " for type " + type.getName());
                    }
                    try {
                        instance = clazz.newInstance();
                        cachedInstances.put(name, instance);
                    } catch (Throwable t) {
                        throw new IllegalStateException("Failed to instantiate extension: " + name, t);
                    }
                }
            }
        }
        return (T) instance;
    }

    /**
     * 获取默认扩展
     */
    public T getDefaultExtension() {
        String name = getDefaultExtensionName();
        if (name == null || name.isEmpty()) {
            return null;
        }
        return getExtension(name);
    }

    /**
     * 是否存在指定扩展
     */
    public boolean hasExtension(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Class<?> clazz = getExtensionClass(name);
        return clazz != null;
    }

    /**
     * 获取所有已加载的扩展名
     */
    public List<String> getExtensionNames() {
        return new ArrayList<>(getExtensionClasses().keySet());
    }

    /**
     * 按 group 加载所有激活的扩展
     *
     * @param groups 可变参数：consumer / provider / client / server ...
     */
    public List<T> getActivateExtension(String... groups) {
        List<T> exts = new ArrayList<>();
        List<String> names = new ArrayList<>();
        // cachedActivates 只有在扩展类被解析之后才会填充；不先触发加载的话冷加载器永远返回空表
        getExtensionClasses();
        for (String group : groups) {
            Map<String, Class<?>> map = cachedActivates.get(group);
            if (map != null) {
                names.addAll(map.keySet());
            }
        }
        if (names.isEmpty()) {
            return exts;
        }
        // 按 @Activate.order 排序
        List<T> sorted = new ArrayList<>();
        for (String name : names) {
            Class<?> clazz = null;
            for (String group : groups) {
                Map<String, Class<?>> map = cachedActivates.get(group);
                if (map != null) {
                    clazz = map.get(name);
                    if (clazz != null) break;
                }
            }
            if (clazz == null) continue;
            int order = 0;
            Activate anno = clazz.getAnnotation(Activate.class);
            if (anno != null) {
                order = anno.order();
            }
            sorted.add(getExtension(name));
            // 简单按 order 排序
        }
        sorted.sort((a, b) -> {
            int oa = orderOf(a);
            int ob = orderOf(b);
            return Integer.compare(oa, ob);
        });
        return sorted;
    }

    private int orderOf(Object o) {
        if (o == null) return 0;
        Class<?> c = o.getClass();
        Activate anno = c.getAnnotation(Activate.class);
        return anno == null ? 0 : anno.order();
    }

    /**
     * 获取默认扩展名
     */
    public String getDefaultExtensionName() {
        if (cachedDefaultName == null) {
            SPI spi = type.getAnnotation(SPI.class);
            cachedDefaultName = spi == null ? null : spi.value();
        }
        return cachedDefaultName;
    }

    /**
     * 加载并返回扩展 Class
     */
    private Class<?> getExtensionClass(String name) {
        Class<?> clazz = cachedClasses.get(name);
        if (clazz == null) {
            synchronized (cachedClasses) {
                clazz = cachedClasses.get(name);
                if (clazz == null) {
                    loadExtensionClasses();
                    clazz = cachedClasses.get(name);
                }
            }
        }
        return clazz;
    }

    /**
     * 加载接口所有扩展类
     */
    private void loadExtensionClasses() {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            cachedDefaultName = spi.value();
        }
        loadFromDirectory();
    }

    /**
     * 从 META-INF/ttc-rpc/ 目录加载所有扩展
     */
    private void loadFromDirectory() {
        String fileName = ttcrpc_DIRECTORY + type.getName();
        try {
            ClassLoader cl = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = cl == null
                    ? ClassLoader.getSystemResources(fileName)
                    : cl.getResources(fileName);
            if (urls == null || !urls.hasMoreElements()) {
                log.warn("No ttc-RPC extensions found for type {} (expected file {})", type.getName(), fileName);
                return;
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try (InputStream in = url.openStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq <= 0) continue;
                        String name = line.substring(0, eq).trim();
                        String className = line.substring(eq + 1).trim();
                        try {
                            Class<?> clazz = Class.forName(className, true, cl);
                            // 必须是 type 的实现
                            if (!type.isAssignableFrom(clazz)) {
                                throw new IllegalStateException(className + " is not assignable to " + type.getName());
                            }
                            cachedClasses.putIfAbsent(name, clazz);

                            // 收集 @Activate 标注的类
                            Activate anno = clazz.getAnnotation(Activate.class);
                            if (anno != null) {
                                for (String group : anno.group()) {
                                    cachedActivates.computeIfAbsent(group, k -> new HashMap<>())
                                            .putIfAbsent(name, clazz);
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("Failed to load extension class: " + className, e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read extension file: " + fileName, e);
        }
    }

    /**
     * 获取所有已加载的扩展类（按需加载）
     */
    private Map<String, Class<?>> getExtensionClasses() {
        if (cachedClasses.isEmpty()) {
            synchronized (cachedClasses) {
                if (cachedClasses.isEmpty()) {
                    loadExtensionClasses();
                }
            }
        }
        return cachedClasses;
    }

    /**
     * 获取未排序的所有扩展名
     */
    public Map<String, Class<?>> getExtensionClassesMap() {
        return Collections.unmodifiableMap(getExtensionClasses());
    }
}
