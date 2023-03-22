package com.taotao.cloud.log.api.api.common;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * This is Description
 *
 * @since 2022/05/04
 */
@Getter
@Setter
public class PageRequest implements Serializable {

	private int pageNo = 1;

	private int pageSize = 15;
}
