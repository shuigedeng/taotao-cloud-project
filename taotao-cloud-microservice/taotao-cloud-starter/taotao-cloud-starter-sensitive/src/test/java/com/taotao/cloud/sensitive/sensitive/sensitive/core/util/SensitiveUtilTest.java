package com.taotao.cloud.sensitive.sensitive.sensitive.core.util;

import com.taotao.cloud.sensitive.sensitive.sensitive.core.api.SensitiveUtil;
import com.taotao.cloud.sensitive.sensitive.sensitive.model.bugs.Father;
import org.junit.jupiter.api.Test;


public class SensitiveUtilTest {

    @Test
    public void bugs5NpeTest() {
        Father father = new Father();

        Father copy = SensitiveUtil.desCopy(father);
        System.out.println(copy);
    }

}
