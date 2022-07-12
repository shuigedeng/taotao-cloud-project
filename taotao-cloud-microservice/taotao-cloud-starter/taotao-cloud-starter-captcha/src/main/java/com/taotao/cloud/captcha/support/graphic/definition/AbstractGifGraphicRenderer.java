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
package com.taotao.cloud.captcha.support.graphic.definition;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.gif.AnimatedGifEncoder;
import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.common.constant.SymbolConstants;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: Gif 类型图形验证码绘制器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:54:33
 */
public abstract class AbstractGifGraphicRenderer extends AbstractBaseGraphicRenderer {

	@Override
	protected String getBase64ImagePrefix() {
		return BASE64_GIF_IMAGE_PREFIX;
	}

	@Override
	public Metadata draw() {

		String[] drawCharacters = this.getDrawCharacters();

		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		// gif编码类
		AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
		// 生成字符
		gifEncoder.start(out);
		// 设置量化器取样间隔
		gifEncoder.setQuality(180);
		// 帧延迟 (默认100)
		int delay = 100;
		//设置帧延迟
		gifEncoder.setDelay(delay);
		//帧循环次数
		gifEncoder.setRepeat(0);

		IntStream.range(0, drawCharacters.length).forEach(i -> {
			BufferedImage frame = createGifBufferedImage(drawCharacters, i);
			gifEncoder.addFrame(frame);
			frame.flush();
		});

		gifEncoder.finish();

		String characters = StringUtils.join(drawCharacters, SymbolConstants.BLANK);

		Metadata metadata = new Metadata();
		metadata.setGraphicImageBase64(getBase64ImagePrefix() + Base64.encode(out.toByteArray()));
		metadata.setCharacters(characters);
		return metadata;
	}
}
