package com.taotao.cloud.generator.maku.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.impl.BaseServiceImpl;
import com.taotao.cloud.generator.maku.dao.BaseClassDao;
import com.taotao.cloud.generator.maku.entity.BaseClassEntity;
import com.taotao.cloud.generator.maku.service.BaseClassService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassDao, BaseClassEntity> implements BaseClassService {

    @Override
    public PageResult<BaseClassEntity> page(Query query) {
        IPage<BaseClassEntity> page = baseMapper.selectPage(
                getPage(query), getWrapper(query)
        );

        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<BaseClassEntity> getList() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean save(BaseClassEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
