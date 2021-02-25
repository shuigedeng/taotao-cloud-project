package com.taotao.cloud.uc.biz.service;


import com.taotao.cloud.uc.api.dto.dictItem.DictItemDTO;
import com.taotao.cloud.uc.api.query.dictItem.DictItemPageQuery;
import com.taotao.cloud.uc.api.query.dictItem.DictItemQuery;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ISysDictItemService
 *
 * @author dengtao
 * @date 2020/4/30 11:25
 */
public interface ISysDictItemService {

    /**
     * 根据字典id删除字典项
     *
     * @param dictId dictId
     * @author dengtao
     * @date 2020/9/30 11:29
     * @since v1.0
     */
    void deleteByDictId(Long dictId);

    /**
     * 添加字典项详情
     *
     * @param dictItemDTO dictItemDTO
     * @author dengtao
     * @date 2020/9/30 12:37
     * @since v1.0
     */
    SysDictItem save(DictItemDTO dictItemDTO);

    /**
     * 更新字典项详情
     *
     * @param id          id
     * @param dictItemDTO dictItemDTO
     * @author dengtao
     * @date 2020/9/30 12:40
     * @since v1.0
     */
    SysDictItem updateById(Long id, DictItemDTO dictItemDTO);

    /**
     * 根据id删除字典项详情
     *
     * @param id id
     * @author dengtao
     * @date 2020/9/30 12:44
     * @since v1.0
     */
    Boolean deleteById(Long id);

    /**
     * 分页查询字典详情内容
     *
     * @param dictItemPageQuery dictItemQuery
     * @author dengtao
     * @date 2020/9/30 12:48
     * @since v1.0
     */
    Page<SysDictItem> getPage(Pageable page, DictItemPageQuery dictItemPageQuery);

    /**
     * 查询字典详情内容
     *
     * @param dictItemQuery dictItemQuery
     * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysDictItem>
     * @author dengtao
     * @date 2020/10/15 11:02
     * @since v1.0
     */
    List<SysDictItem> getInfo(DictItemQuery dictItemQuery);
}
