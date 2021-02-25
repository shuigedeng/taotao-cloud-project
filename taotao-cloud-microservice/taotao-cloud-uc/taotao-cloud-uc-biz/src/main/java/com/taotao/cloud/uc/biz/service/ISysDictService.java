package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.api.query.dict.DictPageQuery;
import com.taotao.cloud.uc.biz.entity.SysDict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 字典表 服务类
 *
 * @author dengtao
 * @since 2020/4/30 11:18
 */
public interface ISysDictService {

    /**
     * 添加字典
     *
     * @param sysDict
     *        sysDict
     * @return com.taotao.cloud.uc.biz.entity.SysDict
     * @author dengtao
     * @since 2020/10/15 09:35
     * @version 1.0.0
     */
    SysDict save(SysDict sysDict);

    /**
     * 查询所有字典集合
     *
     * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysDict>
     * @author dengtao
     * @since 2020/10/15 09:36
     * @version 1.0.0
     */
    List<SysDict> getAll();

    /**
     * 分页查询字典集合
     *
     * @param page          page
     * @param dictPageQuery dictPageQuery
     * @return org.springframework.data.domain.Page<com.taotao.cloud.uc.biz.entity.SysDict>
     * @author dengtao
     * @since 2020/10/15 09:36
     * @version 1.0.0
     */
    Page<SysDict> getPage(Pageable page, DictPageQuery dictPageQuery);

    /**
     * 根据主键Id删除字典
     *
     * @param id id
     * @return java.lang.Boolean
     * @author dengtao
     * @since 2020/10/15 09:36
     * @version 1.0.0
     */
    Boolean removeById(Long id);

    /**
     * 根据code删除字典
     *
     * @param code code
     * @return java.lang.Boolean
     * @author dengtao
     * @since 2020/10/15 09:36
     * @version 1.0.0
     */
    Boolean deleteByCode(String code);

    SysDict findById(Long id);

    SysDict findByCode(String code);

    SysDict update(SysDict dict);
}
