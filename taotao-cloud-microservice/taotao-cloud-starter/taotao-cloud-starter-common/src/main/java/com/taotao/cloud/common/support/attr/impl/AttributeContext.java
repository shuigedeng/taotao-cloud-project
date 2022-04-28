package com.taotao.cloud.common.support.attr.impl;


import com.taotao.cloud.common.support.attr.IAttributeContext;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性上下文上下文
 * <p>
 * [一定线程安全吗](<a href="https://segmentfault.com/a/1190000018954561?utm_source=tag-newest">https://segmentfault.com/a/1190000018954561?utm_source=tag-newest</a>)
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:06:56
 */
public class AttributeContext implements IAttributeContext {

	/**
	 * 上下文
	 */
	private final Map<String, Object> context;

	public AttributeContext() {
		this.context = new ConcurrentHashMap<>();
	}

	public AttributeContext(final Map<String, Object> map) {
		this.context = new ConcurrentHashMap<>(map);
	}

	/**
	 * 设置属性 map
	 *
	 * @param map map 信息
	 * @return this
	 */
	protected AttributeContext putAttrMap(final Map<String, ?> map) {
		ArgUtil.notNull(map, "map");

		this.context.putAll(map);
		return this;
	}

	/**
	 * 获取明细集合
	 *
	 * @return 明细集合
	 */
	protected Set<Map.Entry<String, Object>> entrySet() {
		return this.context.entrySet();
	}

	/**
	 * 设置属性
	 *
	 * @param key   key
	 * @param value 值
	 * @return this
	 */
	@Override
	public AttributeContext putAttr(final String key, final Object value) {
		context.put(key, value);
		return this;
	}

	/**
	 * 获取配置属性
	 *
	 * @return 目标对象
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
		return (Boolean) objectOptional.orElse(false);
	}

	@Override
	public Character getAttrCharacter(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Character) objectOptional.orElse("");
	}

	@Override
	public Byte getAttrByte(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Byte) objectOptional.orElse(0);
	}

	@Override
	public Short getAttrShort(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Short) objectOptional.orElse(0);
	}

	@Override
	public Integer getAttrInteger(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Integer) objectOptional.orElse(0);
	}

	@Override
	public Float getAttrFloat(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Float) objectOptional.orElse(0);
	}

	@Override
	public Double getAttrDouble(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Double) objectOptional.orElse(0);
	}

	@Override
	public Long getAttrLong(String key) {
		Optional<Object> objectOptional = getAttrOptional(key);
		return (Long) objectOptional.orElse(0L);
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
