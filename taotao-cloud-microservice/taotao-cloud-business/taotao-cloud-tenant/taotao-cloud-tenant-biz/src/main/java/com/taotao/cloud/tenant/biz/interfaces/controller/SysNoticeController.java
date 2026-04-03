package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeQuery;
import com.mdframe.forge.plugin.system.entity.SysNotice;
import com.taotao.cloud.tenant.biz.application.service.service.ISysNoticeReadRecordService;
import com.taotao.cloud.tenant.biz.application.service.service.ISysNoticeService;
import com.mdframe.forge.plugin.system.vo.NoticeReadStatisticsVO;
import com.mdframe.forge.plugin.system.vo.NoticeReadUserVO;
import com.mdframe.forge.plugin.system.vo.SysNoticeVO;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import com.mdframe.forge.starter.core.annotation.log.OperationLog;
import com.mdframe.forge.starter.core.domain.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告Controller
 */
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysNoticeController {

    private final ISysNoticeService noticeService;
    private final ISysNoticeReadRecordService noticeReadRecordService;

    /**
     * 分页查询公告列表
     */
    @GetMapping("/page")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "分页查询公告列表")
    public Result<Page<SysNotice>> page(PageQuery pageQuery, SysNoticeQuery query) {
        Page<SysNotice> page = noticeService.selectNoticePage(pageQuery, query);
        return Result.success(page);
    }

    /**
     * 查询公告列表
     */
    @GetMapping("/list")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "查询公告列表")
    public Result<List<SysNotice>> list(SysNoticeQuery query) {
        List<SysNotice> list = noticeService.selectNoticeList(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询公告详情
     */
    @PostMapping("/getById")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "查询公告详情")
    public Result<SysNoticeVO> getById(@RequestParam Long noticeId) {
        SysNoticeVO notice = noticeService.selectNoticeById(noticeId);
        // 增加阅读次数
        if (notice != null) {
            noticeService.increaseReadCount(noticeId);
        }
        return Result.success(notice);
    }

    /**
     * 新增公告
     */
    @PostMapping("/add")
    @OperationLog(module = "通知公告管理", type = OperationType.ADD, desc = "新增公告")
    public Result<Void> add(@RequestBody SysNoticeDTO dto) {
        boolean result = noticeService.insertNotice(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改公告
     */
    @PostMapping("/edit")
    @OperationLog(module = "通知公告管理", type = OperationType.UPDATE, desc = "修改公告")
    public Result<Void> edit(@RequestBody SysNoticeDTO dto) {
        boolean result = noticeService.updateNotice(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除公告
     */
    @PostMapping("/remove")
    @OperationLog(module = "通知公告管理", type = OperationType.DELETE, desc = "删除公告")
    public Result<Void> remove(@RequestParam Long noticeId) {
        boolean result = noticeService.deleteNoticeById(noticeId);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除公告
     */
    @PostMapping("/removeBatch")
    @OperationLog(module = "通知公告管理", type = OperationType.DELETE, desc = "批量删除公告")
    public Result<Void> removeBatch(@RequestBody Long[] noticeIds) {
        boolean result = noticeService.deleteNoticeByIds(noticeIds);
        return result ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 发布公告
     */
    @PostMapping("/publish")
    @OperationLog(module = "通知公告管理", type = OperationType.UPDATE, desc = "发布公告")
    public Result<Void> publish(@RequestParam Long noticeId) {
        boolean result = noticeService.publishNotice(noticeId);
        return result ? Result.success() : Result.error("发布失败");
    }

    /**
     * 撤回公告
     */
    @PostMapping("/revoke")
    @OperationLog(module = "通知公告管理", type = OperationType.UPDATE, desc = "撤回公告")
    public Result<Void> revoke(@RequestParam Long noticeId) {
        boolean result = noticeService.revokeNotice(noticeId);
        return result ? Result.success() : Result.error("撤回失败");
    }

    /**
     * 置顶公告
     */
    @PostMapping("/top")
    @OperationLog(module = "通知公告管理", type = OperationType.UPDATE, desc = "置顶公告")
    public Result<Void> top(@RequestParam Long noticeId,
                              @RequestParam Integer isTop,
                              @RequestParam(required = false) Integer topSort) {
        boolean result = noticeService.topNotice(noticeId, isTop, topSort);
        return result ? Result.success() : Result.error("操作失败");
    }

    /**
     * 查询用户可见的公告列表（前台展示）
     */
    @GetMapping("/user/page")
    public Result<Page<SysNoticeVO>> userPage(PageQuery pageQuery, SysNoticeQuery query) {
        Page<SysNoticeVO> page = noticeService.selectUserNoticePage(pageQuery, query);
        return Result.success(page);
    }

    /**
     * 查询当前用户未读公告数量
     */
    @GetMapping("/user/unread-count")
    public Result<Integer> getUserUnreadCount() {
        Integer count = noticeService.getUserUnreadCount();
        return Result.success(count);
    }

    /**
     * 标记公告为已读
     */
    @PostMapping("/markAsRead")
    public Result<Void> markAsRead(@RequestParam Long noticeId) {
        boolean result = noticeReadRecordService.markAsRead(noticeId);
        return result ? Result.success() : Result.error("标记失败");
    }

    /**
     * 获取公告已读/未读统计（只有发布人可见）
     */
    @GetMapping("/statistics/{noticeId}")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "查询公告统计")
    public Result<NoticeReadStatisticsVO> getStatistics(@PathVariable Long noticeId) {
        NoticeReadStatisticsVO statistics = noticeReadRecordService.getReadStatistics(noticeId);
        return Result.success(statistics);
    }

    /**
     * 获取已读用户列表（只有发布人可见）
     */
    @GetMapping("/read-users/{noticeId}")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "查询已读用户列表")
    public Result<List<NoticeReadUserVO>> getReadUsers(@PathVariable Long noticeId) {
        List<NoticeReadUserVO> users = noticeReadRecordService.getReadUserList(noticeId);
        return Result.success(users);
    }

    /**
     * 获取未读用户列表（只有发布人可见）
     */
    @GetMapping("/unread-users/{noticeId}")
    @OperationLog(module = "通知公告管理", type = OperationType.QUERY, desc = "查询未读用户列表")
    public Result<List<NoticeReadUserVO>> getUnreadUsers(@PathVariable Long noticeId) {
        List<NoticeReadUserVO> users = noticeReadRecordService.getUnreadUserList(noticeId);
        return Result.success(users);
    }
}
