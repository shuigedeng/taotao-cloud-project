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

package com.taotao.cloud.sys.biz.utils;
import com.taotao.boot.common.utils.log.LogUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * ToolUtils
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:22:32
 */
public class ToolUtils {

    /** from:yyyy-MM-dd HH:mm:ss to: "yyyy-MM-dd'T'HH:mm:ss'Z'" 2013-01-30T07:00:00Z */
    public static String dataFormate(String time) {
        String data = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date data1 = sdf.parse(time);
            data = simpleDateFormat.format(data1);
        } catch (ParseException e) {
            LogUtils.error(e);
        }
        return data;
    }

    /** 2013-01-30T07:00:00Z */
    public static String EsDateTimeTransfrom(String time, String fromPattern, String toPattern) {
        String data = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toPattern);
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern);
        try {
            Date data1 = sdf.parse(time);
            data = simpleDateFormat.format(data1);
        } catch (ParseException e) {
            LogUtils.error(e);
        }
        return data;
    }

    /**
     * 处理关键字，解决跳词问题
     *
     * @param keywords
     */
    public static String handKeyword(String keywords) {
        StringBuilder handleKeyWords = new StringBuilder();
        if (StringUtils.isNotBlank(keywords)) {
            String[] keywordArray = keywords.split(" ");
            if (keywordArray.length != 0) {
                if (keywordArray.length != 1) {
                    for (int i = 0; i < keywordArray.length; i++) {
                        if (i == keywordArray.length - 1) {
                            if (!"AND".equalsIgnoreCase(keywordArray[i])
                                    && !"OR".equalsIgnoreCase(keywordArray[i])
                                    && !"OR".equalsIgnoreCase(keywordArray[i])) {
                                if (keywordArray[i].startsWith("(")) {
                                    if (keywordArray[i].contains("NOT") || keywordArray[i].contains("not")) {
                                        handleKeyWords.append(keywordArray[i].toUpperCase());
                                        continue;
                                    }
                                    int frontIndex = keywordArray[i].lastIndexOf("(");
                                    handleKeyWords
                                            .append(keywordArray[i], 0, frontIndex + 1)
                                            .append("\"")
                                            .append(keywordArray[i].substring(frontIndex + 1))
                                            .append("\"");
                                } else if (keywordArray[i].endsWith(")")) {
                                    int backIndex = keywordArray[i].indexOf(")");
                                    handleKeyWords
                                            .append("\"")
                                            .append(keywordArray[i], 0, backIndex)
                                            .append("\"")
                                            .append(keywordArray[i].substring(backIndex));
                                } else {
                                    handleKeyWords
                                            .append("\"")
                                            .append(keywordArray[i].toUpperCase())
                                            .append("\"");
                                }
                            } else {
                                handleKeyWords.append(keywordArray[i]);
                            }
                        } else {
                            if (!"AND".equalsIgnoreCase(keywordArray[i])
                                    && !"OR".equalsIgnoreCase(keywordArray[i])
                                    && !"NOT".equalsIgnoreCase(keywordArray[i])) {
                                if (keywordArray[i].startsWith("(")) {
                                    if (keywordArray[i].contains("NOT") || keywordArray[i].contains("not")) {
                                        handleKeyWords
                                                .append(keywordArray[i].toUpperCase())
                                                .append(" ");
                                        continue;
                                    }
                                    int frontIndex = keywordArray[i].lastIndexOf("(");
                                    handleKeyWords
                                            .append(keywordArray[i], 0, frontIndex + 1)
                                            .append("\"")
                                            .append(keywordArray[i].substring(frontIndex + 1))
                                            .append("\" ");
                                } else if (keywordArray[i].endsWith(")")) {
                                    int backIndex = keywordArray[i].indexOf(")");
                                    handleKeyWords
                                            .append("\"")
                                            .append(keywordArray[i], 0, backIndex)
                                            .append("\"")
                                            .append(keywordArray[i].substring(backIndex))
                                            .append(" ");
                                } else {
                                    handleKeyWords
                                            .append("\"")
                                            .append(keywordArray[i])
                                            .append("\" ");
                                }
                            } else {
                                handleKeyWords
                                        .append(keywordArray[i].toUpperCase())
                                        .append(" ");
                            }
                        }
                    }
                } else {
                    handleKeyWords.append("\"").append(keywords).append("\"");
                }
            }
            handleKeyWords = new StringBuilder("(" + handleKeyWords + ")");
        }
        return handleKeyWords.toString();
    }
}
