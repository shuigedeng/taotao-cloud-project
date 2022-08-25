package com.taotao.cloud.pinyin.bugs;

import com.taotao.cloud.pinyin.util.PinyinHelper;
import org.junit.Assert;
import org.junit.Test;

public class Bug16Test {

    @Test
    public void failedTest() {
        Assert.assertEquals("[jí]", PinyinHelper.toPinyinList('䳭').toString());
        Assert.assertEquals("pì tī", PinyinHelper.toPinyin("䴙䴘"));
        Assert.assertEquals("fèng tóu pì tī", PinyinHelper.toPinyin("凤头䴙䴘"));
        Assert.assertEquals("hēi hóu shí jí", PinyinHelper.toPinyin("黑喉石䳭"));
    }

}
