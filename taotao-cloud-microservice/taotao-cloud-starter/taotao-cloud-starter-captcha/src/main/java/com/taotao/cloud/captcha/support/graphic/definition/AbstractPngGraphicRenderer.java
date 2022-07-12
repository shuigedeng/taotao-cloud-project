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

import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.common.constant.SymbolConstants;
import java.awt.image.BufferedImage;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: Png 类型图形验证码绘制器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:54:36
 */
public abstract class AbstractPngGraphicRenderer extends AbstractBaseGraphicRenderer {

	@Override
	public Metadata draw() {
		String[] drawCharacters = this.getDrawCharacters();

		BufferedImage bufferedImage = createPngBufferedImage(drawCharacters);

		String characters = StringUtils.join(drawCharacters, SymbolConstants.BLANK);

		Metadata metadata = new Metadata();
		metadata.setGraphicImageBase64(toBase64(bufferedImage));
		metadata.setCharacters(characters);

		return metadata;
	}
}
