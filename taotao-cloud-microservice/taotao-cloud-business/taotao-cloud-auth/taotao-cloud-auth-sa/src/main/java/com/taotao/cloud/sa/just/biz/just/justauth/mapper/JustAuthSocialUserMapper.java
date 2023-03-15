package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 租户第三方用户绑定 Mapper 接口
 * </p>
 *
 * @since 2022-05-19
 */
public interface JustAuthSocialUserMapper extends BaseMapper<JustAuthSocialUser> {

	/**
	 * 分页查询租户第三方用户绑定列表
	 *
	 * @param page
	 * @param justAuthSocialUserDTO
	 * @return
	 */
	Page<JustAuthSocialUserDTO> queryJustAuthSocialUserList(Page<JustAuthSocialUserDTO> page,
		@Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);

	/**
	 * 查询租户第三方用户绑定列表
	 *
	 * @param justAuthSocialUserDTO
	 * @return
	 */
	List<JustAuthSocialUserDTO> queryJustAuthSocialUserList(
		@Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);

	/**
	 * 查询租户第三方用户绑定信息
	 *
	 * @param justAuthSocialUserDTO
	 * @return
	 */
	JustAuthSocialUserDTO queryJustAuthSocialUser(
		@Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);
}
