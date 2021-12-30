//package com.taotao.cloud.auth.api.tmp.service;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserDetailsServiceImpl implements UserDetailsService {
////    private final UserMapper userMapper;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        AuthUser user = userMapper.selectOne(Wrappers.<AuthUser>lambdaQuery()
////                .eq(AuthUser::getUsername, username));
////
////        if (user == null) {
////            throw new UsernameNotFoundException("用户不存在");
////        }
////
////        return AuthUtils.translate(user);
//		UserDetails user = User.withDefaultPasswordEncoder()
//			.username("admin")
//			.password("123456")
//			.roles("USER")
//			.build();
//		return user;
//	}
//}
