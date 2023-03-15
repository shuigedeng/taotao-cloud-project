package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;


/**
 * <p>
 * 第三方用户绑定
 * </p>
 *
 * @since 2022-05-19
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@Schema(description = "第三方用户绑定数据导出")
public class JustAuthSocialUserExport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id")
	@ExcelProperty(value = "用户id", index = 0)
	@ColumnWidth(20)
	private Long userId;

	@Schema(description = "第三方用户id")
	@ExcelProperty(value = "第三方用户id", index = 1)
	@ColumnWidth(20)
	private Long socialId;
}
