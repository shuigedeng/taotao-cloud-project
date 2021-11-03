/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.xss.model;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.xss.properties.XssProperties;
import com.taotao.cloud.xss.utils.XssUtil;
import java.beans.PropertyEditorSupport;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 表单 xss 处理
 *
 * @author L.cm
 */
@ControllerAdvice
public class FormXssClean {

	private final XssProperties properties;
	private final XssCleaner xssCleaner;

	public FormXssClean(XssProperties properties, XssCleaner xssCleaner) {
		this.properties = properties;
		this.xssCleaner = xssCleaner;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 处理前端传来的表单字符串
		binder.registerCustomEditor(String.class,
			new StringPropertiesEditor(xssCleaner, properties));
	}

	public static class StringPropertiesEditor extends PropertyEditorSupport {

		private final XssCleaner xssCleaner;
		private final XssProperties properties;

		public StringPropertiesEditor(XssCleaner xssCleaner,
			XssProperties properties) {
			this.xssCleaner = xssCleaner;
			this.properties = properties;
		}

		public StringPropertiesEditor(Object source, XssCleaner xssCleaner,
			XssProperties properties) {
			super(source);
			this.xssCleaner = xssCleaner;
			this.properties = properties;
		}

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : StringPool.EMPTY;
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			} else if (XssHolder.isEnabled()) {
				String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
				setValue(value);
				LogUtil.debug("Request parameter value:{} cleaned up by mica-xss, current value is:{}.",
					text, value);
			} else {
				setValue(XssUtil.trim(text, properties.isTrimText()));
			}
		}
	}

}
