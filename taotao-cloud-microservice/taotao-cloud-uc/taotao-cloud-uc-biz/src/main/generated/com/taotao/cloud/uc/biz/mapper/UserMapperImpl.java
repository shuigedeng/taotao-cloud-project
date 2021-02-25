package com.taotao.cloud.uc.biz.mapper;

import com.taotao.cloud.uc.api.dto.user.UserDTO;
import com.taotao.cloud.uc.api.vo.user.AddUserVO;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import com.taotao.cloud.uc.biz.entity.SysUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-20T16:59:50+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserVO sysUserToUserVO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        UserVO userVO = new UserVO();

        userVO.setId( sysUser.getId() );
        userVO.setNickname( sysUser.getNickname() );
        userVO.setUsername( sysUser.getUsername() );
        userVO.setPhone( sysUser.getPhone() );
        userVO.setType( sysUser.getType() );
        userVO.setSex( sysUser.getSex() );
        userVO.setEmail( sysUser.getEmail() );
        userVO.setDeptId( sysUser.getDeptId() );
        userVO.setJobId( sysUser.getJobId() );
        userVO.setAvatar( sysUser.getAvatar() );
        userVO.setCreateTime( sysUser.getCreateTime() );
        userVO.setLastModifiedTime( sysUser.getLastModifiedTime() );

        return userVO;
    }

    @Override
    public AddUserVO sysUserToAddUserVO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        AddUserVO addUserVO = new AddUserVO();

        addUserVO.setUsername( sysUser.getUsername() );
        addUserVO.setPhone( sysUser.getPhone() );
        addUserVO.setPassword( sysUser.getPassword() );

        return addUserVO;
    }

    @Override
    public List<UserVO> sysUserToUserVO(List<SysUser> userList) {
        if ( userList == null ) {
            return null;
        }

        List<UserVO> list = new ArrayList<UserVO>( userList.size() );
        for ( SysUser sysUser : userList ) {
            list.add( sysUserToUserVO( sysUser ) );
        }

        return list;
    }

    @Override
    public SysUser userDtoToSysUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setNickname( userDTO.getNickname() );
        sysUser.setUsername( userDTO.getUsername() );
        sysUser.setPhone( userDTO.getPhone() );
        if ( userDTO.getType() != null ) {
            sysUser.setType( userDTO.getType() );
        }
        if ( userDTO.getSex() != null ) {
            sysUser.setSex( userDTO.getSex() );
        }
        sysUser.setEmail( userDTO.getEmail() );
        if ( userDTO.getDeptId() != null ) {
            sysUser.setDeptId( userDTO.getDeptId().longValue() );
        }
        if ( userDTO.getJobId() != null ) {
            sysUser.setJobId( userDTO.getJobId().longValue() );
        }
        sysUser.setAvatar( userDTO.getAvatar() );

        return sysUser;
    }

    @Override
    public void copyUserDtoToSysUser(UserDTO userDTO, SysUser user) {
        if ( userDTO == null ) {
            return;
        }

        user.setNickname( userDTO.getNickname() );
        user.setUsername( userDTO.getUsername() );
        user.setPhone( userDTO.getPhone() );
        if ( userDTO.getType() != null ) {
            user.setType( userDTO.getType() );
        }
        if ( userDTO.getSex() != null ) {
            user.setSex( userDTO.getSex() );
        }
        user.setEmail( userDTO.getEmail() );
        if ( userDTO.getDeptId() != null ) {
            user.setDeptId( userDTO.getDeptId().longValue() );
        }
        else {
            user.setDeptId( null );
        }
        if ( userDTO.getJobId() != null ) {
            user.setJobId( userDTO.getJobId().longValue() );
        }
        else {
            user.setJobId( null );
        }
        user.setAvatar( userDTO.getAvatar() );
    }
}
