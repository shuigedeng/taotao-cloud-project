package com.taotao.cloud.log.api.api.common;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is Description
 *
 * @since 2022/05/04
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PageResult<T> implements Serializable {

	private int page;

	private int size;

	private List<T> data;

}
