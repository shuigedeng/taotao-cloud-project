package com.taotao.cloud.pinyin.util;

import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.pinyin.constant.enums.PinyinStyleEnum;
import org.junit.Assert;
import org.junit.Test;

public class PinyinHelperConnectorTest {

    /**
     * 首字母用空字符串连接
     */
    @Test
    public void firstLetterEmptyTest() {
        final String text = "我爱中文";

        Assert.assertEquals("wazw", PinyinHelper.toPinyin(text, PinyinStyleEnum.FIRST_LETTER, StringUtils.EMPTY));
    }

    @Test
    public void baseCaseTest() {
        final String text = "这个是测试";
        Assert.assertEquals("zgscs", PinyinHelper.toPinyin(text,
                PinyinStyleEnum.FIRST_LETTER, StringUtils.EMPTY));
    }

}
