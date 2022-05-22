package com.taotao.cloud.core.sensitive.word.support.allow;


import com.taotao.cloud.common.utils.io.FileStreamUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordAllow;
import java.util.List;

/**
 * 系统默认的信息
 */
public class WordAllowSystem implements IWordAllow {

    @Override
    public List<String> allow() {
        return FileStreamUtil.readAllLines("/sensitive_word_allow.txt");
    }

}
