package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 第三方用户信息 Mapper 接口
 * </p>
 *
 * @since 2022-05-23
 */
public interface JustAuthSocialMapper extends BaseMapper<JustAuthSocial> {

	/**
	 * 分页查询第三方用户信息列表
	 *
	 * @param page
	 * @param justAuthSocialDTO
	 * @return
	 */
	Page<JustAuthSocialDTO> queryJustAuthSocialList(Page<JustAuthSocialDTO> page,
		@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

	/**
	 * 查询第三方用户信息列表
	 *
	 * @param justAuthSocialDTO
	 * @return
	 */
	List<JustAuthSocialDTO> queryJustAuthSocialList(
		@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

	/**
	 * 查询第三方用户绑定的系统用户id
	 *
	 * @param justAuthSocialDTO
	 * @return
	 */
	Long queryUserIdBySocial(@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

	/**
	 * 查询第三方用户信息信息
	 *
	 * @param justAuthSocialDTO
	 * @return
	 */
	JustAuthSocialDTO queryJustAuthSocial(
		@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);
}
