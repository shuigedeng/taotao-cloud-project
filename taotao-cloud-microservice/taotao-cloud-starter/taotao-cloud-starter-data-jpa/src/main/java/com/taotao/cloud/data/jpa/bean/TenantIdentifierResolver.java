// /*
//  * Copyright 2017-2020 original authors
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  * https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.data.jpa;
//
// import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Lazy;
//
// /**
//  * jpa 多租户识别解析器
//  *
//  * @author dengtao
//  * @date 2020/9/28 17:29
//  * @since v1.0.0
//  */
// public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
//     private final DatabaseManager databaseManager;
//
//
//     public TenantIdentifierResolver(@Autowired @Lazy final DatabaseManager databaseManager) {
//         this.databaseManager = databaseManager;
//     }
//
//     @Override
//     public String resolveCurrentTenantIdentifier() {
//         if (StringUtils.isBlank(TenantContext.getCurrentTenant())) {
//             return databaseManager.getDefaultSchemaName();
//         }
//
//         return databaseManager.getSchemaNameByCompanyId(TenantContext.getCurrentTenant());
//     }
//
//     @Override
//     public boolean validateExistingCurrentSessions() {
//         return true;
//     }
// }
