package com.taotao.cloud.sys.api.vo.logistics;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流信息
 */
@Data
@Builder
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
