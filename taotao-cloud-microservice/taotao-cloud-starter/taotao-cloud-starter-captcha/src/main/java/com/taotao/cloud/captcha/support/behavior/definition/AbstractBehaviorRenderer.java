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
package com.taotao.cloud.captcha.support.behavior.definition;


import com.taotao.cloud.captcha.support.core.definition.AbstractRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: 验证码通用基础类 </p>
 *
 * @param <K> 验证码缓存对应Key值的类型。
 * @param <V> 验证码缓存存储数据的值的类型
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:53:22
 */
public abstract class AbstractBehaviorRenderer<K, V> extends AbstractRenderer<K, V> {

	protected int getEnOrZhLength(String s) {
		int enCount = 0;
		int zhCount = 0;
		for (int i = 0; i < s.length(); i++) {
			int length = String.valueOf(s.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
			if (length > 1) {
				zhCount++;
			} else {
				enCount++;
			}
		}
		int zhOffset = getHalfWatermarkFontSize() * zhCount + 5;
		int enOffset = enCount * 8;
		return zhOffset + enOffset;
	}

	private int getWatermarkFontSize() {
		return getCaptchaProperties().getWatermark().getFontSize();
	}

	private int getHalfWatermarkFontSize() {
		return getWatermarkFontSize() / 2;
	}

	protected void addWatermark(Graphics graphics, int width, int height) {
		int fontSize = getHalfWatermarkFontSize();
		Font watermakFont = this.getResourceProvider().getWaterMarkFont(fontSize);
		graphics.setFont(watermakFont);
		graphics.setColor(Color.white);
		String content = this.getCaptchaProperties().getWatermark().getContent();
		graphics.drawString(content, width - getEnOrZhLength(content),
			height - getHalfWatermarkFontSize() + 7);
	}

	protected boolean isUnderOffset(int actualValue, int standardValue, int threshold) {
		return actualValue < standardValue - threshold;
	}

	protected boolean isOverOffset(int actualValue, int standardValue, int threshold) {
		return actualValue > standardValue + threshold;
	}

	protected boolean isDeflected(int actualValue, int standardValue, int threshold) {
		return isUnderOffset(actualValue, standardValue, threshold) || isOverOffset(actualValue,
			standardValue, threshold);
	}
}
