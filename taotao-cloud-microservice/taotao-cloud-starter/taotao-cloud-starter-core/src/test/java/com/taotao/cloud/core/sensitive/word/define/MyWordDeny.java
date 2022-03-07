package com.taotao.cloud.core.sensitive.word.define;


import com.taotao.cloud.core.sensitive.word.api.IWordDeny;
import java.util.Arrays;
import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.14
 */
public class MyWordDeny implements IWordDeny {

    @Override
    public List<String> deny() {
        return Arrays.asList("我的自定义敏感词");
    }

}
