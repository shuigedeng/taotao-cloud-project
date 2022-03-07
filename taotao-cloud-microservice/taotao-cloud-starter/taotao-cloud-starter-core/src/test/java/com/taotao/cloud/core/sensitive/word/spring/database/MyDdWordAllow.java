package com.taotao.cloud.core.sensitive.word.spring.database;


import com.taotao.cloud.core.sensitive.word.api.IWordAllow;
import java.util.Arrays;
import java.util.List;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
@Component
public class MyDdWordAllow implements IWordAllow {

    @Override
    public List<String> allow() {
        // 数据库查询
        return Arrays.asList("学习");
    }

}
