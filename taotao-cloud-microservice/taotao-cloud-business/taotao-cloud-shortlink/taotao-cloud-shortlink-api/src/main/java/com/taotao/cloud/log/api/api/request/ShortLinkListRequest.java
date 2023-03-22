package com.taotao.cloud.log.api.api.request;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链服务 - 批量查询短链 - Request
 *
 * @since 2022/05/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkListRequest implements Serializable {

	private Set<String> shortLinkCodeSet;

}
