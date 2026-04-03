package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysDictData;
import com.mdframe.forge.starter.core.domain.PageQuery;

import java.util.List;

/**
 * 字典数据Service接口
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 分页查询字典数据列表
     */
    Page<SysDictData> selectDictDataPage(PageQuery pageQuery, SysDictDataQuery query);

    /**
     * 查询字典数据列表
     */
    List<SysDictData> selectDictDataList(SysDictDataQuery query);

    /**
     * 根据字典类型查询字典数据
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据ID查询字典数据详情
     */
    SysDictData selectDictDataById(Long dictCode);

    /**
     * 新增字典数据
     */
    boolean insertDictData(SysDictDataDTO dto);

    /**
     * 修改字典数据
     */
    boolean updateDictData(SysDictDataDTO dto);

    /**
     * 删除字典数据
     */
    boolean deleteDictDataById(Long dictCode);

    /**
     * 批量删除字典数据
     */
    boolean deleteDictDataByIds(Long[] dictCodes);
}
