package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SensitiveWordBsConfigTest {

    @Test
    public void configTest() {
        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(true)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .init();

        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";
        Assert.assertTrue(wordBs.contains(text));
    }

}
