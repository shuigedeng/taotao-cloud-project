/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.generator.maku.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.impl.BaseServiceImpl;
import com.taotao.cloud.generator.maku.dao.BaseClassDao;
import com.taotao.cloud.generator.maku.entity.BaseClassEntity;
import com.taotao.cloud.generator.maku.service.BaseClassService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassDao, BaseClassEntity>
        implements BaseClassService {

    @Override
    public PageResult<BaseClassEntity> page(Query query) {
        IPage<BaseClassEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

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
