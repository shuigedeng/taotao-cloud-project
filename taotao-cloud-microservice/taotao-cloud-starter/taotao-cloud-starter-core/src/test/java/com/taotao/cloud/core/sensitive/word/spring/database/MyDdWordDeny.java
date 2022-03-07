package com.taotao.cloud.core.sensitive.word.spring.database;


import com.taotao.cloud.core.sensitive.word.api.IWordDeny;
import java.util.Arrays;
import java.util.List;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
@Component
public class MyDdWordDeny implements IWordDeny {

    @Override
    public List<String> deny() {
        // 数据库返回的各种信息
        return Arrays.asList("广告");
    }

}
