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

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * ifile地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:30
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileConvert {

    /** 实例 */
    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    /**
     * 文件文件签证官
     *
     * @param file 文件
     * @return {@link FileVO }
     * @since 2022-04-28 13:39:30
     */
    FileVO convert(File file);

    // /**
    //  * SysUser转AddUserVO
    //  *
    //  * @param sysUser sysUser
    //  * @return com.taotao.cloud.sys.api.vo.user.AddUserVO
    //  * @author shuigedeng
    //  * @since 2020/11/11 16:59
    //  * @version 2022.03
    //  */
    // AddUserVO sysUserToAddUserVO(SysUser sysUser);
    //
    // /**
    //  * list -> SysUser转UserVO
    //  *
    //  * @param userList userList
    //  * @return java.util.List<com.taotao.cloud.sys.api.vo.user.UserVO>
    //  * @author shuigedeng
    //  * @since 2020/11/11 15:00
    //  * @version 2022.03
    //  */
    // List<UserVO> sysUserToUserVO(List<SysUser> userList);
    //
    // /**
    //  * UserDTO转SysUser
    //  *
    //  * @param userDTO userDTO
    //  * @return com.taotao.cloud.sys.biz.entity.SysUser
    //  * @author shuigedeng
    //  * @since 2020/11/11 14:52
    //  * @version 2022.03
    //  */
    // SysUser userDtoToSysUser(UserDTO userDTO);
    //
    // /**
    //  * 拷贝 UserDTO 到SysUser
    //  *
    //  * @param userDTO userDTO
    //  * @param user    user
    //  * @return void
    //  * @author shuigedeng
    //  * @since 2020/11/11 16:59
    //  * @version 2022.03
    //  */
    // void copyUserDtoToSysUser(UserDTO userDTO, @MappingTarget SysUser user);
}
