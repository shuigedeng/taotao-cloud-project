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

import com.taotao.boot.common.constant.RuleConstant;

/**
 * 拼音工具相关异常
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:14:48
 */
public enum PinyinExceptionEnum {

    /** 字符不能转成汉语拼音 */
    PARSE_ERROR(
            RuleConstant.THIRD_ERROR_TYPE_CODE + PinyinConstants.PINYIN_EXCEPTION_STEP_CODE + "01", "拼音转化异常，具体信息：{}");

    /** 错误编码 */
    private final String errorCode;

    /** 提示用户信息 */
    private final String userTip;

    PinyinExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUserTip() {
        return userTip;
    }
}
