package com.taotao.cloud.sa.just.biz.just.justauth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSource;
import java.util.List;


/**
 * <p>
 * 租户第三方登录信息配置表 服务类
 * </p>
 *
 * @since 2022-05-19
 */
public interface IJustAuthSourceService extends IService<JustAuthSource> {

	/**
	 * 分页查询租户第三方登录信息配置表列表
	 *
	 * @param page
	 * @param queryJustAuthSourceDTO
	 * @return
	 */
	Page<JustAuthSourceDTO> queryJustAuthSourceList(Page<JustAuthSourceDTO> page,
		QueryJustAuthSourceDTO queryJustAuthSourceDTO);

	/**
	 * 查询租户第三方登录信息配置表列表
	 *
	 * @param queryJustAuthSourceDTO
	 * @return
	 */
	List<JustAuthSourceDTO> queryJustAuthSourceList(QueryJustAuthSourceDTO queryJustAuthSourceDTO);

	/**
	 * 查询租户第三方登录信息配置表详情
	 *
	 * @param queryJustAuthSourceDTO
	 * @return
	 */
	JustAuthSourceDTO queryJustAuthSource(QueryJustAuthSourceDTO queryJustAuthSourceDTO);

	/**
	 * 创建租户第三方登录信息配置表
	 *
	 * @param justAuthSource
	 * @return
	 */
	boolean createJustAuthSource(CreateJustAuthSourceDTO justAuthSource);

	/**
	 * 更新租户第三方登录信息配置表
	 *
	 * @param justAuthSource
	 * @return
	 */
	boolean updateJustAuthSource(UpdateJustAuthSourceDTO justAuthSource);

	/**
	 * 删除租户第三方登录信息配置表
	 *
	 * @param justAuthSourceId
	 * @return
	 */
	boolean deleteJustAuthSource(Long justAuthSourceId);

	/**
	 * 批量删除租户第三方登录信息配置表
	 *
	 * @param justAuthSourceIds
	 * @return
	 */
	boolean batchDeleteJustAuthSource(List<Long> justAuthSourceIds);

	/**
	 * 初始化配置表列表
	 *
	 * @return
	 */
	void initJustAuthSourceList();
}
