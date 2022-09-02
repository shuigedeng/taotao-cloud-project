package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SensitiveWordBsEnglishTest {

    /**
     * 忽略英文写法
     */
    @Test
    public void ignoreEnglishStyleTest() {
        final String text = "Ⓕⓤc⒦ the bad words";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assertions.assertEquals("[Ⓕⓤc⒦]", wordList.toString());
    }

}
