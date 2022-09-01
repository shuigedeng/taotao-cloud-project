package com.taotao.cloud.core.sensitive.word.define;

import com.taotao.cloud.core.sensitive.word.api.IWordAllow;
import com.taotao.cloud.core.sensitive.word.api.IWordDeny;
import com.taotao.cloud.core.sensitive.word.bs.SensitiveWordBs;
import com.taotao.cloud.core.sensitive.word.support.allow.WordAllows;
import com.taotao.cloud.core.sensitive.word.support.deny.WordDenys;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
public class SensitiveWordBsDefineTest {

    @Test
    public void defineDenyTest() {
        String text = "这是一个测试，我的自定义敏感词。";

        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .wordDeny(new MyWordDeny())
                .wordAllow(new MyWordAllow())
                .init();

        Assert.assertEquals("[我的自定义敏感词]", wordBs.findAll(text).toString());
    }

    @Test
    public void defineChainsTest() {
        String text = "这是一个测试。我的自定义敏感词。";

        IWordDeny wordDeny = WordDenys.chains(WordDenys.system(), new MyWordDeny());
        IWordAllow wordAllow = WordAllows.chains(WordAllows.system(), new MyWordAllow());

        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .wordDeny(wordDeny)
                .wordAllow(wordAllow)
                .init();

        Assert.assertEquals("[我的自定义敏感词]", wordBs.findAll(text).toString());
    }

}
