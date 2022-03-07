package com.taotao.cloud.core.pinyin.support.tone;

import com.taotao.cloud.core.heaven.support.instance.impl.Instances;
import com.taotao.cloud.core.pinyin.spi.IPinyinTone;

/**
 * <p> project: pinyin-PinyinTones </p>
 * <p> create on 2020/2/24 22:18 </p>
 *
 * @author Administrator
 * @since 0.1.1
 */
public final class PinyinTones {

    private PinyinTones(){}

    /**
     * 默认实现
     * @return 默认实现
     * @since 0.1.1
     */
    public static IPinyinTone defaults() {
        return Instances.singleton(DefaultPinyinTone.class);
    }

}
