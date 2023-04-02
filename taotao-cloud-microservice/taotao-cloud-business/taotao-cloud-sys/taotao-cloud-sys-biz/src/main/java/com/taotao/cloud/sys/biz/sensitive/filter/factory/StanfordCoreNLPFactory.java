package com.taotao.cloud.sys.biz.sensitive.filter.factory;

import org.springframework.stereotype.Component;

import com.daffodil.sensitive.filter.properties.SensitiveWordProperties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * 
 * @author yweijian
 * @date 2022年9月19日
 * @version 2.0.0
 * @description
 */
@Component
public class StanfordCoreNLPFactory {

    private final StanfordCoreNLP stanfordCoreNLP;
    
    public StanfordCoreNLPFactory(SensitiveWordProperties sensitiveWordProperties) {
        super();
        if(sensitiveWordProperties.getEnable()) {
            this.stanfordCoreNLP = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
        }else {
            this.stanfordCoreNLP = null;
        }
    }
    
    public StanfordCoreNLP getStanfordCoreNLP() {
        return this.stanfordCoreNLP;
    }
    
}
