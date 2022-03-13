package com.taotao.cloud.operation.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.operation.biz.entity.PageData;
import com.taotao.cloud.operation.biz.entity.Special;
import com.taotao.cloud.operation.biz.mapper.SpecialMapper;
import com.taotao.cloud.operation.biz.service.PageDataService;
import com.taotao.cloud.operation.biz.service.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 专题活动业务层实现
 */
@Service
public class SpecialServiceImpl extends ServiceImpl<SpecialMapper, Special> implements
	SpecialService {

    /**
     * 页面数据
     */
    @Autowired
    private PageDataService pageDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Special addSpecial(Special special) {
        //新建页面
        PageData pageData = new PageData();
        pageDataService.save(pageData);

        //设置专题页面
        special.setPageDataId(pageData.getId());
        this.save(special);
        return special;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSpecial(String id) {

        //删除页面内容
        Special special = this.getById(id);
        pageDataService.removeById(special.getPageDataId());

        //删除专题
        return this.removeById(id);
    }
}
