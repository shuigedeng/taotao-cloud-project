/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package com.taotao.cloud.sys.biz.service;

/**
 * 拼音转化接口
 *
 * @author fengshuonan
 * @date 2020/12/4 9:30
 */
public interface IPinYinService {

    /**
     * 获取姓氏的首字母大写
     * <p>
     * 百家姓涉及到多音字的，都配置在properties中，优先读取properties中的映射
     * <p>
     * 例如：张 -> Z
     * 例如：单 -> S
     *
     * @param lastnameChines 中文姓氏
     * @return 姓氏的首字母大写
     * @author fengshuonan
     * @date 2020/12/4 10:34
     */
    String getLastnameFirstLetterUpper(String lastnameChines);

    /**
     * 将文字转为汉语拼音，取每个字的第一个字母大写
     * <p>
     * 例如：你好 => NH
     *
     * @param chineseString 中文字符串
     * @return 中文字符串每个字的第一个字母大写
     * @author fengshuonan
     * @date 2020/12/4 13:41
     */
    String getChineseStringFirstLetterUpper(String chineseString);

    /**
     * 获取汉字字符串的全拼拼音
     * <p>
     * 例如：中国人 -> zhongguoren
     *
     * @param chineseString 中文字符串
     * @return 拼音形式的字符串
     * @author fengshuonan
     * @date 2020/12/4 14:55
     */
    String parsePinyinString(String chineseString);

    /**
     * 将中文字符串转化为汉语拼音，取每个字的首字母
     * <p>
     * 例如：中国人 -> zgr
     *
     * @param chinesString 中文字符串
     * @return 每个字的拼音首字母组合
     * @author fengshuonan
     * @date 2020/12/4 15:18
     */
    String parseEveryPinyinFirstLetter(String chinesString);

    /**
     * 将中文字符串转移为ASCII码
     *
     * @param chineseString 中文字符串
     * @return ASCII码
     * @author fengshuonan
     * @date 2020/12/4 15:21
     */
    String getChineseAscii(String chineseString);

}
