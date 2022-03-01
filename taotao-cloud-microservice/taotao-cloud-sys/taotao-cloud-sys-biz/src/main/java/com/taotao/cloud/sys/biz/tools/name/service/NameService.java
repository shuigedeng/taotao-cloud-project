package com.taotao.cloud.sys.biz.tools.name.service;

import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NameService {

    // 分词器
    @Autowired(required = false)
    private List<TokenizerTool> tokenizerTools = new ArrayList<>();

    // 翻译工具, 包含业务翻译,英语翻译
    @Autowired(required = false)
    private List<Translate> translates = new ArrayList<>();

    // 后续处理工具
    @Autowired(required = false)
    private List<CharHandler> charHandlers = new ArrayList<>();

    private Map<String,EnglishTranslate> englishTranslateHashMap = new HashMap<>();

    @Autowired
    private BizTranslate bizTranslate;



    public NameService() {
    }

    @Autowired(required = false)
    public NameService(List<EnglishTranslate> englishTranslates) {
        if (CollectionUtils.isNotEmpty(englishTranslates)){
            for (EnglishTranslate englishTranslate : englishTranslates) {
                String name = ((ToolName) englishTranslate).getName();
                englishTranslateHashMap.put(name,englishTranslate);
            }
        }
    }

    /**
     * 变量或方法取名
     * @param orginChars
     * @param tokenizer
     * @param bizs 业务翻译列表
     * @param translates 英语翻译列表
     * @return
     */
    public Set<String> translate(String orginChars, String tokenizer, String [] bizs,String [] translates){
        TranslateCharSequence translateCharSequence = new TranslateCharSequence(orginChars);

        // 先找到使用的分词器,进行分词
        firstTokenizer(tokenizer, translateCharSequence);

        // 然后使用翻译工具进行翻译; 找到需要使用的翻译工具; 这里包含业务词和通用词,最后才是英语翻译
        secondTranslate(bizs,translates, translateCharSequence);

        // 词拼接及后续处理
        thridCharHandlerAndMerge(translateCharSequence);

        // 得出结论
        Set<String> results = translateCharSequence.results();

        return results;
    }

    /**
     * 获取所有的英语翻译工具
     * @return
     */
    public List<String> englishTranslate(){
        List<String> result = new ArrayList<>();
        for (Translate translate : translates) {
            if (translate instanceof EnglishTranslate) {
                result.add(translate.getName());
            }
        }
        return result;
    }

    /**
     * 第三步,使用其它处理器进行后续处理
     * @param translateCharSequence
     */
    private void thridCharHandlerAndMerge(TranslateCharSequence translateCharSequence) {
        if(CollectionUtils.isNotEmpty(charHandlers)) {
            for (CharHandler charHandler : charHandlers) {
                charHandler.handler(translateCharSequence);
            }
        }
    }

    /**
     * 第二步,使用业务或英语翻译器进行翻译
     * @param translates
     * @param translateCharSequence
     */
    private void secondTranslate(String [] bizs,String [] translates, TranslateCharSequence translateCharSequence) {
        List<Translate> findtranslates = new ArrayList<>();
        for (Translate translate : this.translates) {
            if (ArrayUtils.contains(translates,translate.getName())){
                findtranslates.add(translate);
            }
        }

        // 先用业务工具进行翻译
        bizTranslate.doTranslate(bizs,translateCharSequence);

        // 用找到的翻译工具做翻译
        for (Translate translate : findtranslates) {
            translate.doTranslate(translateCharSequence);

            // 如果是英语翻译工具,再增加直译
            if (translate instanceof  EnglishTranslate){
                EnglishTranslate englishTranslate = (EnglishTranslate) translate;
                Set<String> results = englishTranslate.directTranslate(translateCharSequence.getOriginSequence().toString());
                for (String result : results) {
                    translateCharSequence.addDirectTranslate(result);
                }
            }
        }
    }

    /**
     * 第一步,使用分词工具进行分词
     * @param tokenizerToolName
     * @param translateCharSequence
     */
    private void firstTokenizer(String tokenizerToolName, TranslateCharSequence translateCharSequence) {
        if (CollectionUtils.isEmpty(tokenizerTools)){
            throw new ToolException("未找到可用的分词器");
        }
        TokenizerTool findTokenizerTool = null;
        for (TokenizerTool tokenizerTool : tokenizerTools) {
            String name = tokenizerTool.getName();
            if (name.equals(tokenizerToolName)){
                findTokenizerTool = tokenizerTool;
                break;
            }
        }
        if (findTokenizerTool == null){
            throw new ToolException("找不到分词器:"+tokenizerToolName);
        }
        findTokenizerTool.doTokenizer(translateCharSequence);
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("call").name(BizTranslate.MODULE).author("9420").logo("translate.gif").desc("快速对方法或变量取名").build());
//    }

    /**
     * 获取所有的分词器
     * @return
     */
    public List<String> tokenizers() {
        return tokenizerTools.stream().map(ToolName::getName).collect(Collectors.toList());
    }

    /**
     * 多列翻译
     * @param words
     * @param english
     * @return
     */
    public List<String> multiTranslate(String[] words, String english) {
        List<String> results = new ArrayList<>();
        EnglishTranslate englishTranslate = englishTranslateHashMap.get(english);
        for (String word : words) {
            Set<String> wordResult = englishTranslate.directTranslate(word);
            results.addAll(wordResult);
        }
        return results;
    }
}
