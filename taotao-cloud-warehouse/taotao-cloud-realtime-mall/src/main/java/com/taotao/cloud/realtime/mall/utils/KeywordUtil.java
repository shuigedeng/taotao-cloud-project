package com.taotao.cloud.realtime.mall.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 *
 * Date: 2021/2/26
 * Desc: IK分词器分词工具类
 */
public class KeywordUtil {
    //分词    将字符串进行分词，将分词之后的结果放到一个集合中返回
    public static List<String> analyze(String text){
        List<String> wordList = new ArrayList<>();
        //将字符串转换为字符输入流
        StringReader sr = new StringReader(text);
        //创建分词器对象
        IKSegmenter ikSegmenter = new IKSegmenter(sr, true);
        // Lexeme  是分词后的一个单词对象
        Lexeme lexeme = null;
        //通过循环，获取分词后的数据
        while(true){
            try {
                //获取一个单词
                if((lexeme = ikSegmenter.next())!=null){
                    String word = lexeme.getLexemeText();
                    wordList.add(word);
                }else{
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wordList;
    }

    public static void main(String[] args) {
        String text = "尚硅谷大数据数仓";
        System.out.println(KeywordUtil.analyze(text));

    }
}
