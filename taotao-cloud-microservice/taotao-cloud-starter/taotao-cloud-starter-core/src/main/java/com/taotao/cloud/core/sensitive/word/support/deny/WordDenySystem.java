package com.taotao.cloud.core.sensitive.word.support.deny;


import com.taotao.cloud.common.utils.io.FileStreamUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordDeny;
import java.util.List;

/**
 * 系统默认的信息
 */
public class WordDenySystem implements IWordDeny {

    @Override
    public List<String> deny() {
        List<String> results = FileStreamUtil.readAllLines("/dict.txt");
        results.addAll(FileStreamUtil.readAllLines("/dict_en.txt"));
        results.addAll(FileStreamUtil.readAllLines("/sensitive_word_deny.txt"));
        return results;
    }

}
