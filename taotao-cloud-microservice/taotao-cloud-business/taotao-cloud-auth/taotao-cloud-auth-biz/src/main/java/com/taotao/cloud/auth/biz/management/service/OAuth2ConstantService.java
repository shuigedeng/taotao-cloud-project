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

package com.taotao.cloud.auth.biz.management.service;

import com.taotao.boot.security.spring.enums.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * <p>OAuth2 常量服务 </p>
 *
 *
 * @since : 2022/3/17 14:36
 */
@Service
public class OAuth2ConstantService {

    private static final List<Map<String, Object>> APPLICATION_TYPE_ENUM =
            ApplicationType.getPreprocessedJsonStructure();
    private static final List<Map<String, Object>> GRANT_TYPE_ENUM =
            GrantType.getPreprocessedJsonStructure();
    private static final List<Map<String, Object>> SIGNATURE_ENUM =
            Signature.getPreprocessedJsonStructure();
    private static final List<Map<String, Object>> AUTHENTICATION_METHOD_ENUM =
            AuthenticationMethod.getPreprocessedJsonStructure();
    private static final List<Map<String, Object>> PERMISSION_EXPRESSION_ENUM =
            PermissionExpression.getPreprocessedJsonStructure();

    //    private static final List<Map<String, Object>> DATABASE_ENUM =
    // Database.getPreprocessedJsonStructure();
    //    private static final List<Map<String, Object>> SERVER_DEVICE_ENUM =
    // ServerDevice.getPreprocessedJsonStructure();

    public Map<String, Object> getAllEnums() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("applicationType", APPLICATION_TYPE_ENUM);
        map.put("grantType", GRANT_TYPE_ENUM);
        map.put("signature", SIGNATURE_ENUM);
        map.put("permissionExpression", PERMISSION_EXPRESSION_ENUM);
        map.put("authenticationMethod", AUTHENTICATION_METHOD_ENUM);
        //        map.put("database", DATABASE_ENUM);
        //        map.put("serverDevice", SERVER_DEVICE_ENUM);
        return map;
    }
}
