package com.taotao.cloud.payment.biz.bootx.dto.paymodel.union;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* @author xxm
* @date 2022/3/11
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "云闪付配置")
public class UnionPayConfigDto extends BaseDto {
}
