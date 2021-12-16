/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.system.api.dto.DictDto;
import com.taotao.cloud.system.api.dto.DictQueryCriteria;
import com.taotao.cloud.system.biz.entity.Dict;
import com.taotao.cloud.system.biz.mapper.DictMapper;
import com.taotao.cloud.system.biz.service.DictService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author hupeng
 * @date 2020-05-14
 */
@Service
//@CacheConfig(cacheNames = "dict")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final IGenerator generator;


	public DictServiceImpl(IGenerator generator) {
		this.generator = generator;
	}

	@Override
    //@Cacheable
    public Map<String, Object> queryAll(DictQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Dict> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), DictDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<Dict> queryAll(DictQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Dict.class, criteria));
    }


    @Override
    public void download(List<DictDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDto dict : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("字典名称", dict.getName());
            map.put("描述", dict.getRemark());
            map.put("创建日期", dict.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
