package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNotice;
import com.mdframe.forge.plugin.system.vo.SysNoticeVO;
import com.mdframe.forge.starter.core.domain.PageQuery;

import java.util.List;

/**
 * 通知公告Service接口
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询公告列表
     */
    Page<SysNotice> selectNoticePage(PageQuery pageQuery, SysNoticeQuery query);

    /**
     * 查询公告列表
     */
    List<SysNotice> selectNoticeList(SysNoticeQuery query);

    /**
     * 根据ID查询公告详情
     */
    SysNoticeVO selectNoticeById(Long noticeId);

    /**
     * 新增公告
     */
    boolean insertNotice(SysNoticeDTO dto);

    /**
     * 修改公告
     */
    boolean updateNotice(SysNoticeDTO dto);

    /**
     * 删除公告
     */
    boolean deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告
     */
    boolean deleteNoticeByIds(Long[] noticeIds);

    /**
     * 发布公告
     */
    boolean publishNotice(Long noticeId);

    /**
     * 撤回公告
     */
    boolean revokeNotice(Long noticeId);

    /**
     * 置顶/取消置顶公告
     */
    boolean topNotice(Long noticeId, Integer isTop, Integer topSort);

    /**
     * 增加阅读次数
     */
    boolean increaseReadCount(Long noticeId);

    /**
     * 查询当前用户可见的公告列表（前台展示）
     */
    Page<SysNoticeVO> selectUserNoticePage(PageQuery pageQuery, SysNoticeQuery query);

    /**
     * 查询当前用户的未读公告数量
     */
    Integer getUserUnreadCount();
}
