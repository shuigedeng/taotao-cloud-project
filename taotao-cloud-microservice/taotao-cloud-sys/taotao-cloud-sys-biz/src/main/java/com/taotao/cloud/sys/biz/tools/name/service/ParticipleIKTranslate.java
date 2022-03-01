package com.taotao.cloud.sys.biz.tools.name.service;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 分词器,IK 分词
 */
@Service
public class ParticipleIKTranslate implements TokenizerTool {
    private Analyzer ikAnalyzer = new IKAnalyzer(true); // true　用智能分词，false细粒度

    /**
     * cfg 使用时初始化, 因为太耗时了
     */
    private volatile Configuration cfg = null;

    @Override
    public void doTokenizer(TranslateCharSequence translateCharSequence) {
        if (cfg == null){
            synchronized (ParticipleIKTranslate.class){
                if (cfg == null) {
                    cfg = DefaultConfig.getInstance();
                    Dictionary.initial(cfg);
                }
            }
        }

        StringReader reader = new StringReader(translateCharSequence.getOriginSequence().toString());
        // 分词
        TokenStream tokenStream = null;
        try {
            tokenStream = ikAnalyzer.tokenStream("", reader);
            CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
            // 遍历分词数据
            tokenStream.reset();
            List<String> segment = new ArrayList<String>();
            while (tokenStream.incrementToken()) {
                segment.add(term.toString());
            }
            translateCharSequence.addSegment(segment);

            //初始化翻译
            translateCharSequence.initCharMaps();

        } catch (IOException e) {
            log.error("ParticipleIKTranslate doTokenizer error : {}",e.getMessage(),e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public String getName() {
        return "ik";
    }
}
