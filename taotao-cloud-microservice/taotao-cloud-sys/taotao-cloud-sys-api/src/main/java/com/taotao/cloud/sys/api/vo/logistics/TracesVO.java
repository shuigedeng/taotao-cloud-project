package com.taotao.cloud.sys.api.vo.logistics;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TracesVO {

	/**
	 * 物流公司
	 */
	private String shipper;

	/**
	 * 物流单号
	 */
	private String logisticCode;

	/**
	 * 物流详细信息
	 */
	private List<Map> traces;
}
