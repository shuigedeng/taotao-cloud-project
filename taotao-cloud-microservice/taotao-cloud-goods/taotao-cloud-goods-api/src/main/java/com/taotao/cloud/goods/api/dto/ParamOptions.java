package com.taotao.cloud.goods.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数属性选择器
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParamOptions {

	private String key;

	private List<String> values;

}
