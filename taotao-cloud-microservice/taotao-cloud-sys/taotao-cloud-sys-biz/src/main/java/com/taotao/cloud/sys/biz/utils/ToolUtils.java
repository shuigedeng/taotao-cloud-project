package com.taotao.cloud.sys.biz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class ToolUtils {

    /**
     * from:yyyy-MM-dd HH:mm:ss
     * to: "yyyy-MM-dd'T'HH:mm:ss'Z'"
     * 2013-01-30T07:00:00Z
     */
    public static String dataFormate(String time) {
        String data = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date data1 = sdf.parse(time);
            data = simpleDateFormat.format(data1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 2013-01-30T07:00:00Z
     */
    public static String EsDateTimeTransfrom(String time, String fromPattern, String toPattern) {
        String data = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toPattern);
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern);
        try {
            Date data1 = sdf.parse(time);
            data = simpleDateFormat.format(data1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 处理关键字，解决跳词问题
     * @param keywords
     */
    public static String handKeyword(String keywords) {
        String handleKeyWords = "";
        if (StringUtils.isNotBlank(keywords)) {
            String[] keywordArray = keywords.split(" ");
            if (keywordArray.length != 0) {
                if (keywordArray.length != 1) {
                    for (int i = 0; i < keywordArray.length; i++) {
                        if (i == keywordArray.length - 1) {
                            if (!keywordArray[i].equalsIgnoreCase("AND") && !keywordArray[i].equalsIgnoreCase("OR") && !keywordArray[i].equalsIgnoreCase("OR")) {
                                if (keywordArray[i].startsWith("(")) {
                                    if (keywordArray[i].contains("NOT") || keywordArray[i].contains("not")) {
                                        handleKeyWords += keywordArray[i].toUpperCase();
                                        continue;
                                    }
                                    int frontIndex = keywordArray[i].lastIndexOf("(");
                                    handleKeyWords += keywordArray[i].substring(0, frontIndex + 1) + "\"" + keywordArray[i].substring(frontIndex + 1) + "\"";
                                } else if (keywordArray[i].endsWith(")")) {
                                    int backIndex = keywordArray[i].indexOf(")");
                                    handleKeyWords += "\"" + keywordArray[i].substring(0, backIndex) + "\"" + keywordArray[i].substring(backIndex);
                                } else {
                                    handleKeyWords += "\"" + keywordArray[i].toUpperCase() + "\"";
                                }
                            } else {
                                handleKeyWords += keywordArray[i];
                            }
                        } else {
                            if (!keywordArray[i].equalsIgnoreCase("AND") && !keywordArray[i].equalsIgnoreCase("OR") && !keywordArray[i].equalsIgnoreCase("NOT")) {
                                if (keywordArray[i].startsWith("(")) {
                                    if (keywordArray[i].contains("NOT") || keywordArray[i].contains("not")) {
                                        handleKeyWords += keywordArray[i].toUpperCase() + " ";
                                        continue;
                                    }
                                    int frontIndex = keywordArray[i].lastIndexOf("(");
                                    handleKeyWords += keywordArray[i].substring(0, frontIndex + 1) + "\"" + keywordArray[i].substring(frontIndex + 1) + "\" ";
                                } else if (keywordArray[i].endsWith(")")) {
                                    int backIndex = keywordArray[i].indexOf(")");
                                    handleKeyWords += "\"" + keywordArray[i].substring(0, backIndex) + "\"" + keywordArray[i].substring(backIndex) + " ";
                                } else {
                                    handleKeyWords += "\"" + keywordArray[i] + "\" ";
                                }
                            } else {
                                handleKeyWords += keywordArray[i].toUpperCase() + " ";
                            }
                        }
                    }
                } else {
                    handleKeyWords += "\"" + keywords + "\"";
                }
            }
            handleKeyWords = "(" + handleKeyWords + ")";
    }
        return handleKeyWords;
    }


}
