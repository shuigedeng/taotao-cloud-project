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

package com.taotao.cloud.sys.biz.service.business.impl;

import static com.taotao.cloud.sys.api.constant.PinyinConstants.CHINESE_WORDS_REGEX;

import com.taotao.cloud.sys.api.constant.PinyinException;
import com.taotao.cloud.sys.biz.service.business.IPinYinService;
import java.util.Properties;
import java.util.Set;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Service;

/** 拼音工具类的实现 */
@Service
public class PinyinServiceImpl implements IPinYinService {

    private final Properties properties = new Properties();

    public PinyinServiceImpl() {
        init();
    }

    /**
     * 初始化多音字中文字和拼音首字母大写的映射
     *
     * @since 2022-03-23 09:01:08
     */
    public void init() {
        // 百家姓多音字的汉语-首字母大写的映射
        properties.put("\u533A", "O");
        properties.put("\u665F", "C");
        properties.put("\u4E50", "Y");
        properties.put("\u5458", "Y");
        properties.put("\u8D20", "Y");
        properties.put("\u9ED1", "H");
        properties.put("\u91CD", "C");
        properties.put("\u4EC7", "Q");
        properties.put("\u79D8", "B");
        properties.put("\u51BC", "X");
        properties.put("\u89E3", "X");
        properties.put("\u6298", "S");
        properties.put("\u5355", "S");
        properties.put("\u6734", "P");
        properties.put("\u7FDF", "Z");
        properties.put("\u67E5", "Z");
        properties.put("\u76D6", "G");
        properties.put("\u4E07\u4FDF", "M I");
        properties.put("\u5C09\u8FDF", "Y C");
    }

    @Override
    public String getLastnameFirstLetterUpper(String lastnameChines) {

        // 从百家姓多音字映射中去比对，有没有包含多音字的，包含多音字姓的以映射表为准，不包含的以普通拼音解析为准
        Set<Object> keys = properties.keySet();
        for (Object lastNameObject : keys) {
            String lastname = (String) lastNameObject;
            if (lastnameChines.length() >= lastname.length() && lastnameChines.startsWith(lastname)) {
                return properties.getProperty(lastname);
            }
        }

        // 返回普通姓氏的首字符大写
        return getFirstLetters(lastnameChines, HanyuPinyinCaseType.UPPERCASE);
    }

    @Override
    public String getChineseStringFirstLetterUpper(String chineseString) {
        return getFirstLetters(chineseString, HanyuPinyinCaseType.UPPERCASE);
    }

    @Override
    public String parsePinyinString(String chineseString) {

        // 拆分每个汉字
        char[] chineseWordsArray = chineseString.toCharArray();

        // 拼音格式化
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        // 最终的拼音字符串
        StringBuilder finalPinyinString = new StringBuilder();
        try {
            // 每一个汉字分别转化为拼音
            for (char chineseWord : chineseWordsArray) {
                // 判断是否为汉字字符
                if (Character.toString(chineseWord).matches(CHINESE_WORDS_REGEX)) {
                    String[] strings = PinyinHelper.toHanyuPinyinStringArray(chineseWord, hanyuPinyinOutputFormat);
                    finalPinyinString.append(strings[0]);
                } else {
                    finalPinyinString.append(chineseWord);
                }
            }
            return finalPinyinString.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            throw new PinyinException(500, e1.getMessage());
        }
    }

    @Override
    public String parseEveryPinyinFirstLetter(String chinesString) {
        StringBuilder convert = new StringBuilder();
        for (int i = 0; i < chinesString.length(); i++) {
            char word = chinesString.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    @Override
    public String getChineseAscii(String chineseString) {
        StringBuilder strBuf = new StringBuilder();
        byte[] bGBK = chineseString.getBytes();
        for (byte b : bGBK) {
            strBuf.append(Integer.toHexString(b & 0xff));
        }
        return strBuf.toString();
    }

    /**
     * 获取中文字符串的首字母，大小写根据传参决定
     *
     * @param chineseString 中文字符串
     * @param caseType 大小写类型
     * @return 首字母大小写
     */
    private static String getFirstLetters(String chineseString, HanyuPinyinCaseType caseType) {

        char[] chinesWords = chineseString.trim().toCharArray();
        StringBuilder hanyupinyin = new StringBuilder();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

        // 输出拼音全部大写
        defaultFormat.setCaseType(caseType);

        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        try {
            for (char word : chinesWords) {
                String str = String.valueOf(word);

                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                if (str.matches(CHINESE_WORDS_REGEX)) {
                    hanyupinyin.append(PinyinHelper.toHanyuPinyinStringArray(word, defaultFormat)[0].charAt(0));
                }
                // 如果字符是数字,取数字
                else if (str.matches("[0-9]+")) {
                    hanyupinyin.append(word);
                }
                // 如果字符是字母,取字母
                else if (str.matches("[a-zA-Z]+")) {
                    hanyupinyin.append(word);
                }
                // 否则不转换，如果是标点符号的话，也带着
                else {
                    hanyupinyin.append(word);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new PinyinException(500, e.getMessage());
        }

        return hanyupinyin.toString();
    }
}
