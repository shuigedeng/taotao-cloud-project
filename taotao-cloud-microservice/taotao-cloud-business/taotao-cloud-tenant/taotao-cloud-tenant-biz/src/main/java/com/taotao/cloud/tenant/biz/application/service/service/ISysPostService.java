package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysPostDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysPostQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysPost;

import java.util.List;

/**
 * 岗位Service接口
 */
public interface ISysPostService extends IService<SysPost> {

    /**
     * 分页查询岗位列表
     */
    IPage<SysPost> selectPostPage(SysPostQuery query);

    /**
     * 查询岗位列表
     */
    List<SysPost> selectPostList(SysPostQuery query);

    /**
     * 根据ID查询岗位详情
     */
    SysPost selectPostById(Long id);

    /**
     * 新增岗位
     */
    boolean insertPost(SysPostDTO dto);

    /**
     * 修改岗位
     */
    boolean updatePost(SysPostDTO dto);

    /**
     * 删除岗位
     */
    boolean deletePostById(Long id);

    /**
     * 批量删除岗位
     */
    boolean deletePostByIds(Long[] ids);
}
