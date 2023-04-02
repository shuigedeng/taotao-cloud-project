package com.taotao.cloud.sys.biz.sensitive.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daffodil.core.util.HqlUtils;
import com.daffodil.core.dao.JpaDao;
import com.daffodil.core.entity.Query;
import com.daffodil.sensitive.service.ISysSensitiveWordsService;
import com.daffodil.sensitive.constant.SensitiveConstant;
import com.daffodil.sensitive.controller.model.FilterText;
import com.daffodil.sensitive.controller.model.FilterWords;
import com.daffodil.sensitive.entity.SysSensitiveWords;
import com.daffodil.sensitive.filter.factory.StanfordCoreNLPFactory;
import com.daffodil.util.StringUtils;
import com.daffodil.util.hash.BloomFilterRedis;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * -敏感词词语Service接口业务实现层
 * @author yweijian
 * @date 2022-09-16
 * @version 1.0
 * @description
 */
@Service
public class SysSensitiveWordsServiceImpl implements ISysSensitiveWordsService {

    @Autowired
    private JpaDao<String> jpaDao;
    
    @Autowired
    private BloomFilterRedis<String> bloomFilter;
    
    @Autowired
    private StanfordCoreNLPFactory stanfordCoreNLPFactory;
	
    @Override
    public List<SysSensitiveWords> selectSensitiveWordsList(Query<SysSensitiveWords> query) {
        StringBuffer hql = new StringBuffer("from SysSensitiveWords where 1=1 ");
        List<Object> paras = new ArrayList<Object>();
        HqlUtils.createHql(hql, paras, query);
        return jpaDao.search(hql.toString(), paras, SysSensitiveWords.class, query.getPage());
    }
    
    @Override
    public SysSensitiveWords selectSensitiveWordsById(String id) {
        return jpaDao.find(SysSensitiveWords.class, id);
    }

    @Override
    @Transactional
    public void insertSensitiveWords(SysSensitiveWords words) {
        jpaDao.save(words);
        bloomFilter.put(SensitiveConstant.SENSITIVE_WORD, words.getWords());
    }

    @Override
    @Transactional
    public void updateSensitiveWords(SysSensitiveWords words) {
        jpaDao.update(words);
        bloomFilter.put(SensitiveConstant.SENSITIVE_WORD, words.getWords());
    }

    @Override
    @Transactional
    public void deleteSensitiveWordsByIds(String[] ids) {
        jpaDao.delete(SysSensitiveWords.class, ids);
    }

    @Override
    public int countSensitiveWords() {
        return jpaDao.count("from SysSensitiveWords");
    }

    @Override
    public FilterWords checkSensitiveWords(FilterText filterText) {
        return this.filterSensitiveWords(filterText, true);
    }

    @Override
    public FilterWords matchSensitiveWords(FilterText filterText) {
        return this.filterSensitiveWords(filterText, false);
    }
    
    @Override
    public String maskSensitiveWords(FilterText filterText) {
        FilterWords filterWords = this.filterSensitiveWords(filterText, false);
        String text = filterText.getText();
        if(filterWords.getIsMatched() && StringUtils.isNotEmpty(text)) {
            for(String word : filterWords.getWords()) {
                text = text.replaceAll(word, this.getSensitiveWordsStar(word));
            }
        }
        return text;
    }
    
    private String getSensitiveWordsStar(String word) {
        String star = "";
        if(StringUtils.isNotEmpty(word)) {
            for(int i = 0; i< word.length(); i++) {
                star += "*";
            }
        }
        return star;
    }
    
    /**
     * -检查匹配文本text中敏感词
     * @param filterText 检查文本
     * @param immediately 匹配到是否立即返回
     * @return
     */
    private FilterWords filterSensitiveWords(FilterText filterText, Boolean immediately) {
        FilterWords filterWords = new FilterWords();
        if(StringUtils.isNull(filterText) || StringUtils.isEmpty(filterText.getText())) {
            return filterWords;
        }
        StanfordCoreNLP pipeline = stanfordCoreNLPFactory.getStanfordCoreNLP();
        if(StringUtils.isNull(pipeline)) {
            return filterWords;
        }
        CoreDocument document = pipeline.processToCoreDocument(filterText.getText());
        List<CoreSentence> sentences = document.sentences();
        if(StringUtils.isEmpty(sentences)) {
            return filterWords;
        }
        for(int i = 0; i < sentences.size(); i++) {
            CoreSentence sentence = sentences.get(i);
            List<String> nouns = sentence.nounPhrases();
            for(int j = 0; j < nouns.size(); j++) {
                String word = nouns.get(j);
                if(bloomFilter.mightContain(SensitiveConstant.SENSITIVE_WORD, word)) {
                    filterWords.addWord(word);
                    if(immediately) {
                        return filterWords;
                    }
                }
            }
        }
        return filterWords;
    }

}
