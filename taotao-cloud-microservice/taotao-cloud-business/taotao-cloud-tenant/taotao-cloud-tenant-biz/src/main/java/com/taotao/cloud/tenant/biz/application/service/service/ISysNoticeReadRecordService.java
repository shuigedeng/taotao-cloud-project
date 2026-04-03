package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNoticeReadRecord;
import com.mdframe.forge.plugin.system.vo.NoticeReadStatisticsVO;
import com.mdframe.forge.plugin.system.vo.NoticeReadUserVO;

import java.util.List;

/**
 * 通知公告已读记录Service接口
 */
public interface ISysNoticeReadRecordService extends IService<SysNoticeReadRecord> {

    /**
     * 标记公告为已读
     * @param noticeId 公告ID
     * @return 是否成功
     */
    boolean markAsRead(Long noticeId);

    /**
     * 检查当前用户是否已读指定公告
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @return 是否已读
     */
    boolean isRead(Long noticeId, Long userId);

    /**
     * 获取公告的已读/未读统计
     * @param noticeId 公告ID
     * @return 统计信息
     */
    NoticeReadStatisticsVO getReadStatistics(Long noticeId);

    /**
     * 获取已读用户列表
     * @param noticeId 公告ID
     * @return 已读用户列表
     */
    List<NoticeReadUserVO> getReadUserList(Long noticeId);

    /**
     * 获取未读用户列表
     * @param noticeId 公告ID
     * @return 未读用户列表
     */
    List<NoticeReadUserVO> getUnreadUserList(Long noticeId);
}
