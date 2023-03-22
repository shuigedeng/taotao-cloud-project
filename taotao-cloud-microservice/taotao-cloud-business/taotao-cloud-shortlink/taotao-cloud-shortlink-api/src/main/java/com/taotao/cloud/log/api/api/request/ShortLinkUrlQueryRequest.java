package com.taotao.cloud.log.api.api.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is Description
 *
 * @since 2022/05/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkUrlQueryRequest implements Serializable {

	private String originUrl;

}
