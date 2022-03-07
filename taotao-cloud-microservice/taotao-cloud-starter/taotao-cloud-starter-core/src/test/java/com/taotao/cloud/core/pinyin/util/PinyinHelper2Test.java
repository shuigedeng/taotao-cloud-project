package com.taotao.cloud.core.pinyin.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class PinyinHelper2Test {

    /**
     * 《施氏食狮史》
     *
     * @since 0.3.0
     */
    @Test
    public void toPinyinTest() {
        String pinyin = PinyinHelper.toPinyin("石室诗士施氏，嗜狮，誓食十狮。施氏时时适市视狮。十时，适十狮适市。是时，适施氏适市。施氏视是十狮，恃矢势，使是十狮逝世。氏拾是十狮尸，适石室。石室湿，氏使侍拭石室。石室拭，氏始试食是十狮尸。食时，始识是十狮尸，实十石狮尸。试释是事。");
        System.out.println(pinyin);
    }

}
