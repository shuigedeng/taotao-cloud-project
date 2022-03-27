package com.taotao.cloud.sys.api.vo.redis;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * RedisVo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:07:32
 */
@Data
@Builder
public class RedisVo implements Serializable {

	private String key;

	private String value;
}
