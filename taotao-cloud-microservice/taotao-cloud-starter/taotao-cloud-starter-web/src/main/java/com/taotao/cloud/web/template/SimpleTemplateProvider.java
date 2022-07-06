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
package com.taotao.cloud.web.template;

/**
 * TemplateProvider 缩写简写扩展，方便页面模板里面使用
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:40:36
 */
public class SimpleTemplateProvider extends TemplateProvider {

    /**
     * getattr方法 缩写
     *
     * @param key key
     * @return {@link Object }
     * @since 2022-07-06 14:40:36
     */
    public Object g(String key) {
		return getattr(key);
	}

    /**
     * setattr方法 缩写
     *
     * @param key   key
     * @param value value
     * @since 2022-07-06 14:40:36
     */
    public void s(String key, Object value) {
		setattr(key, value);
	}

    /**
     * s2
     *
     * @param key   key
     * @param value value
     * @return {@link SimpleTemplateProvider }
     * @since 2022-07-06 14:40:36
     */
    public SimpleTemplateProvider s2(String key, Object value) {
		setattr(key, value);
		return this;
	}

    /**
     * where 缩写
     *
     * @param istrue   istrue
     * @param trueObj  trueObj
     * @param falseObj falseObj
     * @return {@link Object }
     * @since 2022-07-06 14:40:37
     */
    public Object w(boolean istrue, Object trueObj, Object falseObj) {
		return where(istrue, trueObj, falseObj);
	}

    /**
     * print 缩写
     *
     * @param o o
     * @return {@link String }
     * @since 2022-07-06 14:40:37
     */
    public String p(Object o) {
		return print(o);
	}
}
