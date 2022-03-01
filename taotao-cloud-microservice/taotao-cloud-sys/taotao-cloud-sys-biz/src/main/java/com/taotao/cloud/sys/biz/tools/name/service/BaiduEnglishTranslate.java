package com.taotao.cloud.sys.biz.tools.name.service;

import com.taotao.cloud.sys.biz.tools.name.remote.apis.TranslateApi;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.BaiduTranslateRequest;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.BaiduTranslateResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BaiduEnglishTranslate extends EnglishTranslate implements Translate {

    @Autowired
    private TranslateApi translateApi;

    @Value("${translate.baidu.appId}")
    private String appId;
    @Value("${translate.baidu.secret}")
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
        BaiduTranslateRequest baiduTranslateRequest = new BaiduTranslateRequest(needTranslateWord,appId,secret);
        BaiduTranslateResponse baiduTranslateResponse = translateApi.baiduTranslate(baiduTranslateRequest);
        String errorCode = baiduTranslateResponse.getError_code();
        if (StringUtils.isBlank(errorCode)) {
            List<BaiduTranslateResponse.TranslateResult> transResult = baiduTranslateResponse.getTrans_result();
            Set<String> collect = transResult.stream().map(BaiduTranslateResponse.TranslateResult::getDst).collect(Collectors.toSet());
            return collect;
        }
        log.error("百度翻译失败[{} | {}],使用原词[{}]代替",baiduTranslateResponse.getError_code(),baiduTranslateResponse.getError_msg(),needTranslateWord);

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
        return "baidu";
    }
}
