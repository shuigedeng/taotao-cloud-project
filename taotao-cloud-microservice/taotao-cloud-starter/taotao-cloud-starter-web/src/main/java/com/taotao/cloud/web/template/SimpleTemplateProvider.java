package com.taotao.cloud.web.template;

/**
 * TemplateProvider 缩写简写扩展，方便页面模板里面使用
 */
public class SimpleTemplateProvider extends TemplateProvider {

	/**
	 * getattr方法 缩写
	 */
	public Object g(String key) {
		return getattr(key);
	}

	/**
	 * setattr方法 缩写
	 */
	public void s(String key, Object value) {
		setattr(key, value);
	}

	public SimpleTemplateProvider s2(String key, Object value) {
		setattr(key, value);
		return this;
	}

	/**
	 * where 简写
	 */
	public Object w(boolean istrue, Object trueObj, Object falseObj) {
		return where(istrue, trueObj, falseObj);
	}

	/**
	 * print 缩写
	 */
	public String p(Object o) {
		return print(o);
	}
}
