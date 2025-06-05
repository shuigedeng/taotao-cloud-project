package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;
import com.taotao.cloud.generator.maku.entity.BaseClassEntity;

import java.util.List;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    PageResult<BaseClassEntity> page(Query query);

    List<BaseClassEntity> getList();
}
