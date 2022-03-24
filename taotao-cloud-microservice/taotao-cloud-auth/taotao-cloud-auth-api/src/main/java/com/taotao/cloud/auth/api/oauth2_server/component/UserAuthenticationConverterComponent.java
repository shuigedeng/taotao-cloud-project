///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2_server.component;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//
///**
// * UserAuthenticationConverterComponent<br>
// *
// * @author shuigedeng
// * @since 2020/6/12 09:15
// * @version 2022.03
// */
//public class UserAuthenticationConverterComponent extends DefaultUserAuthenticationConverter {
//    @Override
//    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
//        Map<String, Object> response = new LinkedHashMap<>();
////        response.put(USERNAME, authentication.getPrincipal());
////        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
////            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
////        }
//        return response;
//    }
//}
