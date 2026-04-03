package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOnlineUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户Mapper
 */
@Mapper
public interface SysOnlineUserMapper extends BaseMapper<SysOnlineUser> {

    /**
     * 批量更新状态为离线
     *
     * @param userIds 用户ID列表
     * @param logoutType 登出类型
     * @return 更新数量
     */
    int batchUpdateOffline(@Param("userIds") List<Long> userIds,
                          @Param("logoutType") Integer logoutType,
                          @Param("logoutTime") LocalDateTime logoutTime);

    /**
     * 清理过期的在线用户记录
     *
     * @param expireTime 过期时间
     * @return 清理数量
     */
    int cleanExpiredUsers(@Param("expireTime") LocalDateTime expireTime);
}
