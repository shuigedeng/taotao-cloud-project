//package com.taotao.cloud.auth.api.tmp.social.user;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//public interface CustomOAuth2User extends OAuth2User {
//
//    default String getNickname() {
//        return getName();
//    }
//
//    default String getAvatar() {
//        return null;
//    }
//
//    // 这里只是保存用户在第三方应用上的权限,对于我们来说无用
//    @Override
//    default Map<String, Object> getAttributes() {
//        return new HashMap<>();
//    }
//
//    // 这里只是保存用户在第三方应用上的权限,对于我们来说无用
//    @Override
//   default Collection<? extends GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.createAuthorityList("ROLE_USER");
//    }
//}
