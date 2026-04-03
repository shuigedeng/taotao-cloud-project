package com.taotao.cloud.tenant.biz.application.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import import com.taotao.cloud.tenant.biz.application.service.service.ISysNoticeReadRecordService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNoticeReadRecord;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOrg;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserOrg;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysNoticeMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysNoticeReadRecordMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysOrgMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserOrgMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysNoticeReadRecordService;
import com.mdframe.forge.plugin.system.vo.NoticeReadStatisticsVO;
import com.mdframe.forge.plugin.system.vo.NoticeReadUserVO;
import com.mdframe.forge.starter.core.session.SessionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告已读记录Service实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysNoticeReadRecordServiceImpl extends ServiceImpl<SysNoticeReadRecordMapper, SysNoticeReadRecord>
        implements ISysNoticeReadRecordService {

    private final SysNoticeReadRecordMapper readRecordMapper;
    private final SysNoticeMapper noticeMapper;
    private final SysUserMapper userMapper;
    private final SysOrgMapper orgMapper;
    private final SysUserOrgMapper userOrgMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long noticeId) {
        try {
            // 获取当前用户信息
            Long userId = SessionHelper.getUserId();
            String userName = SessionHelper.getUsername();
            
            // 检查是否已读
            LambdaQueryWrapper<SysNoticeReadRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysNoticeReadRecord::getNoticeId, noticeId)
                   .eq(SysNoticeReadRecord::getUserId, userId);
            
            if (readRecordMapper.selectCount(wrapper) > 0) {
                log.info("用户{}已读过公告{}", userId, noticeId);
                return true;
            }
            
            // 获取用户组织信息
            LambdaQueryWrapper<SysUserOrg> userOrgWrapper = new LambdaQueryWrapper<>();
            userOrgWrapper.eq(SysUserOrg::getUserId, userId)
                         .eq(SysUserOrg::getIsMain, 1); // 主组织
            SysUserOrg userOrg = userOrgMapper.selectOne(userOrgWrapper);
            
            // 创建已读记录
            SysNoticeReadRecord record = new SysNoticeReadRecord();
            record.setNoticeId(noticeId);
            record.setUserId(userId);
            record.setUserName(userName);
            if (userOrg != null) {
                record.setOrgId(userOrg.getOrgId());
                SysOrg sysOrg = orgMapper.selectById(userOrg.getOrgId());
                record.setOrgName(sysOrg.getOrgName());
            }
            record.setReadTime(LocalDateTime.now());
            record.setCreateTime(LocalDateTime.now());
            
            return readRecordMapper.insert(record) > 0;
        } catch (Exception e) {
            log.error("标记公告已读失败", e);
            return false;
        }
    }

    @Override
    public boolean isRead(Long noticeId, Long userId) {
        LambdaQueryWrapper<SysNoticeReadRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNoticeReadRecord::getNoticeId, noticeId)
               .eq(SysNoticeReadRecord::getUserId, userId);
        return readRecordMapper.selectCount(wrapper) > 0;
    }

    @Override
    public NoticeReadStatisticsVO getReadStatistics(Long noticeId) {
        NoticeReadStatisticsVO statistics = new NoticeReadStatisticsVO();
        statistics.setNoticeId(noticeId);
        
        // 查询已读人数
        LambdaQueryWrapper<SysNoticeReadRecord> readWrapper = new LambdaQueryWrapper<>();
        readWrapper.eq(SysNoticeReadRecord::getNoticeId, noticeId);
        int readCount = readRecordMapper.selectCount(readWrapper).intValue();
        statistics.setReadCount(readCount);
        
        // 查询未读用户列表获取总数
        List<NoticeReadUserVO> unreadList = getUnreadUserList(noticeId);
        int unreadCount = unreadList.size();
        statistics.setUnreadCount(unreadCount);
        
        // 计算总人数和已读率
        int totalCount = readCount + unreadCount;
        statistics.setTotalUserCount(totalCount);
        
        if (totalCount > 0) {
            double rate = (double) readCount / totalCount * 100;
            statistics.setReadRate(Math.round(rate * 100.0) / 100.0);
        } else {
            statistics.setReadRate(0.0);
        }
        
        return statistics;
    }

    @Override
    public List<NoticeReadUserVO> getReadUserList(Long noticeId) {
        return readRecordMapper.selectReadUserList(noticeId);
    }

    @Override
    public List<NoticeReadUserVO> getUnreadUserList(Long noticeId) {
        return readRecordMapper.selectUnreadUserList(noticeId);
    }
}
