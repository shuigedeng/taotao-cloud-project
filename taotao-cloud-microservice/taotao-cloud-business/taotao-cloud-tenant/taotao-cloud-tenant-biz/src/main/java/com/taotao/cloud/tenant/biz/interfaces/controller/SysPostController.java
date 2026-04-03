package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.biz.application.dto.SysPostDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysPostQuery;
import com.mdframe.forge.plugin.system.entity.SysPost;
import com.taotao.cloud.tenant.biz.application.service.service.ISysPostService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位管理Controller
 */
@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysPostController {

    private final ISysPostService postService;

    /**
     * 分页查询岗位列表
     */
    @GetMapping("/page")
    public Result<IPage<SysPost>> page(SysPostQuery query) {
        IPage<SysPost> page = postService.selectPostPage(query);
        return Result.success(page);
    }

    /**
     * 查询岗位列表
     */
    @GetMapping("/list")
    public Result<List<SysPost>> list(SysPostQuery query) {
        List<SysPost> list = postService.selectPostList(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询岗位详情
     */
    @PostMapping("/getById")
    public Result<SysPost> getById(@RequestParam Long id) {
        SysPost post = postService.selectPostById(id);
        return Result.success(post);
    }

    /**
     * 新增岗位
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysPostDTO dto) {
        boolean result = postService.insertPost(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改岗位
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysPostDTO dto) {
        boolean result = postService.updatePost(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除岗位
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = postService.deletePostById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除岗位
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] ids) {
        boolean result = postService.deletePostByIds(ids);
        return result ? Result.success() : Result.error("批量删除失败");
    }
}
