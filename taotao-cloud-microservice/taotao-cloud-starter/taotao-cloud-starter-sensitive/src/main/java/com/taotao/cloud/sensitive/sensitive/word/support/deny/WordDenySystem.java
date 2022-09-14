package com.taotao.cloud.sensitive.sensitive.word.support.deny;


import com.taotao.cloud.common.utils.io.FileStreamUtils;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordDeny;

import java.util.List;

/**
 * 系统默认的信息
 */
public class WordDenySystem implements IWordDeny {

    @Override
    public List<String> deny() {
        List<String> results = FileStreamUtils.readAllLines("/dict.txt");
        results.addAll(FileStreamUtils.readAllLines("/dict_en.txt"));
        results.addAll(FileStreamUtils.readAllLines("/sensitive_word_deny.txt"));
        return results;
    }

}
