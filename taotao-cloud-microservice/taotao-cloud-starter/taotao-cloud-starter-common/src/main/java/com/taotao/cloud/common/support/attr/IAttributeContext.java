package com.taotao.cloud.common.support.attr;


import java.util.Optional;
import java.util.Set;

/**
 * 属性上下文上下文
 */
public interface IAttributeContext {

	/**
	 * 设置属性
	 *
	 * @param key   key
	 * @param value 值
	 * @return this
	 */
	IAttributeContext putAttr(final String key, final Object value);

	/**
	 * 获取配置属性
	 *
	 * @param key key
	 * @return 属性
	 */
	Object getAttr(final String key);

	/**
	 * 获取配置属性-Optional
	 *
	 * @param key key
	 * @return 属性
	 */
	Optional<Object> getAttrOptional(final String key);

	/**
	 * 获取属性-字符串形式
	 *
	 * @param key key
	 * @return 属性
	 */
	String getAttrString(final String key);

	/**
	 * 获取属性-Boolean
	 *
	 * @param key key
	 * @return 属性
	 */
	Boolean getAttrBoolean(final String key);

	/**
	 * 获取属性-Character
	 *
	 * @param key key
	 * @return 属性
	 */
	Character getAttrCharacter(final String key);

	/**
	 * 获取属性-Byte
	 *
	 * @param key key
	 * @return 属性
	 */
	Byte getAttrByte(final String key);

	/**
	 * 获取属性-Short
	 *
	 * @param key key
	 * @return 属性
	 */
	Short getAttrShort(final String key);

	/**
	 * 获取属性-Integer
	 *
	 * @param key key
	 * @return 属性
	 */
	Integer getAttrInteger(final String key);

	/**
	 * 获取属性-Float
	 *
	 * @param key key
	 * @return 属性
	 */
	Float getAttrFloat(final String key);

	/**
	 * 获取属性-Double
	 *
	 * @param key key
	 * @return 属性
	 */
	Double getAttrDouble(final String key);

	/**
	 * 获取属性-Long
	 *
	 * @param key key
	 * @return 属性
	 */
	Long getAttrLong(final String key);

	/**
	 * 移除属性
	 *
	 * @param key key
	 * @return this
	 */
	IAttributeContext removeAttr(final String key);

	/**
	 * 是否包含 key
	 *
	 * @param key key
	 * @return this
	 */
	boolean containsKey(final String key);

	/**
	 * 所有的 key 集合
	 *
	 * @return this
	 */
	Set<String> keySet();

}
