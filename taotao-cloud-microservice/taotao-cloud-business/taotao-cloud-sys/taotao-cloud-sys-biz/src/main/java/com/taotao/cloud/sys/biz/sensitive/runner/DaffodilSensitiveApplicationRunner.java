package com.taotao.cloud.sys.biz.sensitive.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.daffodil.core.entity.Page;
import com.daffodil.core.entity.Query;
import com.daffodil.sensitive.constant.SensitiveConstant;
import com.daffodil.sensitive.entity.SysSensitiveWords;
import com.daffodil.sensitive.service.ISysSensitiveWordsService;
import com.daffodil.util.StringUtils;
import com.daffodil.util.hash.BloomFilterRedis;

/**
 * 
 * @author yweijian
 * @date 2022年9月19日
 * @version 2.0.0
 * @description
 */
@Component
public class DaffodilSensitiveApplicationRunner implements ApplicationRunner {

    private static final Integer PAGESIZE = 1000;
    
    @Autowired
    private BloomFilterRedis<String> bloomFilter;
    
    @Autowired
    private ISysSensitiveWordsService sensitiveWordsService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadSensitiveWords();
    }
    
    /** 初始化加载敏感词 */
    private void loadSensitiveWords() {
        bloomFilter.clean(SensitiveConstant.SENSITIVE_WORD);
        int totalRow = sensitiveWordsService.countSensitiveWords();
        int totalPage = this.getTotalPage(totalRow, PAGESIZE);
        for(int i = 1; i <= totalPage; i++) {
            Page page = new Page();
            page.setPageNum(i);
            page.setPageSize(PAGESIZE);
            Query<SysSensitiveWords> query = new Query<SysSensitiveWords>();
            query.setEntity(new SysSensitiveWords());
            query.setPage(page);
            List<SysSensitiveWords> list = sensitiveWordsService.selectSensitiveWordsList(query);
            if(StringUtils.isNotEmpty(list)) {
                for(SysSensitiveWords words : list) {
                    bloomFilter.put(SensitiveConstant.SENSITIVE_WORD, words.getWords());
                }
            }
        }
    }
    
    private int getTotalPage(int totalRow, int pageSize) {
        int size = totalRow / pageSize;
        int mod = totalRow % pageSize;
        if (mod != 0) {
            size++;
        }
        if (size == 0) {
            size = 1;
        }
        return size;
    }

}
