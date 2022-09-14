package com.taotao.cloud.sensitive.sensitive.word.support.deny;


import com.taotao.cloud.common.support.pipeline.Pipeline;
import com.taotao.cloud.common.support.pipeline.impl.DefaultPipeline;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordDeny;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化类
 *
 */
public abstract class WordDenyInit implements IWordDeny {

    /**
     * 初始化列表
     *
     * @param pipeline 当前列表泳道
     */
    protected abstract void init(final Pipeline<IWordDeny> pipeline);

    @Override
    public List<String> deny() {
        Pipeline<IWordDeny> pipeline = new DefaultPipeline<>();
        this.init(pipeline);

        List<String> results = new ArrayList<>();
        List<IWordDeny> wordDenies = pipeline.list();
        for (IWordDeny wordDeny : wordDenies) {
            List<String> denyList = wordDeny.deny();
            results.addAll(denyList);
        }

        return results;
    }

}
