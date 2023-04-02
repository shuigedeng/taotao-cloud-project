package com.taotao.cloud.sys.biz.sensitive.service;

import java.util.List;

import com.daffodil.core.entity.Query;
import com.daffodil.sensitive.controller.model.FilterText;
import com.daffodil.sensitive.controller.model.FilterWords;
import com.daffodil.sensitive.entity.SysSensitiveWords;

/**
 * -敏感词词语Service接口
 * @author yweijian
 * @date 2022-09-16
 * @version 1.0
 * @description
 */
public interface ISysSensitiveWordsService {
    
    /**
     * -分页查询敏感词词语列表
     * @param query 敏感词词语
     * @return 敏感词词语
     */
    public List<SysSensitiveWords> selectSensitiveWordsList(Query<SysSensitiveWords> query);
    
    /**
     * -查询敏感词词语
     * @param id 敏感词词语ID
     * @return 敏感词词语
     */
    public SysSensitiveWords selectSensitiveWordsById(String id);

    /**
     * -新增敏感词词语
     * @param words
     */
    public void insertSensitiveWords(SysSensitiveWords words);

    /**
     * -修改敏感词词语
     * @param words
     */
    public void updateSensitiveWords(SysSensitiveWords words);
    
    /**
     * -删除敏感词词语
     * @param ids
     */
    public void deleteSensitiveWordsByIds(String[] ids);

    /**
     * -统计敏感词词语总数
     */
    public int countSensitiveWords();

    /**
     * -检测文本敏感词词语
     * @param filterText
     */
    public FilterWords checkSensitiveWords(FilterText filterText);

    /**
     * -匹配文本敏感词词语
     * @param filterText
     * @return
     */
    public FilterWords matchSensitiveWords(FilterText filterText);

    /**
     * -治理文本敏感词词语
     * @param filterText
     * @return 例如：一个傻逼。 -> 一个<span>**</span>。
     */
    public String maskSensitiveWords(FilterText filterText);
    
}
