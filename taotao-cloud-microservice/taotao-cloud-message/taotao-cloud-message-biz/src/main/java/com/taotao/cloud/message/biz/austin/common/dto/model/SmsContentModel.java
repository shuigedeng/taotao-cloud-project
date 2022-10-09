package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * <p>
 * 短信内容模型
 * <p>
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmsContentModel extends ContentModel {

	/**
	 * 短信发送内容
	 */
	private String content;

	/**
	 * 短信发送链接
	 */
	private String url;

}
