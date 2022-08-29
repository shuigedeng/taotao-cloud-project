package com.taotao.cloud.core.sensitive.word.support.allow;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.support.pipeline.Pipeline;
import com.taotao.cloud.common.utils.collection.ArrayUtils;
import com.taotao.cloud.core.sensitive.word.api.IWordAllow;

/**
 * 所有允许的结果
 */
public final class WordAllows {

    private WordAllows(){}

    /**
     * 责任链
     * @param wordAllow 允许
     * @param others 其他
     * @return 结果
     * @since 0.0.13
     */
    public static IWordAllow chains(final IWordAllow wordAllow,
                                    final IWordAllow... others) {
        return new WordAllowInit() {
            @Override
            protected void init(Pipeline<IWordAllow> pipeline) {
                pipeline.addLast(wordAllow);

                if(ArrayUtils.isNotEmpty(others)) {
                    for(IWordAllow other : others) {
                        pipeline.addLast(other);
                    }
                }
            }
        };
    }

    /**
     * 系统实现
     * @return 结果
     * @since 0.0.13
     */
    public static IWordAllow system() {
        return Instances.singleton(WordAllowSystem.class);
    }

}
