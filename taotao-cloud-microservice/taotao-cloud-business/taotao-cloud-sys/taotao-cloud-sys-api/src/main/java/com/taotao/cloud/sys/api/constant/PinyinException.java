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

package com.taotao.cloud.sys.api.constant;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BaseException;

/**
 * 拼音异常
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:22:32
 */
public class PinyinException extends BaseException {

    public PinyinException(String message) {
        super(message);
    }

    public PinyinException(Integer code, String message) {
        super(code, message);
    }

    public PinyinException(Throwable e) {
        super(e);
    }

    public PinyinException(String message, Throwable e) {
        super(message, e);
    }

    public PinyinException(Integer code, String message, Throwable e) {
        super(code, message, e);
    }

    public PinyinException(ResultEnum result) {
        super(result);
    }

    public PinyinException(ResultEnum result, Throwable e) {
        super(result, e);
    }
}
