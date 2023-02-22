package com.taotao.cloud.message.biz.austin.web.vo;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 消息模板的Vo
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {

	/**
	 * 返回List列表
	 */
	private List<Map<String, Object>> rows;

	/**
	 * 总条数
	 */
	private Long count;
}
