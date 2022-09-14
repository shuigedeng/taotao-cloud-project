package com.taotao.cloud.sensitive.sensitive.word.spring.database;


import com.taotao.cloud.core.sensitive.word.api.IWordDeny;

import java.util.Arrays;
import java.util.List;

//@Component
public class MyDdWordDeny implements IWordDeny {

    @Override
    public List<String> deny() {
        // 数据库返回的各种信息
        return Arrays.asList("广告");
    }

}
