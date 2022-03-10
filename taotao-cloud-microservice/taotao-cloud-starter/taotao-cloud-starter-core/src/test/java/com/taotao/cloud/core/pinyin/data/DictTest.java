package com.taotao.cloud.core.pinyin.data;

import com.taotao.cloud.core.heaven.support.condition.ICondition;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import com.taotao.cloud.core.heaven.util.io.FileUtil;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 词典生成辅助类
 * @author binbin.hou
 * @since 0.0.1
 */
@Ignore
public class DictTest {

    @Test
    public void charTest() {
        List<String> oldList = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\pinyin_char_o.txt");

        List<String> resultList = CollectionUtil.toList(oldList, new IHandler<String, String>() {
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
        List<String> simpleList = CollectionUtil.conditionList(resultList, new ICondition<String>() {
            @Override
            public boolean condition(String string) {
                // 是否为简体
                return StringUtil.isNotEmpty(string);
            }
        });

        simpleList = CollectionUtil.distinctAndSort(simpleList);

        final String target = "D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_char.txt";
        FileUtil.write(target, simpleList);
    }

    @Test
    public void showAllToneList() {
        List<String> lines = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_char.txt");

        List<String> characters = new ArrayList<>();

        for(String string : lines) {
            String[] strings = string.split(":");

            String tones = strings[1];

            characters.add(tones.replaceAll("[a-z,]", ""));
        }

        characters = CollectionUtil.distinctAndSort(characters);

        for(String c : characters) {
            System.out.println(c);
        }
    }

    @Test
    public void commonToneSortTest() {
        List<String> commonList = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\common_tone_num.txt");

        System.out.println(CollectionUtil.sort(commonList));
    }

    @Test
    public void filterSpecialTest() {
        List<String> allToneList = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\tone\\all.txt");
        List<String> knowToneList = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\tone\\all.txt");

        List<String> resultList = new ArrayList<>();

        for(String all : allToneList) {
            String result = all;

            for(String know : knowToneList) {
                // 移除
                result = result.replaceAll(know, "");
            }

            if(StringUtil.isNotEmpty(result)) {
                resultList.add(result);
            }
        }

        resultList = CollectionUtil.distinctAndSort(resultList);
        FileUtil.write("D:\\_github\\pinyin\\src\\test\\resources\\tone\\sepcial.txt",
                resultList);

    }

    @Test
    public void toneListTest() {
        List<String> commonList = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\test\\resources\\common_tone_num.txt");

        commonList = CollectionUtil.distinctAndSort(commonList);

        FileUtil.write("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_tone.txt", commonList);
    }

    @Test
    public void defineDictTest() {
        List<String> allLines = FileUtil.readAllLines("D:\\_github\\pinyin\\src\\main\\resources\\pinyin_dict_phrase.txt");

        List<String> resultList = CollectionUtil.toList(allLines, new IHandler<String, String>() {
            @Override
            public String handle(String s) {
                String[] strings = s.split(":");
                return strings[0];
            }
        });

        resultList = CollectionUtil.distinctAndSort(resultList);
        FileUtil.write("D:\\_github\\pinyin\\src\\main\\resources\\segment_define_dict.txt", resultList);
    }

}
