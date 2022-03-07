package com.taotao.cloud.core.pinyin.bugs;

import com.taotao.cloud.core.pinyin.util.PinyinHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.3.1
 */
public class Bug16Test {

    @Test
    public void failedTest() {
        Assert.assertEquals("[jí]", PinyinHelper.toPinyinList('䳭').toString());
        Assert.assertEquals("pì tī", PinyinHelper.toPinyin("䴙䴘"));
        Assert.assertEquals("fèng tóu pì tī", PinyinHelper.toPinyin("凤头䴙䴘"));
        Assert.assertEquals("hēi hóu shí jí", PinyinHelper.toPinyin("黑喉石䳭"));
    }

}
