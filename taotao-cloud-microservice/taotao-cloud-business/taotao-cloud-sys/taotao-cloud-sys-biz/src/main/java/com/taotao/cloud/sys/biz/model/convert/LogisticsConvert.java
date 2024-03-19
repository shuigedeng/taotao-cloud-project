/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.model.convert;

import com.taotao.cloud.sys.biz.model.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.biz.model.entity.config.LogisticsConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * ilogistics地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:34
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogisticsConvert {

    /** 实例 */
    LogisticsConvert INSTANCE = Mappers.getMapper(LogisticsConvert.class);

    /**
     * 物流文件签证官
     *
     * @param logisticsConfig 物流配置
     * @return {@link LogisticsVO }
     * @since 2022-04-28 13:39:35
     */
    LogisticsVO convert(LogisticsConfig logisticsConfig);

    // /**
    //  * SysUser转AddUserVO
    //  *
    //  * @param sysUser sysUser
    //  * @return com.taotao.cloud.sys.api.vo.user.AddUserVO
    //  * @since 2020/11/11 16:59
    //  */
    // AddUserVO sysUserToAddUserVO(SysUser sysUser);
    //
    // /**
    //  * list -> SysUser转UserVO
    //  *
    //  * @param userList userList
    //  * @return java.util.List<com.taotao.cloud.sys.api.vo.user.UserVO>
    //  * @since 2020/11/11 15:00
    //  */
    // List<UserVO> sysUserToUserVO(List<SysUser> userList);
    //
    // /**
    //  * UserDTO转SysUser
    //  *
    //  * @param userDTO userDTO
    //  * @return com.taotao.cloud.sys.biz.entity.SysUser
    //  * @since 2020/11/11 14:52
    //  */
    // SysUser userDtoToSysUser(UserDTO userDTO);
    //
    // /**
    //  * 拷贝 UserDTO 到SysUser
    //  *
    //  * @param userDTO userDTO
    //  * @param user    user
    //  * @return void
    //  * @since 2020/11/11 16:59
    //  */
    // void copyUserDtoToSysUser(UserDTO userDTO, @MappingTarget SysUser user);
}
