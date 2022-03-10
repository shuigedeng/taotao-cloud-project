package com.taotao.cloud.common.utils.pinyin.support.chinese;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinChinese;

public final class PinyinChineses {

    private PinyinChineses(){}

    /**
     * 默认实现
     * @return 简单实现
     * @since 0.2.2
     */
    public static IPinyinChinese defaults() {
        return Instances.singleton(DefaultsPinyinChinese.class);
    }

}
