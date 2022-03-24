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
package com.taotao.cloud.auth.biz.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.stereotype.Service;

/**
 * QrCodeService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/21 21:07
 */
@Service
public class QrCodeService {

	public String qrcode() {
		QrConfig config = QrConfig.create().setWidth(5).setHeight(5);
		return QrCodeUtil.generateAsBase64("https://m.taotaocloud.top/#/pages/index/index", config, ImgUtil.IMAGE_TYPE_PNG);
	}

}
