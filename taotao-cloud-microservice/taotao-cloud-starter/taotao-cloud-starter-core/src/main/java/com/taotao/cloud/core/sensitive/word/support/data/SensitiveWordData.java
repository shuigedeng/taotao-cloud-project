package com.taotao.cloud.core.sensitive.word.support.data;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.io.FileStreamUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.sensitive.word.api.IWordData;
import java.util.List;

/**
 * 数据加载使用单例的模式，只需要加载一次即可。
 *
 */
public class SensitiveWordData implements IWordData {

    /**
     * 默认的内置行
     *
     * @since 0.0.1
     */
    private static List<String> defaultLines;

    static {
        synchronized (SensitiveWordData.class) {
            long start = System.currentTimeMillis();
            defaultLines = Lists.newArrayList();
            defaultLines = FileStreamUtils.readAllLines("/dict.txt");
            defaultLines.addAll(FileStreamUtils.readAllLines("/dict_en.txt"));

            // 用户自定义
            List<String> denyList = FileStreamUtils.readAllLines("/sensitive_word_deny.txt");
            defaultLines.addAll(denyList);

            // 移除白名单词语
            List<String> allowList = FileStreamUtils.readAllLines("/sensitive_word_allow.txt");
            defaultLines = CollectionUtils.difference(defaultLines, allowList);

            long end = System.currentTimeMillis();
            LogUtils.info("Sensitive data loaded!, cost time: " + (end - start) + "ms");
        }
    }


    @Override
    public List<String> getWordData() {
        return defaultLines;
    }

}
