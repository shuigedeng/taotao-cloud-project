package com.taotao.cloud.core.pinyin.support.style;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.pinyin.spi.IPinyinToneStyle;

/**
 * 默认格式
 * <p> project: pinyin-IPinyinStyle </p>
 * <p> create on 2020/2/24 21:44 </p>
 *
 * @author Administrator
 * @since 0.1.1
 */
@ThreadSafe
public class DefaultPinyinToneStyle implements IPinyinToneStyle {

    @Override
    public String style(String charTone) {
        return charTone;
    }

}
