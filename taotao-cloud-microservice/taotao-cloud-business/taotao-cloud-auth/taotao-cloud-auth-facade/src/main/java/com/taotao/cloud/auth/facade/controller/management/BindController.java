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

package com.taotao.cloud.auth.facade.controller.management;
//
// import com.markix.dao.UserToAuthDao;
// import com.markix.entity.UserToAuthPO;
// import com.markix.security.oauth2login.ThirdPlatformType;
// import com.markix.security.oauth2login.WebAttributes;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.oauth2.client.registration.ClientRegistration;
// import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.annotation.PostConstruct;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpSession;
// import java.util.*;
// import java.util.stream.Collectors;
//
/// **
// * @author markix
// */
// @RestController
// public class BindController {
//
//    @Autowired
//    private UserToAuthDao userToAuthDao;
//    @Autowired
//    private InMemoryClientRegistrationRepository clientRegistrationRepository;
//
//    /**
//     * 三方平台信息
//     */
//    private static final List<OAuthType> OAUTH_TYPES = new ArrayList<>();
//
//    @GetMapping("/oauth-types")
//    public List<OAuthType> oauthTypes() {
//        return OAUTH_TYPES;
//    }
//
//
//    /**
//     * 当前用户和三方帐号的关联信息
//     */
//    @GetMapping("/binds")
//    public List<UserToAuthPO> binds() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userToAuthDao.findByUserId(username);
//    }
//
//    /**
//     * 移除关联
//     */
//    @DeleteMapping("/unbind/{id}")
//    public void unbind(@PathVariable String id) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        userToAuthDao.deleteByIdAndUserId(id, username);
//    }
//
//    /**
//     * 未绑定的平台信息
//     */
//    @GetMapping("/not-bind-types")
//    public List<OAuthType> notBindType() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        List<UserToAuthPO> utas = userToAuthDao.findByUserId(username);
//        Set<String> types = utas.stream().map(UserToAuthPO::getType).collect(Collectors.toSet());
//
//        List<OAuthType> list = OAUTH_TYPES.stream().filter(item ->
// !types.contains(item.getType())).collect(Collectors.toList());
//        return list;
//    }
//
//    /**
//     * 三方登录的认证信息（仅在三方登录时未绑定用户前保留）
//     */
//    @GetMapping("/curr-oauth")
//    public Map<String, Object> currOAuth(HttpServletRequest request){
//        HttpSession session = request.getSession(false);
//        if(session != null){
//            Map<String, Object> map = new HashMap<>();
//            map.put("type", session.getAttribute(WebAttributes.THIRD_TYPE));
//            map.put("auth", session.getAttribute(WebAttributes.THIRD_AUTHENTICATION));
//            return map;
//        }
//        return Collections.EMPTY_MAP;
//    }
//
//
//
//
//    @PostConstruct
//    public void types(){
//        Iterator<ClientRegistration> it = clientRegistrationRepository.iterator();
//        while (it.hasNext()){
//            ClientRegistration client = it.next();
//            String appId = client.getRegistrationId();
//            String type = ThirdPlatformType.parse(client.getProviderDetails().getAuthorizationUri()).toString();
//            OAUTH_TYPES.add(new OAuthType(appId, type));
//        }
//    }
//
//    @Data
//    @AllArgsConstructor
//    class OAuthType {
//        String regId;
//        String type;
//    }
//
//
// }
