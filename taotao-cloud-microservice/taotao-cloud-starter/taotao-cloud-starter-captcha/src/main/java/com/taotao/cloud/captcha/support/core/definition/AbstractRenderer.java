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
package com.taotao.cloud.captcha.support.core.definition;

import cn.hutool.core.img.ImgUtil;
import com.taotao.cloud.captcha.support.core.properties.CaptchaProperties;
import com.taotao.cloud.captcha.support.core.provider.ResourceProvider;
import java.awt.image.BufferedImage;

/**
 * <p>Description: 基础绘制器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:57:39
 */
public abstract class AbstractRenderer implements Renderer {

	protected static final String BASE64_PNG_IMAGE_PREFIX = "data:image/png;base64,";
	protected static final String BASE64_GIF_IMAGE_PREFIX = "data:image/gif;base64,";

	private ResourceProvider resourceProvider;

	public void setResourceProvider(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	public ResourceProvider getResourceProvider() {
		return resourceProvider;
	}

	protected CaptchaProperties getCaptchaProperties() {
		return getResourceProvider().getCaptchaProperties();
	}

	protected String getBase64ImagePrefix() {
		return BASE64_PNG_IMAGE_PREFIX;
	}

	protected String toBase64(BufferedImage bufferedImage) {
		String image = ImgUtil.toBase64(bufferedImage, ImgUtil.IMAGE_TYPE_PNG);
		return getBase64ImagePrefix() + image;
	}
}
