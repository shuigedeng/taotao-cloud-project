package com.taotao.cloud.goods.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class SelectorOptions {

	private String name;

	private String value;

	private String url;

	private List<SelectorOptions> otherOptions;


}
