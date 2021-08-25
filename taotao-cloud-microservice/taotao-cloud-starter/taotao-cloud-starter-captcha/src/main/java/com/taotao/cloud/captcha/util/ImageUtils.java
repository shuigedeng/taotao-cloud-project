/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.captcha.util;

import com.taotao.cloud.captcha.model.CaptchaBaseMapEnum;
import com.taotao.cloud.common.utils.LogUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

/**
 * ImageUtils
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:52
 */
public class ImageUtils {

	private static Map<String, String> originalCacheMap = new ConcurrentHashMap();  //滑块底图
	private static Map<String, String> slidingBlockCacheMap = new ConcurrentHashMap(); //滑块
	private static Map<String, String> picClickCacheMap = new ConcurrentHashMap(); //点选文字
	private static Map<String, String[]> fileNameMap = new ConcurrentHashMap<>();

	public static void cacheImage(String captchaOriginalPathJigsaw,
		String captchaOriginalPathClick) {
		//滑动拼图
		if (StringUtils.isBlank(captchaOriginalPathJigsaw)) {
			originalCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/original"));
			slidingBlockCacheMap.putAll(
				getResourcesImagesFile("defaultImages/jigsaw/slidingBlock"));
		} else {
			originalCacheMap.putAll(
				getImagesFile(captchaOriginalPathJigsaw + File.separator + "original"));
			slidingBlockCacheMap.putAll(
				getImagesFile(captchaOriginalPathJigsaw + File.separator + "slidingBlock"));
		}
		//点选文字
		if (StringUtils.isBlank(captchaOriginalPathClick)) {
			picClickCacheMap.putAll(getResourcesImagesFile("defaultImages/pic-click"));
		} else {
			picClickCacheMap.putAll(getImagesFile(captchaOriginalPathClick));
		}
		fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(),
			originalCacheMap.keySet().toArray(new String[0]));
		fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(),
			slidingBlockCacheMap.keySet().toArray(new String[0]));
		fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(),
			picClickCacheMap.keySet().toArray(new String[0]));
		LogUtil.info("初始化底图:{0}", JsonUtil.toJSONString(fileNameMap));
	}

	public static void cacheBootImage(Map<String, String> originalMap,
		Map<String, String> slidingBlockMap, Map<String, String> picClickMap) {
		originalCacheMap.putAll(originalMap);
		slidingBlockCacheMap.putAll(slidingBlockMap);
		picClickCacheMap.putAll(picClickMap);
		fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(),
			originalCacheMap.keySet().toArray(new String[0]));
		fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(),
			slidingBlockCacheMap.keySet().toArray(new String[0]));
		fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(),
			picClickCacheMap.keySet().toArray(new String[0]));
		LogUtil.info("自定义resource底图:{0}", JsonUtil.toJSONString(fileNameMap));
	}


	public static BufferedImage getOriginal() {
		String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
		if (null == strings || strings.length == 0) {
			return null;
		}
		Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
		String s = originalCacheMap.get(strings[randomInt]);
		return getBase64StrToImage(s);
	}

	public static String getslidingBlock() {
		String[] strings = fileNameMap.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
		if (null == strings || strings.length == 0) {
			return null;
		}
		Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
		String s = slidingBlockCacheMap.get(strings[randomInt]);
		return s;
	}

	public static BufferedImage getPicClick() {
		String[] strings = fileNameMap.get(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue());
		if (null == strings || strings.length == 0) {
			return null;
		}
		Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
		String s = picClickCacheMap.get(strings[randomInt]);
		return getBase64StrToImage(s);
	}

	/**
	 * 图片转base64 字符串
	 *
	 * @param templateImage
	 * @return
	 */
	public static String getImageToBase64Str(BufferedImage templateImage) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(templateImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();

		Base64.Encoder encoder = Base64.getEncoder();

		return encoder.encodeToString(bytes).trim();
	}

	/**
	 * base64 字符串转图片
	 *
	 * @param base64String
	 * @return
	 */
	public static BufferedImage getBase64StrToImage(String base64String) {
		try {
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] bytes = decoder.decode(base64String);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			return ImageIO.read(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static Map<String, String> getResourcesImagesFile(String path) {
		//默认提供六张底图
		Map<String, String> imgMap = new HashMap<>();
		ClassLoader classLoader = ImageUtils.class.getClassLoader();
		for (int i = 1; i <= 6; i++) {
			InputStream resourceAsStream = classLoader.getResourceAsStream(
				path.concat("/").concat(String.valueOf(i).concat(".png")));
			byte[] bytes = new byte[0];
			try {
				bytes = FileCopyUtils.copyToByteArray(resourceAsStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String string = Base64Utils.encodeToString(bytes);
			String filename = String.valueOf(i).concat(".png");
			imgMap.put(filename, string);
		}
		return imgMap;
	}

	private static Map<String, String> getImagesFile(String path) {
		Map<String, String> imgMap = new HashMap<>();
		File file = new File(path);
		if (!file.exists()) {
			return new HashMap<>();
		}
		File[] files = file.listFiles();
		Arrays.stream(files).forEach(item -> {
			try {
				FileInputStream fileInputStream = new FileInputStream(item);
				byte[] bytes = FileCopyUtils.copyToByteArray(fileInputStream);
				String string = Base64Utils.encodeToString(bytes);
				imgMap.put(item.getName(), string);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return imgMap;
	}

}
