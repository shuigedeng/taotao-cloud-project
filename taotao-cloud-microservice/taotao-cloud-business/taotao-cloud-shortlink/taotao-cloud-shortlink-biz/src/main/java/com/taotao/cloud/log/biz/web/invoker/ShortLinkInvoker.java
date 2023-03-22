package com.taotao.cloud.log.biz.web.invoker;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.log.api.api.service.ShortLinkService;
import com.taotao.cloud.log.biz.web.utils.CommonBizUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
@Slf4j
@Component
public class ShortLinkInvoker {

	@DubboReference(version = "1.0.0", timeout = 5000)
	private ShortLinkService shortLinkService;


	public List<ShortLinkDTO> listShortLinkCode(ShortLinkListRequest request) {
		try {
			CommonResponse<List<ShortLinkDTO>> response = shortLinkService.listShortLinkCode(
				request);
			if (CommonBizUtil.isSuccessResponse(response)) {
				return Optional.ofNullable(response.getData()).orElse(Collections.emptyList());
			}

			log.warn("listShortLinkCode: 查短链失败, request -> {}",
				JSONObject.toJSONString(request));
		} catch (Exception e) {
			log.warn("listShortLinkCode: 查短链错误, request -> {},e -> {}",
				JSONObject.toJSONString(request), e.toString());
		}

		return Collections.emptyList();
	}

}
