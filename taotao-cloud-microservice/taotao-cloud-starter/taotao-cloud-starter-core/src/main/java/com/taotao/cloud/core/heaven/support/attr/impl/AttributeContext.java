package com.taotao.cloud.core.heaven.support.attr.impl;


import com.taotao.cloud.core.heaven.support.attr.IAttributeContext;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.heaven.util.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性上下文上下文
 *
 * [一定线程安全吗](https://segmentfault.com/a/1190000018954561?utm_source=tag-newest)
 */
public class AttributeContext implements IAttributeContext {

    /**
     * 上下文
     * @since 0.1.41
     */
    private Map<String, Object> context;

    public AttributeContext() {
        this.context = new ConcurrentHashMap<>();
    }

    public AttributeContext(final Map<String, Object> map) {
        this.context = new ConcurrentHashMap<>(map);
    }

    /**
     * 设置属性 map
     * @param map map 信息
     * @return this
     * @since 0.1.44
     */
    protected AttributeContext putAttrMap(final Map<String, ?> map) {
        ArgUtil.notNull(map, "map");

        this.context.putAll(map);
        return this;
    }

    /**
     * 获取明细集合
     * @return 明细集合
     * @since 0.1.44
     */
    protected Set<Map.Entry<String, Object>> entrySet() {
        return this.context.entrySet();
    }

    /**
     * 设置属性
     * @param key key
     * @param value 值
     * @return this
     * @since 0.1.41
     */
    @Override
    public AttributeContext putAttr(final String key, final Object value) {
        context.put(key, value);
        return this;
    }

    /**
     * 获取配置属性
     * @return 目标对象
     * @since 0.1.41
     */
    @Override
    public Object getAttr(final String key) {
        return context.get(key);
    }

    @Override
    public Optional<Object> getAttrOptional(String key) {
        Object object = getAttr(key);
        return Optional.ofNullable(object);
    }

    @Override
    public String getAttrString(String key) {
        Object object = getAttr(key);
        return ObjectUtil.objectToString(object);
    }

    @Override
    public Boolean getAttrBoolean(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Boolean.class);
    }

    @Override
    public Character getAttrCharacter(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Character.class);
    }

    @Override
    public Byte getAttrByte(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Byte.class);
    }

    @Override
    public Short getAttrShort(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Short.class);
    }

    @Override
    public Integer getAttrInteger(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Integer.class);
    }

    @Override
    public Float getAttrFloat(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Float.class);
    }

    @Override
    public Double getAttrDouble(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Double.class);
    }

    @Override
    public Long getAttrLong(String key) {
        Optional<Object> objectOptional = getAttrOptional(key);
        return objectOptional.getCastOrNull(Long.class);
    }

    @Override
    public IAttributeContext removeAttr(String key) {
        context.remove(key);
        return this;
    }

    @Override
    public boolean containsKey(String key) {
        return context.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return context.keySet();
    }

}
