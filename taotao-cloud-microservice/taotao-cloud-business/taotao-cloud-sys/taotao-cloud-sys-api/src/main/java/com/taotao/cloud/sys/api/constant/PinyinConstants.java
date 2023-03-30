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

/**
 * 拼音模块常量
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:22:32
 */
public interface PinyinConstants {

    /** 邮件模块的名称 */
    String PINYIN_MODULE_NAME = "kernel-d-pinyin";

    /** 异常枚举的步进值 */
    String PINYIN_EXCEPTION_STEP_CODE = "22";

    /** 中文字符的正则表达式 */
    String CHINESE_WORDS_REGEX = "[\u4E00-\u9FA5]+";
}
