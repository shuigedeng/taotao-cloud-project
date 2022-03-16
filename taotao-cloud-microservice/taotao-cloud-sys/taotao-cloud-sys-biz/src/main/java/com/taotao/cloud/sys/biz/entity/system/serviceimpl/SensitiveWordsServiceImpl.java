package com.taotao.cloud.sys.biz.entity.system.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.biz.entity.system.entity.dos.SensitiveWords;
import com.taotao.cloud.sys.biz.entity.system.mapper.SensitiveWordsMapper;
import com.taotao.cloud.sys.biz.entity.system.service.SensitiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感词业务层实现
 *
 * @author Bulbasaur
 * @since 2020/11/17 8:02 下午
 */
@Service
public class SensitiveWordsServiceImpl extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> implements
	SensitiveWordsService {
    @Autowired
    private Cache<List<String>> cache;

    @Override
    public void resetCache() {
        List<SensitiveWords> sensitiveWordsList = this.list();

        if (sensitiveWordsList == null || sensitiveWordsList.isEmpty()) {
            return;
        }
        List<String> sensitiveWords = sensitiveWordsList.stream().map(SensitiveWords::getSensitiveWord).collect(Collectors.toList());
        cache.put(CachePrefix.SENSITIVE.getPrefix(), sensitiveWords);
    }
}
