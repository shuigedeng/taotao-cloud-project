package com.taotao.cloud.core.sensitive.word.support.format;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.lang.CharUtil;
import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;

/**
 * 格式化责任链
 */
@ThreadSafe
public class IgnoreWidthCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return CharUtil.toHalfWidth(original);
    }

}
