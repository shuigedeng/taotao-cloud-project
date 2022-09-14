package com.taotao.cloud.sensitive.sensitive.word.define;


import com.taotao.cloud.core.sensitive.word.api.IWordAllow;

import java.util.Arrays;
import java.util.List;
public class MyWordAllow implements IWordAllow {

    @Override
    public List<String> allow() {
        return Arrays.asList("测试");
    }

}
