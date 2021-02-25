package com.taotao.cloud.standalone.system.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysSocial;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-27
 */
public interface SysSocialMapper extends BaseMapper<SysSocial> {

    /**
     * 获取rank的最大值加1
     * @param userId
     * @param providerId
     * @return
     */
    @Select("select coalesce(max(`rank`) + 1, 1) as `rank` from social_UserConnection where userId = #{userId} and providerId = #{providerId}")
    int getRank(String userId, String providerId);


}
