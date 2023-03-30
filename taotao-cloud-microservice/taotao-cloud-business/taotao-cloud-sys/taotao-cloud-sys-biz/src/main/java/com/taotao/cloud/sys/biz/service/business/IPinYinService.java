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

package com.taotao.cloud.sys.biz.service.business;

/**
 * 拼音转化接口
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:59:36
 */
public interface IPinYinService {

    /**
     * 获取姓氏的首字母大写
     *
     * <p>百家姓涉及到多音字的，都配置在properties中，优先读取properties中的映射
     *
     * <p>例如：张 -> Z 例如：单 -> S
     *
     * @param lastnameChines 中文姓氏
     * @return {@link String }
     * @since 2023-02-01 13:39:23
     */
    String getLastnameFirstLetterUpper(String lastnameChines);

    /**
     * 将文字转为汉语拼音，取每个字的第一个字母大写
     *
     * <p>例如：你好 => NH
     *
     * @param chineseString 中文字符串
     * @return 中文字符串每个字的第一个字母大写
     * @since 2020/12/4 13:41
     */
    String getChineseStringFirstLetterUpper(String chineseString);

    /**
     * 获取汉字字符串的全拼拼音
     *
     * <p>例如：中国人 -> zhongguoren
     *
     * @param chineseString 中文字符串
     * @return 拼音形式的字符串
     * @since 2020/12/4 14:55
     */
    String parsePinyinString(String chineseString);

    /**
     * 将中文字符串转化为汉语拼音，取每个字的首字母
     *
     * <p>例如：中国人 -> zgr
     *
     * @param chinesString 中文字符串
     * @return 每个字的拼音首字母组合
     * @since 2020/12/4 15:18
     */
    String parseEveryPinyinFirstLetter(String chinesString);

    /**
     * 将中文字符串转移为ASCII码
     *
     * @param chineseString 中文字符串
     * @return ASCII码
     * @since 2020/12/4 15:21
     */
    String getChineseAscii(String chineseString);
}
