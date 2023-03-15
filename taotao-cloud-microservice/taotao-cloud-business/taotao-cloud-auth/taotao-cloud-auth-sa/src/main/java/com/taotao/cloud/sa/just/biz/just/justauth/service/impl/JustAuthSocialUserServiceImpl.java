package com.taotao.cloud.sa.just.biz.just.justauth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import com.taotao.cloud.sa.just.biz.just.justauth.mapper.JustAuthSocialUserMapper;
import com.taotao.cloud.sa.just.biz.just.justauth.service.IJustAuthSocialUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户第三方用户绑定 服务实现类
 * </p>
 *
 * @since 2022-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthSocialUserServiceImpl extends
	ServiceImpl<JustAuthSocialUserMapper, JustAuthSocialUser> implements
	IJustAuthSocialUserService {

	private final JustAuthSocialUserMapper justAuthSocialUserMapper;

	/**
	 * 分页查询租户第三方用户绑定列表
	 *
	 * @param page
	 * @param queryJustAuthSocialUserDTO
	 * @return
	 */
	@Override
	public Page<JustAuthSocialUserDTO> queryJustAuthSocialUserList(Page<JustAuthSocialUserDTO> page,
		QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
		Page<JustAuthSocialUserDTO> justAuthSocialUserInfoList = justAuthSocialUserMapper.queryJustAuthSocialUserList(
			page, queryJustAuthSocialUserDTO);
		return justAuthSocialUserInfoList;
	}

	/**
	 * 查询租户第三方用户绑定列表
	 *
	 * @param queryJustAuthSocialUserDTO
	 * @return
	 */
	@Override
	public List<JustAuthSocialUserDTO> queryJustAuthSocialUserList(
		QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
		List<JustAuthSocialUserDTO> justAuthSocialUserInfoList = justAuthSocialUserMapper.queryJustAuthSocialUserList(
			queryJustAuthSocialUserDTO);
		return justAuthSocialUserInfoList;
	}

	/**
	 * 查询租户第三方用户绑定详情
	 *
	 * @param queryJustAuthSocialUserDTO
	 * @return
	 */
	@Override
	public JustAuthSocialUserDTO queryJustAuthSocialUser(
		QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
		JustAuthSocialUserDTO justAuthSocialUserDTO = justAuthSocialUserMapper.queryJustAuthSocialUser(
			queryJustAuthSocialUserDTO);
		return justAuthSocialUserDTO;
	}

	/**
	 * 创建租户第三方用户绑定
	 *
	 * @param justAuthSocialUser
	 * @return
	 */
	@Override
	public JustAuthSocialUser createJustAuthSocialUser(
		CreateJustAuthSocialUserDTO justAuthSocialUser) {

		QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO = new QueryJustAuthSocialUserDTO();
		queryJustAuthSocialUserDTO.setSocialId(justAuthSocialUser.getSocialId());
		JustAuthSocialUserDTO justAuthSocialUserDTO = this.queryJustAuthSocialUser(
			queryJustAuthSocialUserDTO);
		if (null != justAuthSocialUserDTO) {
			throw new BusinessException("已经存在绑定用户，请勿重复绑定。");
		}

		JustAuthSocialUser justAuthSocialUserEntity = BeanCopierUtils.copyByClass(
			justAuthSocialUser, JustAuthSocialUser.class);
		boolean result = this.save(justAuthSocialUserEntity);
		if (!result) {
			throw new BusinessException("绑定用户失败");
		}
		return justAuthSocialUserEntity;
	}

	/**
	 * 更新租户第三方用户绑定
	 *
	 * @param justAuthSocialUser
	 * @return
	 */
	@Override
	public boolean updateJustAuthSocialUser(UpdateJustAuthSocialUserDTO justAuthSocialUser) {
		JustAuthSocialUser justAuthSocialUserEntity = BeanCopierUtils.copyByClass(
			justAuthSocialUser, JustAuthSocialUser.class);
		boolean result = this.updateById(justAuthSocialUserEntity);
		return result;
	}

	/**
	 * 删除租户第三方用户绑定
	 *
	 * @param justAuthSocialUserId
	 * @return
	 */
	@Override
	public boolean deleteJustAuthSocialUser(Long justAuthSocialUserId) {
		boolean result = this.removeById(justAuthSocialUserId);
		return result;
	}

	/**
	 * 批量删除租户第三方用户绑定
	 *
	 * @param justAuthSocialUserIds
	 * @return
	 */
	@Override
	public boolean batchDeleteJustAuthSocialUser(List<Long> justAuthSocialUserIds) {
		boolean result = this.removeByIds(justAuthSocialUserIds);
		return result;
	}
}
