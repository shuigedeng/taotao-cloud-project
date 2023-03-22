package com.taotao.cloud.log.biz.web.service;

import com.taotao.cloud.log.biz.web.web.request.ShortLinkUpdateRequest;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
public interface IShortLinkService {

	Optional<String> parseShortLinkCode(String shortLinkCode);

	Boolean updateShortLinkCode(ShortLinkUpdateRequest request);

}
