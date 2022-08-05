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

import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.captcha.support.core.dto.Captcha;
import com.taotao.cloud.captcha.support.core.dto.GraphicCaptcha;
import com.taotao.cloud.captcha.support.core.dto.Verification;
import com.taotao.cloud.captcha.support.core.exception.CaptchaHasExpiredException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaIsEmptyException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaMismatchException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaParameterIllegalException;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.awt.Font;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description: 抽象的图形验证码 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:57:36
 */
public abstract class AbstractGraphicRenderer extends AbstractRenderer {

	private static final Duration DEFAULT_EXPIRE = Duration.ofMinutes(1);

	@Autowired
	private RedisRepository redisRepository;

	private GraphicCaptcha graphicCaptcha;

	protected Font getFont() {
		return this.getResourceProvider().getGraphicFont();
	}

	protected int getWidth() {
		return this.getCaptchaProperties().getGraphics().getWidth();
	}

	protected int getHeight() {
		return this.getCaptchaProperties().getGraphics().getHeight();
	}

	protected int getLength() {
		return this.getCaptchaProperties().getGraphics().getLength();
	}

	@Override
	public Captcha getCapcha(String key) {
		String identity = key;
		if (StringUtils.isBlank(identity)) {
			identity = IdUtil.fastUUID();
		}

		Metadata metadata = draw();

		GraphicCaptcha graphicCaptcha = new GraphicCaptcha();
		graphicCaptcha.setIdentity(identity);
		graphicCaptcha.setGraphicImageBase64(metadata.getGraphicImageBase64());
		graphicCaptcha.setCategory(getCategory());
		this.setGraphicCaptcha(graphicCaptcha);

		redisRepository.setExpire(identity, metadata.getCharacters(), DEFAULT_EXPIRE.toMillis(),
			TimeUnit.MILLISECONDS);

		return getGraphicCaptcha();
	}

	@Override
	public boolean verify(Verification verification) {
		if (ObjectUtils.isEmpty(verification) || StringUtils.isEmpty(verification.getIdentity())) {
			throw new CaptchaParameterIllegalException("Parameter value is illegal");
		}

		if (StringUtils.isEmpty(verification.getCharacters())) {
			throw new CaptchaIsEmptyException("Captcha is empty");
		}

		String store = (String) redisRepository.get(verification.getIdentity());
		if (StringUtils.isEmpty(store)) {
			throw new CaptchaHasExpiredException("Stamp is invalid!");
		}

		redisRepository.del(verification.getIdentity());

		String real = verification.getCharacters();

		if (!StringUtils.equalsIgnoreCase(store, real)) {
			throw new CaptchaMismatchException("");
		}

		return true;
	}

	private GraphicCaptcha getGraphicCaptcha() {
		return graphicCaptcha;
	}

	protected void setGraphicCaptcha(GraphicCaptcha graphicCaptcha) {
		this.graphicCaptcha = graphicCaptcha;
	}

}
