package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OfficialAccountsContentModel extends ContentModel {

	/**
	 * 模板消息发送的数据
	 */
	Map<String, String> map;

	/**
	 * 模板消息跳转的url
	 */
	String url;

}
