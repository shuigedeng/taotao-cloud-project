package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysPostDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysPostQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysPost;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysPostMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysPostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    private final SysPostMapper postMapper;

    @Override
    public IPage<SysPost> selectPostPage(SysPostQuery query) {
        LambdaQueryWrapper<SysPost> wrapper = buildQueryWrapper(query);
        Page<SysPost> page = new Page<>(query.getPageNum(), query.getPageSize());
        return postMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysPost> selectPostList(SysPostQuery query) {
        LambdaQueryWrapper<SysPost> wrapper = buildQueryWrapper(query);
        return postMapper.selectList(wrapper);
    }

    @Override
    public SysPost selectPostById(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public boolean insertPost(SysPostDTO dto) {
        SysPost post = new SysPost();
        BeanUtil.copyProperties(dto, post);
        return postMapper.insert(post) > 0;
    }

    @Override
    public boolean updatePost(SysPostDTO dto) {
        SysPost post = new SysPost();
        BeanUtil.copyProperties(dto, post);
        return postMapper.updateById(post) > 0;
    }

    @Override
    public boolean deletePostById(Long id) {
        return postMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deletePostByIds(Long[] ids) {
        return postMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
    }

    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPostQuery query) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        // 添加空值检查,防止NPE
        if (query != null) {
            wrapper.eq(query.getTenantId() != null, SysPost::getTenantId, query.getTenantId())
                    .eq(StringUtils.isNotBlank(query.getPostCode()), SysPost::getPostCode, query.getPostCode())
                    .eq(query.getOrgId() != null, SysPost::getOrgId, query.getOrgId())
                    .like(StringUtils.isNotBlank(query.getPostName()), SysPost::getPostName, query.getPostName())
                    .eq(query.getPostStatus() != null, SysPost::getPostStatus, query.getPostStatus())
                    .eq(query.getPostType() != null, SysPost::getPostType, query.getPostType());
        }
        wrapper.orderByAsc(SysPost::getSort)
                .orderByDesc(SysPost::getCreateTime);
        return wrapper;
    }
}
