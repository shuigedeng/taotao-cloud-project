package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNoticeReadRecord;
import com.mdframe.forge.plugin.system.vo.NoticeReadUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知公告已读记录Mapper接口
 */
@Mapper
public interface SysNoticeReadRecordMapper extends BaseMapper<SysNoticeReadRecord> {

    /**
     * 查询指定公告的已读用户列表
     */
    List<NoticeReadUserVO> selectReadUserList(@Param("noticeId") Long noticeId);

    /**
     * 查询指定公告的未读用户列表
     */
    List<NoticeReadUserVO> selectUnreadUserList(@Param("noticeId") Long noticeId);
}
