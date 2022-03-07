package com.taotao.cloud.core.sensitive.word.support.allow;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.io.StreamUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordAllow;
import java.util.List;

/**
 * 系统默认的信息
 */
@ThreadSafe
public class WordAllowSystem implements IWordAllow {

    @Override
    public List<String> allow() {
        return StreamUtil.readAllLines("/sensitive_word_allow.txt");
    }

}
