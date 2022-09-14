package com.taotao.cloud.sensitive.sensitive.word.spring.database;


import com.taotao.cloud.core.sensitive.word.api.IWordAllow;

import java.util.Arrays;
import java.util.List;

//@Component
public class MyDdWordAllow implements IWordAllow {

    @Override
    public List<String> allow() {
        // 数据库查询
        return Arrays.asList("学习");
    }

}
