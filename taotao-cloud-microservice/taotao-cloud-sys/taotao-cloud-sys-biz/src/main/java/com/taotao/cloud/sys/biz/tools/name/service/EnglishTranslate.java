package com.taotao.cloud.sys.biz.tools.name.service;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class EnglishTranslate {
    /**
     * 直译,不用拆词
     * @param source
     * @return
     */
    public abstract Set<String> innerDirectTranslate(String source);

    public Set<String> directTranslate(String source){
        // 先调用翻译工具, 对目标词进行处理
        Set<String> result = innerDirectTranslate(source);

        Set<String> finalResult = new HashSet<>();
        // 然后去特殊字符 , 合并分词
        for (String part : result) {
            String replaceAll = part.replaceAll("!", "");
            String[] charToken = StringUtils.split(replaceAll, " ");
            StringBuilder partMergeChar = new StringBuilder();
            for (int i = 0; i < charToken.length; i++) {
                if (i == 0){
                    partMergeChar.append(StringUtils.uncapitalize(charToken[i]));
                }else{
                    partMergeChar.append(StringUtils.capitalize(charToken[i]));
                }
            }
            finalResult.add(partMergeChar.toString());
        }

        return finalResult;
    }
}
