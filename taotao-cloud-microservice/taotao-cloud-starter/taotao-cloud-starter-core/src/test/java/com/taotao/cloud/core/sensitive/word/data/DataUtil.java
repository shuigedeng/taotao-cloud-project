package com.taotao.cloud.core.sensitive.word.data;

import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataUtil {

    /**
     * 获取对应文件的独一无二内容
     * @param name 名称
     * @return 结果
     */
    public static List<String> distinctLines(final String name) {
        final String dir = "D:\\github\\sensitive-word\\src\\main\\resources\\";
        final String path = dir + name;
        List<String> lines = FileUtils.readAllLines(path);
        return CollectionUtils.distinct(lines);
    }

    public static List<String> disctinctAndSort(final Collection<String> collection) {
        List<String> stringList = CollectionUtils.distinct(collection);
        Collections.sort(stringList);

        return stringList;
    }

    @Test
    @Ignore
    public void singleCharTest() {
        final String path = "D:\\github\\sensitive-word\\src\\main\\resources\\dict.txt";

        List<String> stringList = FileUtils.readAllLines(path);
        for(String s : stringList) {
            if(s.length() == 1) {
                System.out.println(s);
            }
        }
    }

}
