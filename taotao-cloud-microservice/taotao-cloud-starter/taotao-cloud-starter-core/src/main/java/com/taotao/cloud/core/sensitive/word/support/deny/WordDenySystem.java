package com.taotao.cloud.core.sensitive.word.support.deny;


import com.taotao.cloud.common.utils.io.StreamUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordDeny;
import java.util.List;

/**
 * 系统默认的信息
 */
public class WordDenySystem implements IWordDeny {

    @Override
    public List<String> deny() {
        List<String> results = StreamUtil.readAllLines("/dict.txt");
        results.addAll(StreamUtil.readAllLines("/dict_en.txt"));
        results.addAll(StreamUtil.readAllLines("/sensitive_word_deny.txt"));
        return results;
    }

}
