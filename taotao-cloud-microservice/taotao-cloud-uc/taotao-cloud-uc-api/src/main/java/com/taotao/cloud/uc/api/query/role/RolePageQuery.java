package com.taotao.cloud.uc.api.query.role;

import com.taotao.cloud.core.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 角色分页查询query
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色分页查询query")
public class RolePageQuery extends BasePageQuery {

    private static final long serialVersionUID = -7605952923416404638L;

}
