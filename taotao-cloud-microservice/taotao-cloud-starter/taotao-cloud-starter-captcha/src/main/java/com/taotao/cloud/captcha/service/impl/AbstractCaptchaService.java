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
package com.taotao.cloud.captcha.service.impl;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.captcha.model.Captcha;
import com.taotao.cloud.captcha.model.CaptchaCodeEnum;
import com.taotao.cloud.captcha.model.CaptchaException;
import com.taotao.cloud.captcha.model.Const;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.service.CaptchaService;
import com.taotao.cloud.captcha.util.CacheUtil;
import com.taotao.cloud.captcha.util.ImageUtils;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.secure.AESUtil;
import com.taotao.cloud.common.utils.secure.MD5Util;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

/**
 * AbstractCaptchaService
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:50
 */
public abstract class AbstractCaptchaService implements CaptchaService {

	protected static final String IMAGE_TYPE_PNG = "png";

	protected static int HAN_ZI_SIZE = 25;

	protected static int HAN_ZI_SIZE_HALF = HAN_ZI_SIZE / 2;

	//check校验坐标
	protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";

	//后台二次校验坐标
	protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";

	protected static Long EXPIRESIN_SECONDS = 2 * 60L;

	protected static Long EXPIRESIN_THREE = 3 * 60L;

	protected static String waterMark = "我的水印";

	protected static String waterMarkFontStr = "WenQuanZhengHei.ttf";

	protected Font waterMarkFont;//水印字体

	protected static String slipOffset = "5";

	protected static Boolean captchaAesStatus = true;

	protected static String clickWordFontStr = "WenQuanZhengHei.ttf";

	protected Font clickWordFont;//点选文字字体

	protected static String cacheType = "local";

	protected static int captchaInterferenceOptions = 0;

	private static FrequencyLimitHandler limitHandler;

