package com.taotao.cloud.stock.api.common.util.xss;

import com.xtoon.boot.common.util.exception.XTException;
import org.apache.commons.lang.StringUtils;

/**
 * SQL过滤
 *
 * @author haoxin
 * @date 2021-02-02
 **/
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new XTException("包含非法字符");
            }
        }

        return str;
    }
}
