package com.taotao.cloud.sensitive.sensitive.word.support.allow;


import com.taotao.cloud.common.utils.io.FileStreamUtils;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordAllow;

import java.util.List;

/**
 * 系统默认的信息
 */
public class WordAllowSystem implements IWordAllow {

    @Override
    public List<String> allow() {
        return FileStreamUtils.readAllLines("/sensitive_word_allow.txt");
    }

}
