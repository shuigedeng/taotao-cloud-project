package com.taotao.cloud.pinyin.data;

import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.io.FileUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 词典生成辅助类
 */
@Ignore
public class DictTest {

    @Test
    public void charTest() {
        List<String> oldList = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\pinyin_char_o.txt");

        List<String> resultList = CollectionUtils.toList(oldList, new IHandler<String, String>() {
            //U+3007: líng,yuán,xīng  # 〇
            // 〇 líng,yuán,xīng
            @Override
            public String handle(String string) {
                String[] strings = string.split("#");
                String word = strings[1].trim();

//                if(ZhConvertBootstrap.newInstance().isTraditional(word)) {
//                    return "";
//                }
                String tone = strings[0].trim().split(":")[1].trim();
                return word + " " + tone;
            }
        });

        // 只保留简体
        List<String> simpleList = CollectionUtils.conditionList(resultList, new ICondition<String>() {
            @Override
            public boolean condition(String string) {
                // 是否为简体
                return StringUtils.isNotEmpty(string);
            }
        });

        simpleList = CollectionUtils.distinctAndSort(simpleList);

        final String target = "D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_char.txt";
        FileUtils.write(target, simpleList);
    }

    @Test
    public void showAllToneList() {
        List<String> lines = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_char.txt");

        List<String> characters = new ArrayList<>();

        for(String string : lines) {
            String[] strings = string.split(":");

            String tones = strings[1];

            characters.add(tones.replaceAll("[a-z,]", ""));
        }

        characters = CollectionUtils.distinctAndSort(characters);

        for(String c : characters) {
            System.out.println(c);
        }
    }

    @Test
    public void commonToneSortTest() {
        List<String> commonList = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\common_tone_num.txt");

        System.out.println(CollectionUtils.sort(commonList));
    }

    @Test
    public void filterSpecialTest() {
        List<String> allToneList = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\tone\\all.txt");
        List<String> knowToneList = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\tone\\all.txt");

        List<String> resultList = new ArrayList<>();

        for(String all : allToneList) {
            String result = all;

            for(String know : knowToneList) {
                // 移除
                result = result.replaceAll(know, "");
            }

            if(StringUtils.isNotEmpty(result)) {
                resultList.add(result);
            }
        }

        resultList = CollectionUtils.distinctAndSort(resultList);
        FileUtils.write("D:\\_github\\pinyin\\src\\test\\resources\\tone\\sepcial.txt",
                resultList);

    }

    @Test
    public void toneListTest() {
        List<String> commonList = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\common_tone_num.txt");

        commonList = CollectionUtils.distinctAndSort(commonList);

        FileUtils.write("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_tone.txt", commonList);
    }

    @Test
    public void defineDictTest() {
        List<String> allLines = FileUtils.readAllLines("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_phrase.txt");

        List<String> resultList = CollectionUtils.toList(allLines, new IHandler<String, String>() {
            @Override
            public String handle(String s) {
                String[] strings = s.split(":");
                return strings[0];
            }
        });

        resultList = CollectionUtils.distinctAndSort(resultList);
        FileUtils.write("D:\\_github\\pinyin\\src\\main\\resources\\segment_define_dict.txt", resultList);
    }

}
