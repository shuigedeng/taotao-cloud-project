package com.taotao.cloud.sensitive.sensitive.word.utils;


import com.google.common.collect.Maps;
import com.taotao.cloud.common.utils.lang.ObjectUtils;

import java.util.Map;

public final class CharUtils {

    private CharUtils() {
    }

    /**
     * 英文字母1
     */
    private static final String LETTERS_ONE =
            "ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ" +
                    "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ" +
                    "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵";

    /**
     * 英文字母2
     */
    private static final String LETTERS_TWO =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "abcdefghijklmnopqrstuvwxyz";


    /**
     * 英文字母 map
     */
    private static final Map<Character, Character> LETTER_MAP = Maps.newHashMap();

    static {
        final int size = LETTERS_ONE.length();

        for(int i = 0; i < size; i++) {
            LETTER_MAP.put(LETTERS_ONE.charAt(i), LETTERS_TWO.charAt(i));
        }
    }

    /**
     * 映射后的 char
     * @param character 待转换的 char
     * @return 结果
     */
    public static Character getMappingChar(final Character character) {
        final Character mapChar = LETTER_MAP.get(character);
        if(ObjectUtils.isNotNull(mapChar)) {
            return mapChar;
        }

        return character;
    }

}
