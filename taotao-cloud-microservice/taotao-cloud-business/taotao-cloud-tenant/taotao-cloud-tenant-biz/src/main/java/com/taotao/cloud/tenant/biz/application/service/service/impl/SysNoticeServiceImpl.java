package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysNoticeQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.*;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.*;
import com.taotao.cloud.tenant.biz.application.service.service.ISysNoticeService;
import com.mdframe.forge.plugin.system.vo.SysNoticeVO;
import com.mdframe.forge.starter.core.session.SessionHelper;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.trans.annotation.DictTranslate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通知公告Service实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    private final SysNoticeMapper noticeMapper;
    private final SysFileMetadataMapper fileMetadataMapper;
    private final SysNoticeOrgMapper noticeOrgMapper;
    private final SysNoticeReadRecordMapper noticeReadRecordMapper;
    private final SysOrgMapper orgMapper;
    private final SysUserOrgMapper userOrgMapper;

    @Override
    @DictTranslate
    public Page<SysNotice> selectNoticePage(PageQuery pageQuery, SysNoticeQuery query) {
        log.info("分页查询参数:{}, 查询条件:{}", pageQuery, query);
        LambdaQueryWrapper<SysNotice> wrapper = buildQueryWrapper(query);
        // 排序：置顶优先，然后按发布时间倒序
        wrapper.orderByDesc(SysNotice::getIsTop, SysNotice::getTopSort, SysNotice::getPublishTime);
        return noticeMapper.selectPage(pageQuery.toPage(), wrapper);
    }

    @Override
    @DictTranslate
    public List<SysNotice> selectNoticeList(SysNoticeQuery query) {
        log.info("查询参数:{}", query);
        LambdaQueryWrapper<SysNotice> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SysNotice::getIsTop, SysNotice::getTopSort, SysNotice::getPublishTime);
        return noticeMapper.selectList(wrapper);
    }

    @Override
    @DictTranslate
    public SysNoticeVO selectNoticeById(Long noticeId) {
        SysNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            return null;
        }

        SysNoticeVO vo = new SysNoticeVO();
        BeanUtil.copyProperties(notice, vo);

        // 处理附件信息
        if (StrUtil.isNotBlank(notice.getAttachmentIds())) {
            String[] fileIds = notice.getAttachmentIds().split(",");
            List<SysNoticeVO.AttachmentInfo> attachments = new ArrayList<>();
            for (String fileIdStr : fileIds) {
                try {
                    Long fileId = Long.parseLong(fileIdStr.trim());
                    SysFileMetadata fileMetadata = fileMetadataMapper.selectById(fileId);
                    if (fileMetadata != null) {
                        SysNoticeVO.AttachmentInfo attachment = new SysNoticeVO.AttachmentInfo();
                        attachment.setFileId(fileId);
                        attachment.setFileName(fileMetadata.getOriginalName());
                        attachment.setFileSize(fileMetadata.getFileSize());
                        attachments.add(attachment);
                    }
                } catch (NumberFormatException e) {
                    log.warn("无效的文件ID: {}", fileIdStr);
                }
            }
            vo.setAttachments(attachments);
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertNotice(SysNoticeDTO dto) {
        SysNotice notice = new SysNotice();
        BeanUtil.copyProperties(dto, notice);
        
        // 设置默认值
        if (notice.getPublishStatus() == null) {
            notice.setPublishStatus(0); // 默认草稿状态
        }
        if (notice.getIsTop() == null) {
            notice.setIsTop(0);
        }
        if (notice.getTopSort() == null) {
            notice.setTopSort(0);
        }
        if (notice.getReadCount() == null) {
            notice.setReadCount(0);
        }
        if (notice.getPublishScope() == null) {
            notice.setPublishScope(0); // 默认全部组织
        }
        
        boolean result = noticeMapper.insert(notice) > 0;
        
        // 如果指定了组织，保存组织关联
        if (result && dto.getPublishScope() != null && dto.getPublishScope() == 1 
                && dto.getOrgIds() != null && !dto.getOrgIds().isEmpty()) {
            saveNoticeOrgRelations(notice.getNoticeId(), dto.getOrgIds());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotice(SysNoticeDTO dto) {
        SysNotice notice = new SysNotice();
        BeanUtil.copyProperties(dto, notice);
        boolean result = noticeMapper.updateById(notice) > 0;
        
        // 更新组织关联
        if (result && dto.getPublishScope() != null) {
            // 先删除原有关联
            LambdaQueryWrapper<SysNoticeOrg> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysNoticeOrg::getNoticeId, dto.getNoticeId());
            noticeOrgMapper.delete(wrapper);
            
            // 如果是指定组织，重新保存关联
            if (dto.getPublishScope() == 1 && dto.getOrgIds() != null && !dto.getOrgIds().isEmpty()) {
                saveNoticeOrgRelations(dto.getNoticeId(), dto.getOrgIds());
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNoticeById(Long noticeId) {
        return noticeMapper.deleteById(noticeId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNoticeByIds(Long[] noticeIds) {
        return noticeMapper.deleteBatchIds(Arrays.asList(noticeIds)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishNotice(Long noticeId) {
        SysNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            log.warn("公告不存在: {}", noticeId);
            return false;
        }

        // 更新发布状态
        notice.setPublishStatus(1);
        notice.setPublishTime(LocalDateTime.now());
        
        // 设置发布人信息
        try {
            Long userId = SessionHelper.getUserId();
            String username = SessionHelper.getUsername();
            notice.setPublisherId(userId);
            notice.setPublisherName(username);
        } catch (Exception e) {
            log.warn("获取当前用户信息失败", e);
        }

        return noticeMapper.updateById(notice) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeNotice(Long noticeId) {
        SysNotice notice = new SysNotice();
        notice.setNoticeId(noticeId);
        notice.setPublishStatus(2); // 已撤回
        return noticeMapper.updateById(notice) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean topNotice(Long noticeId, Integer isTop, Integer topSort) {
        SysNotice notice = new SysNotice();
        notice.setNoticeId(noticeId);
        notice.setIsTop(isTop);
        if (topSort != null) {
            notice.setTopSort(topSort);
        }
        return noticeMapper.updateById(notice) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseReadCount(Long noticeId) {
        SysNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            return false;
        }
        notice.setReadCount(notice.getReadCount() == null ? 1 : notice.getReadCount() + 1);
        return noticeMapper.updateById(notice) > 0;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysNotice> buildQueryWrapper(SysNoticeQuery query) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getNoticeTitle()), SysNotice::getNoticeTitle, query.getNoticeTitle())
                .eq(StringUtils.isNotBlank(query.getNoticeType()), SysNotice::getNoticeType, query.getNoticeType())
                .eq(query.getPublishStatus() != null, SysNotice::getPublishStatus, query.getPublishStatus())
                .eq(query.getIsTop() != null, SysNotice::getIsTop, query.getIsTop())
                .like(StringUtils.isNotBlank(query.getPublisherName()), SysNotice::getPublisherName, query.getPublisherName())
                .ge(query.getEffectiveTimeStart() != null, SysNotice::getEffectiveTime, query.getEffectiveTimeStart())
                .le(query.getEffectiveTimeEnd() != null, SysNotice::getEffectiveTime, query.getEffectiveTimeEnd());
        return wrapper;
    }

    /**
     * 保存公告-组织关联
     */
    private void saveNoticeOrgRelations(Long noticeId, List<Long> orgIds) {
        for (Long orgId : orgIds) {
            SysNoticeOrg noticeOrg = new SysNoticeOrg();
            noticeOrg.setNoticeId(noticeId);
            noticeOrg.setOrgId(orgId);
            noticeOrgMapper.insert(noticeOrg);
        }
    }

    @Override
    public Page<SysNoticeVO> selectUserNoticePage(PageQuery pageQuery, SysNoticeQuery query) {
        try {
            Long userId = SessionHelper.getUserId();
            
            // 查询用户所属组织
            LambdaQueryWrapper<SysUserOrg> userOrgWrapper = new LambdaQueryWrapper<>();
            userOrgWrapper.eq(SysUserOrg::getUserId, userId);
            List<SysUserOrg> userOrgs = userOrgMapper.selectList(userOrgWrapper);
            List<Long> userOrgIds = userOrgs.stream()
                    .map(SysUserOrg::getOrgId)
                    .collect(java.util.stream.Collectors.toList());
            
            // 构建查询条件
            LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysNotice::getPublishStatus, 1) // 只查询已发布的
                   .like(StringUtils.isNotBlank(query.getNoticeTitle()), SysNotice::getNoticeTitle, query.getNoticeTitle())
                   .eq(StringUtils.isNotBlank(query.getNoticeType()), SysNotice::getNoticeType, query.getNoticeType())
                   .and(w -> w.eq(SysNotice::getPublishScope, 0) // 全部组织
                           .or(w2 -> w2.eq(SysNotice::getPublishScope, 1)
                                   .exists("SELECT 1 FROM sys_notice_org no WHERE no.notice_id = sys_notice.notice_id "
                                           + "AND no.org_id IN ({0})", String.join(",", userOrgIds.stream()
                                           .map(String::valueOf).collect(java.util.stream.Collectors.toList())))))
                   .orderByDesc(SysNotice::getIsTop, SysNotice::getTopSort, SysNotice::getPublishTime);
            
            Page<SysNotice> page = noticeMapper.selectPage(pageQuery.toPage(), wrapper);
            
            // 转换为VO并填充已读状态
            Page<SysNoticeVO> voPage = new Page<>();
            voPage.setCurrent(page.getCurrent());
            voPage.setSize(page.getSize());
            voPage.setTotal(page.getTotal());
            
            List<SysNoticeVO> voList = page.getRecords().stream().map(notice -> {
                SysNoticeVO vo = new SysNoticeVO();
                BeanUtil.copyProperties(notice, vo);
                
                // 检查是否已读
                LambdaQueryWrapper<SysNoticeReadRecord> readWrapper = new LambdaQueryWrapper<>();
                readWrapper.eq(SysNoticeReadRecord::getNoticeId, notice.getNoticeId())
                           .eq(SysNoticeReadRecord::getUserId, userId);
                vo.setIsRead(noticeReadRecordMapper.selectCount(readWrapper) > 0 ? 1 : 0);
                
                // 填充附件信息
                if (StrUtil.isNotBlank(notice.getAttachmentIds())) {
                    String[] fileIds = notice.getAttachmentIds().split(",");
                    List<SysNoticeVO.AttachmentInfo> attachments = new ArrayList<>();
                    for (String fileIdStr : fileIds) {
                        try {
                            Long fileId = Long.parseLong(fileIdStr.trim());
                            SysFileMetadata fileMetadata = fileMetadataMapper.selectById(fileId);
                            if (fileMetadata != null) {
                                SysNoticeVO.AttachmentInfo attachment = new SysNoticeVO.AttachmentInfo();
                                attachment.setFileId(fileId);
                                attachment.setFileName(fileMetadata.getOriginalName());
                                attachment.setFileSize(fileMetadata.getFileSize());
                                attachments.add(attachment);
                            }
                        } catch (NumberFormatException e) {
                            log.warn("无效的文件ID: {}", fileIdStr);
                        }
                    }
                    vo.setAttachments(attachments);
                }
                
                return vo;
            }).collect(java.util.stream.Collectors.toList());
            
            voPage.setRecords(voList);
            return voPage;
        } catch (Exception e) {
            log.error("查询用户公告列表失败", e);
            return new Page<>();
        }
    }

    @Override
    public Integer getUserUnreadCount() {
        try {
            Long userId = SessionHelper.getUserId();
            
            // 查询用户所属组织
            LambdaQueryWrapper<SysUserOrg> userOrgWrapper = new LambdaQueryWrapper<>();
            userOrgWrapper.eq(SysUserOrg::getUserId, userId);
            List<SysUserOrg> userOrgs = userOrgMapper.selectList(userOrgWrapper);
            List<Long> userOrgIds = userOrgs.stream()
                    .map(SysUserOrg::getOrgId)
                    .collect(java.util.stream.Collectors.toList());
            
            if (userOrgIds.isEmpty()) {
                return 0;
            }
            
            // 查询未读公告数量
            LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysNotice::getPublishStatus, 1)
                   .and(w -> w.eq(SysNotice::getPublishScope, 0)
                           .or(w2 -> w2.eq(SysNotice::getPublishScope, 1)
                                   .exists("SELECT 1 FROM sys_notice_org no WHERE no.notice_id = sys_notice.notice_id "
                                           + "AND no.org_id IN ({0})", String.join(",", userOrgIds.stream()
                                           .map(String::valueOf).collect(java.util.stream.Collectors.toList())))))
                   .notExists("SELECT 1 FROM sys_notice_read_record r WHERE r.notice_id = sys_notice.notice_id "
                           + "AND r.user_id = {0}", userId);
            
            return noticeMapper.selectCount(wrapper).intValue();
        } catch (Exception e) {
            log.error("查询未读公告数量失败", e);
            return 0;
        }
    }
}
