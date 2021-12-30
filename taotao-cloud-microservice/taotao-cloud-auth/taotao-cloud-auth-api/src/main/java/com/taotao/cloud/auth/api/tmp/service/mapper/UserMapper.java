//package com.taotao.cloud.oauth2.biz.service.mapper;
//
//
//import com.taotao.cloud.oauth2.biz.AuthUser;
//import java.util.List;
//
///**
// */
//public interface UserMapper extends BaseMapper<AuthUser> {
//
//    List<Integer> roleIds(@Param("userId") Integer userId);
//
//    List<UpmsRole> roles(@Param("userId") Integer userId);
//
//    List<String> roleNames(@Param("userId") Integer userId);
//
//    List<UpmsAuthority> authorities(@Param("userId") Integer userId, @Param("type") Integer type);
//
//    List<String> authorityNames(@Param("userId") Integer userId, @Param("type") Integer type);
//
//    IPage<UpmsUser> getPage(@Param("page") Page<UpmsUser> page, @Param("dataScope") DataScope dataScope,
//                            @Param("username") String username,
//                            @Param("phone") String phone,
//                            @Param("deptId") Integer deptId,
//                            @Param("enabled") Boolean enabled);
//}
