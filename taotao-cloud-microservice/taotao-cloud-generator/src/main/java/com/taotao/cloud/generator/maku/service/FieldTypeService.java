package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;
import com.taotao.cloud.generator.maku.entity.FieldTypeEntity;

import java.util.Map;
import java.util.Set;

/**
 * 字段类型管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface FieldTypeService extends BaseService<FieldTypeEntity> {
    PageResult<FieldTypeEntity> page(Query query);

    Map<String, FieldTypeEntity> getMap();

    /**
     * 根据tableId，获取包列表
     *
     * @param tableId 表ID
     * @return 返回包列表
     */
    Set<String> getPackageByTableId(Long tableId);

    Set<String> getList();
}
