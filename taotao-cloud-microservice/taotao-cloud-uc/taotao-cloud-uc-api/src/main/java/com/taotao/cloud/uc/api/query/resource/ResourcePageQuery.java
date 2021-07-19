package com.taotao.cloud.uc.api.query.resource;

import com.taotao.cloud.common.model.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 资源分页查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "ResourcePageQuery", description = "资源查询query")
public class ResourcePageQuery extends BasePageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

}
