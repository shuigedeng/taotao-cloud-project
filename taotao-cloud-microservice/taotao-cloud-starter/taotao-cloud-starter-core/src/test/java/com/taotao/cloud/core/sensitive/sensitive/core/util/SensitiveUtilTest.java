package com.taotao.cloud.core.sensitive.sensitive.core.util;

import com.taotao.cloud.core.sensitive.sensitive.core.api.SensitiveUtil;
import com.taotao.cloud.core.sensitive.sensitive.model.bugs.Father;
import org.junit.Test;

/**
 * BUGS 5
 * @author binbin.hou
 * @since 0.0.11
 */
public class SensitiveUtilTest {

    @Test
    public void bugs5NpeTest() {
        Father father = new Father();

        Father copy = SensitiveUtil.desCopy(father);
        System.out.println(copy);
    }

}
