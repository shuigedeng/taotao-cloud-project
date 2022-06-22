package com.taotao.cloud.goods.biz.elasticsearch;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorOptions {

	private String name;

	private String value;

	private String url;

	private List<SelectorOptions> otherOptions;


}
