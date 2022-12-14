package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class WxUserBatchTaggingForm {

	@NotNull(message = "标签ID不得为空")
	private Long tagid;
	@NotNull(message = "openid列表不得为空")
	@Length(min = 1, max = 50, message = "每次处理数量1-50个")
	private String[] openidList;
}
