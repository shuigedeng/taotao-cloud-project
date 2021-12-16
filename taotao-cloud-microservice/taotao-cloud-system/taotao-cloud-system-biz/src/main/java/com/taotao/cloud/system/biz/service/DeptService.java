/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.system.api.dto.DeptDto;
import com.taotao.cloud.system.api.dto.DeptQueryCriteria;
import com.taotao.cloud.system.biz.entity.Dept;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeptService extends IService<Dept> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(DeptQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<DeptDto>
     */
    List<Dept> queryAll(DeptQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DeptDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据PID查询
     * @param pid /
     * @return /
     */
    List<Dept> findByPid(long pid);

    /**
     * 构建树形数据
     * @param deptDtos 原始数据
     * @return /
     */
    Object buildTree(List<DeptDto> deptDtos);


    /**
     * 删除部门
     * @param deptIds
     */
    void delDepts(List<Long> deptIds);

    /**
     * 获取待删除的部门
     * @param deptList /
     * @param deptDtos /
     * @return /
     */
    /*Set<DeptDto> getDeleteDepts(List<Dept> deptList, Set<DeptDto> deptDtos);*/

    /**
     * 根据角色ID查询
     * @param id /
     * @return /
     */
    Set<Dept> findByRoleIds(Long id);
}
