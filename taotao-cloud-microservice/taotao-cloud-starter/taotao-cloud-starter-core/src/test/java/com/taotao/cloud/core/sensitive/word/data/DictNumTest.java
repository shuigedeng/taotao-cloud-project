package com.taotao.cloud.core.sensitive.word.data;

import com.taotao.cloud.common.utils.io.FileUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 数据数据的格式统一化
 */
@Ignore
public class DictNumTest {

    /**
     * 统一格式
     *
     * 1. 将所有的大写字母统一转换为小写
     * 2. 将所有的全角转换为半角
     * 3. 移除所有【空格】【符号】(这个就是各种符号的过滤了)
     * 4. 繁体字统一转换为简体字
     */
    @Test
    @Ignore
    public void formatTest() {
        final String sourceFile = "D:\\_github\\sensitive-word\\src\\main\\resources\\dict.txt";
        final String targetFile = "D:\\_github\\sensitive-word\\src\\main\\resources\\dict.txt";

        List<String> words = FileUtils.readAllLines(sourceFile);
//        List<String> formats = CollectionUtil.toList(words, new IHandler<String, String>() {
//            @Override
//            public String handle(String string) {
//                // 数字的格式化统一处理
//                return NumUtils.getMappingString(string);
//            }
//        });

        List<String> resultList = DataUtil.disctinctAndSort(words);
        FileUtils.write(targetFile, resultList);
    }

}