	//判断应用是否实现了自定义缓存，没有就使用内存
	@Override
	public void init(final Properties config) {
		//初始化底图
		boolean aBoolean = Boolean.parseBoolean(config.getProperty(Const.CAPTCHA_INIT_ORIGINAL));
		if (!aBoolean) {
			ImageUtils.cacheImage(config.getProperty(Const.ORIGINAL_PATH_JIGSAW),
				config.getProperty(Const.ORIGINAL_PATH_PIC_CLICK));
		}

		LogUtil.info("--->>>初始化验证码底图<<<---" + captchaType());
		waterMark = config.getProperty(Const.CAPTCHA_WATER_MARK, "我的水印");
		slipOffset = config.getProperty(Const.CAPTCHA_SLIP_OFFSET, "5");
		waterMarkFontStr = config.getProperty(Const.CAPTCHA_WATER_FONT, "WenQuanZhengHei.ttf");
		captchaAesStatus = Boolean.parseBoolean(
			config.getProperty(Const.CAPTCHA_AES_STATUS, "true"));
		clickWordFontStr = config.getProperty(Const.CAPTCHA_FONT_TYPE, "WenQuanZhengHei.ttf");
		//clickWordFontStr = config.getProperty(Const.CAPTCHA_FONT_TYPE, "SourceHanSansCN-Normal.otf");
		cacheType = config.getProperty(Const.CAPTCHA_CACHETYPE, "local");
		captchaInterferenceOptions = Integer.parseInt(
			config.getProperty(Const.CAPTCHA_INTERFERENCE_OPTIONS, "0"));

		// 部署在linux中，如果没有安装中文字段，水印和点选文字，中文无法显示，
		// 通过加载resources下的font字体解决，无需在linux中安装字体
		loadWaterMarkFont();

		if (cacheType.equals("local")) {
			LogUtil.info("初始化local缓存...");
			CacheUtil.init(
				Integer.parseInt(config.getProperty(Const.CAPTCHA_CACAHE_MAX_NUMBER, "1000")),
				Long.parseLong(config.getProperty(Const.CAPTCHA_TIMING_CLEAR_SECOND, "180")));
		}

		if (config.getProperty(Const.HISTORY_DATA_CLEAR_ENABLE, "0").equals("1")) {
			LogUtil.info("历史资源清除开关...开启..." + captchaType());
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					destroy(config);
				}
			}));
		}

		if (config.getProperty(Const.REQ_FREQUENCY_LIMIT_ENABLE, "0").equals("1")) {
			if (limitHandler == null) {
				LogUtil.info("接口分钟内限流开关...开启...");
				limitHandler = new FrequencyLimitHandler.DefaultLimitHandler(config,
					getCacheService(cacheType));
			}
		}
	}

	protected CaptchaCacheService getCacheService(String cacheType) {
		return CaptchaServiceFactory.getCache(cacheType);
	}

	@Override
	public void destroy(Properties config) {

	}

	@Override
	public Captcha get(Captcha captcha) {
		if (limitHandler != null) {
			captcha.setClientUid(getValidateClientId(captcha));
			// 校验客户端请求数据
			limitHandler.validateGet(captcha);
		}
		return captcha;
	}

	@Override
	public Captcha check(Captcha captcha) {
		if (limitHandler != null) {
			// 验证客户端
           /* ResponseModel ret = limitHandler.validateCheck(captchaVO);
            if(!validatedReq(ret)){
                return ret;
            }
            // 服务端参数验证*/

			captcha.setClientUid(getValidateClientId(captcha));
			limitHandler.validateCheck(captcha);
		}
		return captcha;
	}

	@Override
	public Captcha verification(Captcha captcha) {
		if (captcha == null) {
			throw new CaptchaException(CaptchaCodeEnum.NULL_ERROR.parseError("captcha"));
		}
		if (StrUtil.isEmpty(captcha.getCaptchaVerification())) {
			throw new CaptchaException(
				CaptchaCodeEnum.NULL_ERROR.parseError("captchaVerification"));
		}
		if (limitHandler != null) {
			limitHandler.validateVerify(captcha);
		}
		return captcha;
	}

	protected String getValidateClientId(Captcha req) {
		// 以服务端获取的客户端标识 做识别标志
		if (StrUtil.isNotEmpty(req.getBrowserInfo())) {
			return MD5Util.encrypt(req.getBrowserInfo());
		}
		// 以客户端Ui组件id做识别标志
		if (StrUtil.isNotEmpty(req.getClientUid())) {
			return req.getClientUid();
		}
		return null;
	}

	protected void afterValidateFail(Captcha data) {
		if (limitHandler != null) {
			// 验证失败 分钟内计数
			String fails = String.format(FrequencyLimitHandler.LIMIT_KEY, "FAIL",
				data.getClientUid());
			CaptchaCacheService cs = getCacheService(cacheType);
			if (!cs.exists(fails)) {
				cs.set(fails, "1", 60);
			}
			cs.increment(fails, 1);
		}
	}

	/**
	 * 加载resources下的font字体 部署在linux中，如果没有安装中文字段，水印和点选文字，中文无法显示， 通过加载resources下的font字体解决，无需在linux中安装字体
	 */
	private void loadWaterMarkFont() {
		try {
			if (waterMarkFontStr.toLowerCase().endsWith(".ttf") || waterMarkFontStr.toLowerCase()
				.endsWith(".ttc")
				|| waterMarkFontStr.toLowerCase().endsWith(".otf")) {
				this.waterMarkFont = Font.createFont(Font.TRUETYPE_FONT,
						getClass().getResourceAsStream("/fonts/" + waterMarkFontStr))
					.deriveFont(Font.BOLD, HAN_ZI_SIZE / 2);
			} else {
				this.waterMarkFont = new Font(waterMarkFontStr, Font.BOLD, HAN_ZI_SIZE / 2);
			}

		} catch (Exception e) {
			LogUtil.error(e, "load font error:{}", e.getMessage());
		}
	}

	public static boolean base64StrToImage(String imgStr, String path) {
		if (imgStr == null) {
			return false;
		}

		Base64.Decoder decoder = Base64.getDecoder();
		try {
			// 解密
			byte[] b = decoder.decode(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			//文件夹不存在则自动创建
			File tempFile = new File(path);
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			OutputStream out = new FileOutputStream(tempFile);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 解密前端坐标aes加密
	 */
	public static String decrypt(String point, String key) throws Exception {
		return AESUtil.decrypt(point, key);
	}

	protected static int getEnOrChLength(String s) {
		int enCount = 0;
		int chCount = 0;
		for (int i = 0; i < s.length(); i++) {
			int length = String.valueOf(s.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
			if (length > 1) {
				chCount++;
			} else {
				enCount++;
			}
		}
		int chOffset = (HAN_ZI_SIZE / 2) * chCount + 5;
		int enOffset = enCount * 8;
		return chOffset + enOffset;
	}


}
