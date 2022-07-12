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
package com.taotao.cloud.data.mybatis.plus.cipher.service;

import com.taotao.cloud.data.mybatis.plus.cipher.util.EncryptCodeUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CryptServiceImpl
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 14:04:42
 */
public class CryptServiceImpl implements CryptService {

	@Override
	public String encrypt(String value) {
		return EncryptCodeUtil.encryptData(value);
	}

	@Override
	public String decrypt(String value) {
		return EncryptCodeUtil.decryptData(value);
	}

	@Override
	public Map<String, String> batchDecrypt(List<String> ori) {
		Map<String, String> decrypts = new HashMap<>(ori.size());
		for (String orj : ori) {
			decrypts.put(orj, EncryptCodeUtil.decryptData(orj));
		}
		return decrypts;
	}

	@Override
	public Map<String, String> batchEncrypt(List<String> ori) {
		Map<String, String> decrypts = new HashMap<>(ori.size());
		for (String orj : ori) {
			decrypts.put(orj, EncryptCodeUtil.encryptData(orj));
		}
		return decrypts;
	}
}
