package com.taotao.cloud.log.api.api.service;


import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.api.api.common.PageResult;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.request.ShortLinkCreateRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkTimePageRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkUrlQueryRequest;
import java.util.List;

/**
 * 短链服务 - dubbo api
 * <p>
 * version: 1.0.0
 *
 * @since 2022/05/03
 */
public interface ShortLinkService {

	/**
	 * 创建短链
	 *
	 * @param request @{@link ShortLinkCreateRequest}
	 * @return @{@link ShortLinkDTO} 短链DTO
	 */
	Result<Boolean> createShortLinkCode(ShortLinkCreateRequest request);

	/**
	 * 批量查询短链 - by code
	 *
	 * @param request @{@link ShortLinkListRequest}
	 * @return @{@link ShortLinkDTO} 短链DTO
	 */
	Result<List<ShortLinkDTO>> listShortLinkCode(ShortLinkListRequest request);

	/**
	 * 查询短链 - by originUrl
	 *
	 * @param request @{@link ShortLinkUrlQueryRequest}
	 * @return @{@link ShortLinkDTO} 短链DTO
	 */
	Result<List<ShortLinkDTO>> getShortLinkCodeByOriginUrl(ShortLinkUrlQueryRequest request);

	/**
	 * 分页查询短链 - by time
	 *
	 * @param request @{@link ShortLinkTimePageRequest}
	 * @return @{@link ShortLinkDTO} 短链DTO
	 */
	Result<PageResult<ShortLinkDTO>> pageShortLinkByTime(ShortLinkTimePageRequest request);

	// TODO 根据创建人分页查询
	// TODO 根据备注模糊查询
}
