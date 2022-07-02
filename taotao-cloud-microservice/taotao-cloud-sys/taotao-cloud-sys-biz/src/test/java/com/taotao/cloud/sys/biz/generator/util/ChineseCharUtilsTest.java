package com.taotao.cloud.sys.biz.generator.util;

import static com.taotao.cloud.sys.biz.support.generator.util.ChineseCharUtils.genFixedLengthChineseChars;
import static com.taotao.cloud.sys.biz.support.generator.util.ChineseCharUtils.genOneChineseChars;
import static com.taotao.cloud.sys.biz.support.generator.util.ChineseCharUtils.genRandomLengthChineseChars;
import static com.taotao.cloud.sys.biz.support.generator.util.ChineseCharUtils.getOneOddChar;
import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;
@Test
public class ChineseCharUtilsTest {
    public void testGenOneChineseChars() {
        final String result = genOneChineseChars();
        System.out.println(result);
        assertThat(result).hasSize(1);
    }

    public void testGenFixedLengthChineseChars() {
        final String result = genFixedLengthChineseChars(20);
        System.out.println(result);
        assertThat(result).hasSize(20);
    }

    public void testGenRandomLengthChineseChars() {
        final String result = genRandomLengthChineseChars(2, 10);
        System.out.println(result);
        assertThat(result).hasSizeBetween(2, 10);
    }

    public void testGetOneOddChar() {
        final char result = getOneOddChar();
        System.out.println(result);
        assertThat(result).isNotNull();
    }
}
