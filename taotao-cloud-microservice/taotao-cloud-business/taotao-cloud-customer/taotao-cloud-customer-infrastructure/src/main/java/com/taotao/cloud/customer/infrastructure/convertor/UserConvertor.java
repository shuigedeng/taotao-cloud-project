

package com.taotao.cloud.member.infratructure.convertor;

import org.laokou.auth.domain.user.User;
import org.laokou.auth.dto.user.clientobject.UserCO;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.dto.Convertor;
import org.mapstruct.Mapper;

import static org.laokou.common.i18n.common.SysConstants.SPRING;

/**
 * 用户转换器.
 *
 *
 */
@Mapper(componentModel = SPRING)
public interface UserConvertor extends Convertor<UserCO, User, UserDO> {

}
