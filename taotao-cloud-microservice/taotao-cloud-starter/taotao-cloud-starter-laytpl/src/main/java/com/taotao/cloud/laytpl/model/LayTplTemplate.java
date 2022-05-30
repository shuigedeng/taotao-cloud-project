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

package com.taotao.cloud.laytpl.model;

import cn.hutool.script.JavaScriptEngine;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.support.function.Unchecked;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.io.IoUtil;
import com.taotao.cloud.laytpl.exception.LayTplException;
import com.taotao.cloud.laytpl.js.JsConsole;
import com.taotao.cloud.laytpl.js.JsContext;
import com.taotao.cloud.laytpl.js.JsLayTpl;
import com.taotao.cloud.laytpl.properties.LayTplProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * laytpl 服务器端模板
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:30:33
 */
public class LayTplTemplate implements ApplicationContextAware, InitializingBean {

	private final ConcurrentMap<String, String> tplCache = new ConcurrentHashMap<>();
	private final Function<String, String> tplFunction = Unchecked.function(tpl -> {
		Resource resource = LayTplTemplate.this.getApplicationContext().getResource(tpl);
		return IoUtil.readToString(resource.getInputStream());
	});
	private final LayTplProperties tplProperties;
	private final JsConsole console;
	private final FmtFunc fmtFunc;
	private ApplicationContext applicationContext;
	private ScriptEngine engine;

	public LayTplTemplate(LayTplProperties tplProperties, FmtFunc fmtFunc) {
		this.tplProperties = tplProperties;
		this.fmtFunc = fmtFunc;
		this.console = new JsConsole();

		try {
			final ScriptEngineManager engineManager = new ScriptEngineManager();
			//final ScriptEngine engine = engineManager.getEngineByMimeType("text/javascript");
			final ScriptEngine engine = new JavaScriptEngine();
			Bindings bindings = engine.createBindings();
			Map<String, String> config = new HashMap<>(4);
			config.put("open", tplProperties.getOpen());
			config.put("close", tplProperties.getClose());
			bindings.put("console", console);
			bindings.put("fmt", fmtFunc);
			bindings.put("cloud", new JsContext(applicationContext));
			bindings.put("_config", config);
			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
			engine.eval(JsLayTpl.LAY_TPL_JS, bindings);
			this.engine = engine;
			this.engine.eval("console.log('MicaTpl init, laytpl version:{}', laytpl.v);");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 渲染html字符串
	 *
	 * @param tplName 模板名称
	 * @param data    数据模型
	 * @return 渲染后的html
	 */
	public String renderTpl(String tplName, Object data) {
		if (tplName.startsWith(StringPool.SLASH)) {
			tplName = tplName.substring(1);
		}
		final String tplPath = tplProperties.getPrefix() + tplName;
		try {
			String html = tplProperties.isCache()
				? CollectionUtil.computeIfAbsent(tplCache, tplPath, tplFunction)
				: tplFunction.apply(tplPath);
			return renderHtml(html, data);
		} catch (ScriptException e) {
			throw new LayTplException(e);
		}
	}

	/**
	 * 渲染html字符串
	 *
	 * @param html html字符串
	 * @return 渲染后的html
	 */
	public String render(String html) {
		return render(html, new HashMap<>(0));
	}

	/**
	 * 渲染html字符串
	 *
	 * @param html html字符串
	 * @param data 数据模型
	 * @return 渲染后的html
	 */
	public String render(String html, Object data) {
		try {
			return renderHtml(html, data);
		} catch (ScriptException e) {
			throw new LayTplException(e);
		}
	}

	private String renderHtml(String html, Object data) throws ScriptException {
		// 避免多线程问题
		Bindings bindings = engine.createBindings();
		bindings.put("_html_", html);
		bindings.put("data", data);
		return (String) engine.eval("laytpl(_html_).render(data);", bindings);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//final ScriptEngineManager engineManager = new ScriptEngineManager();
		//final ScriptEngine engine = engineManager.getEngineByMimeType("text/javascript");
		//Bindings bindings = engine.createBindings();
		//Map<String, String> config = new HashMap<>(4);
		//config.put("open", tplProperties.getOpen());
		//config.put("close", tplProperties.getClose());
		//bindings.put("console", console);
		//bindings.put("fmt", fmtFunc);
		//bindings.put("cloud", new JsContext(applicationContext));
		//bindings.put("_config", config);
		//engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
		//engine.eval(JsLayTpl.LAY_TPL_JS, bindings);
		//this.engine = engine;
		//this.engine.eval("console.log('MicaTpl init, laytpl version:{}', laytpl.v);");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
