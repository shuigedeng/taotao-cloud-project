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

package com.taotao.cloud.shell.jcommand;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class UrlParameterValidator implements IParameterValidator {
    @Override
    public void validate(String key, String value) throws ParameterException {
        try {
			URL url = URI.create(value).toURL();
		} catch (MalformedURLException e) {
            throw new ParameterException("参数 " + key + " 的值必须是 URL 格式");
        }
    }
}
