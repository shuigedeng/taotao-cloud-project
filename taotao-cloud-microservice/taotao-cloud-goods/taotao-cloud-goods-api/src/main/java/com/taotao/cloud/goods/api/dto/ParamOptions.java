package com.taotao.cloud.goods.api.dto;

import java.util.List;
import lombok.Data;

/**
 * 参数属性选择器
 **/
@Data
public class ParamOptions {

	private String key;

	private List<String> values;

}
