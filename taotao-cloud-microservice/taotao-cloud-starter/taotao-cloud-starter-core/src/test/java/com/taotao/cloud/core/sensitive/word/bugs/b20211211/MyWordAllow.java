package com.taotao.cloud.core.sensitive.word.bugs.b20211211;


import com.taotao.cloud.core.sensitive.word.api.IWordAllow;
import java.util.Arrays;
import java.util.List;

public class MyWordAllow implements IWordAllow {

    @Override
    public List<String> allow() {
        return Arrays.asList("五星红旗");
    }

}
