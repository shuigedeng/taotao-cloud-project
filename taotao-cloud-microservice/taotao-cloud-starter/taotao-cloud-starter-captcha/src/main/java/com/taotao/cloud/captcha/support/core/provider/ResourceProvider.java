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

package com.taotao.cloud.captcha.support.core.provider;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.FontUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaResource;
import com.taotao.cloud.captcha.support.core.definition.enums.FontStyle;
import com.taotao.cloud.captcha.support.core.properties.CaptchaProperties;
import com.taotao.cloud.common.utils.common.ResourceUtil;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * <p>Description: 验证码静态资源加载器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:58:33
 */
@Component
public class ResourceProvider implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ResourceProvider.class);

	private static final String FONT_RESOURCE = "classpath*:/fonts/*.ttf";

	private final Map<String, String[]> imageIndexes = new ConcurrentHashMap<>();
	private final Map<String, String> jigsawOriginalImages = new ConcurrentHashMap<>();
	private final Map<String, String> jigsawTemplateImages = new ConcurrentHashMap<>();
	private final Map<String, String> wordClickImages = new ConcurrentHashMap<>();
	private Map<String, Font> fonts = new ConcurrentHashMap<>();

	private final CaptchaProperties captchaProperties;

	public ResourceProvider(CaptchaProperties captchaProperties) {
		this.captchaProperties = captchaProperties;
	}

	public CaptchaProperties getCaptchaProperties() {
		return captchaProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		String systemName = SystemUtil.getOsInfo().getName();
		log.debug(
			"Before captcha resource loading, check system. Current system is [{}]",
			systemName);

		log.debug("Captcha resource loading is BEGIN！");

		loadImages(jigsawOriginalImages, getCaptchaProperties().getJigsaw().getOriginalResource(),
			CaptchaResource.JIGSAW_ORIGINAL);

		loadImages(jigsawTemplateImages, getCaptchaProperties().getJigsaw().getTemplateResource(),
			CaptchaResource.JIGSAW_TEMPLATE);

		loadImages(wordClickImages, getCaptchaProperties().getWordClick().getImageResource(),
			CaptchaResource.WORD_CLICK);

		loadFonts();

		log.debug("Jigsaw captcha resource loading is END！");
	}

	private static String getBase64Image(Resource resource) {
		try {
			InputStream inputStream = resource.getInputStream();
			byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
			return Base64.encode(bytes);
		} catch (IOException e) {
			log.error(" Captcha get image catch io error!", e);
		}
		return null;
	}

	private static Map<String, String> getImages(String location) {
		if (ResourceUtil.isClasspathAllUrl(location)) {
			try {
				Resource[] resources = ResourceUtil.getResources(location);
				Map<String, String> images = new ConcurrentHashMap<>();
				if (ArrayUtils.isNotEmpty(resources)) {
					Arrays.stream(resources).forEach(resource -> {
						String data = getBase64Image(resource);
						if (StringUtils.isNotBlank(data)) {
							images.put(IdUtil.fastSimpleUUID(), data);
						}
					});
				}
				return images;
			} catch (IOException e) {
				log.error("Analysis the  location [{}] catch io error!", location,
					e);
			}
		}

		return new ConcurrentHashMap<>(8);
	}

	private void loadImages(Map<String, String> container, String location,
		CaptchaResource captchaResource) {
		Map<String, String> resource = getImages(location);

		if (MapUtils.isNotEmpty(resource)) {
			container.putAll(resource);
			log.debug("{} load complete, total number is [{}]",
				captchaResource.getContent(), resource.size());
			imageIndexes.put(captchaResource.name(), resource.keySet().toArray(new String[0]));
		}
	}

	private static Font getFont(Resource resource) {
		try {
			return FontUtil.createFont(resource.getInputStream());
		} catch (IOException e) {
			log.error("Read font catch io error!", e);
		}

		return null;
	}

	private static Map<String, Font> getFonts(String location) {
		if (ResourceUtil.isClasspathAllUrl(location)) {
			try {
				Resource[] resources = ResourceUtil.getResources(location);
				Map<String, Font> fonts = new ConcurrentHashMap<>();
				if (ArrayUtils.isNotEmpty(resources)) {
					Arrays.stream(resources).forEach(resource -> {
						Font font = getFont(resource);
						if (ObjectUtils.isNotEmpty(font)) {
							fonts.put(resource.getFilename(), font);
						}
					});
				}
				return fonts;
			} catch (IOException e) {
				log.error(" Analysis the  location [{}] catch io error!", location,
					e);
			}
		}

		return new ConcurrentHashMap<>(8);
	}

	private void loadFonts() {
		if (MapUtils.isEmpty(fonts)) {
			this.fonts = getFonts(FONT_RESOURCE);
			log.debug("Font load complete, total number is [{}]", fonts.size());
		}
	}

	private Map<String, Font> getFontsUnderLinux() {
		String directory = "/usr/share/fonts";
		boolean includeRequired = FileUtil.exist("/usr/share/fonts", "WenQuanZhengHei.ttf");
		if (includeRequired) {
			File[] fonts = FileUtil.ls(directory);
			if (ArrayUtils.isNotEmpty(fonts)) {
				Map<String, Font> result = Arrays.stream(fonts)
					.collect(Collectors.toMap(File::getName, FontUtil::createFont));
				log.debug("Load [{}] fonts under linux or docker.", result.size());
				return result;
			}
		}

		return new HashMap<>();
	}

	private Font getDefaultFont(String fontName, int fontSize, FontStyle fontStyle) {
		if (StringUtils.isNotBlank(fontName)) {
			return new Font(fontName, fontStyle.getMapping(), fontSize);
		} else {
			return new Font("Arial", fontStyle.getMapping(), fontSize);
		}
	}

	public Font getFont(String fontName, int fontSize, FontStyle fontStyle) {
		if (MapUtils.isEmpty(fonts) || ObjectUtils.isEmpty(fonts.get(fontName))) {
			return getDefaultFont(fontName, fontSize, fontStyle);
		} else {
			return fonts.get(fontName)
				.deriveFont(fontStyle.getMapping(), Integer.valueOf(fontSize).floatValue());
		}
	}

	public Font getFont(String fontName) {
		return getFont(fontName, 32, FontStyle.BOLD);
	}

	public Font getGraphicFont() {
		String fontName = getCaptchaProperties().getGraphics().getFont().getFontName();
		return this.getFont(fontName);
	}

	public Font getWaterMarkFont(int fontSize) {
		String fontName = getCaptchaProperties().getWatermark().getFontName();
		FontStyle fontStyle = getCaptchaProperties().getWatermark().getFontStyle();
		return getFont(fontName, fontSize, fontStyle);
	}

	public Font getChineseFont() {
		return getFont("楷体", 25, FontStyle.PLAIN);
	}

	private String getRandomBase64Image(Map<String, String> container,
		CaptchaResource captchaResource) {
		String[] data = this.imageIndexes.get(captchaResource.name());
		if (ArrayUtils.isNotEmpty(data)) {
			int randomInt = RandomProvider.randomInt(0, data.length);
			return container.get(data[randomInt]);
		}
		return null;
	}

	protected BufferedImage getRandomImage(Map<String, String> container,
		CaptchaResource captchaResource) {
		String data = getRandomBase64Image(container, captchaResource);
		if (StringUtils.isNotBlank(data)) {
			return ImgUtil.toImage(data);
		}

		return null;
	}

	public String getRandomBase64OriginalImage() {
		return getRandomBase64Image(jigsawOriginalImages, CaptchaResource.JIGSAW_ORIGINAL);
	}

	public String getRandomBase64TemplateImage() {
		return getRandomBase64Image(jigsawTemplateImages, CaptchaResource.JIGSAW_TEMPLATE);
	}

	public BufferedImage getRandomOriginalImage() {
		return getRandomImage(jigsawOriginalImages, CaptchaResource.JIGSAW_ORIGINAL);
	}

	public BufferedImage getRandomTemplateImage() {
		return getRandomImage(jigsawOriginalImages, CaptchaResource.JIGSAW_ORIGINAL);
	}

	public BufferedImage getRandomWordClickImage() {
		return getRandomImage(wordClickImages, CaptchaResource.WORD_CLICK);
	}
}
