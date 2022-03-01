package com.taotao.cloud.sys.biz.tools.name.service;

import com.taotao.cloud.sys.biz.tools.name.remote.apis.TranslateApi;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.YoudaoTranslateRequest;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.YoudaoTranslateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class YoudaoEnglishTranslate extends EnglishTranslate implements  Translate {

    @Autowired
    private TranslateApi translateApi;

    @Value("${translate.youdao.appId}")
    private String appId;
    @Value("${translate.youdao.secret}")
    private String secret;

    @Override
    public void doTranslate(TranslateCharSequence translateCharSequence) {
        Set<String> needTranslateWords = translateCharSequence.getNeedTranslateWords();
        if(CollectionUtils.isEmpty(needTranslateWords)){return ;}

        for (String needTranslateWord : needTranslateWords) {
            Set<String> words = translateWord(needTranslateWord);
            for (String word : words) {
                translateCharSequence.addTranslate(needTranslateWord,word);
            }
        }

        //添加直译结果
//        Set<String> results = directTranslate(translateCharSequence.getOriginSequence().toString());
//        for (String result : results) {
//            translateCharSequence.addDirectTranslate(result);
//        }
    }

    /**
     * 单个词的翻译结果
     * @param needTranslateWord
     * @param translateCharSequence
     */
    private Set<String> translateWord(String needTranslateWord) {
        YoudaoTranslateRequest baiduTranslateRequest = new YoudaoTranslateRequest(needTranslateWord,appId,secret);
        YoudaoTranslateResponse youdaoTranslateResponse = translateApi.youdaoTranslate(baiduTranslateRequest);
        String errorCode = youdaoTranslateResponse.getErrorCode();
        if ("0".equals(errorCode)) {
            Set<String> translation = youdaoTranslateResponse.getTranslation();
            return translation;
        }
        log.error("有道翻译失败[{}],使用原词[{}]代替",errorCode,needTranslateWord);

        HashSet<String> words = new HashSet<>();
        words.add(needTranslateWord);
        return words;
    }

    @Override
    public Set<String> innerDirectTranslate(String source) {
        Set<String> translate = translateWord(source);
        return translate;
    }

    @Override
    public String getName() {
        return "youdao";
    }
}
