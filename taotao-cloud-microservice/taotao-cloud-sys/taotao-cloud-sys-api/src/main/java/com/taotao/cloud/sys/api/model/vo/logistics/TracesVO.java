package com.taotao.cloud.sys.api.model.vo.logistics;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.*;

/**
 * 物流信息
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TracesVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

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
	private List<Map<String, Object>> traces;
}
