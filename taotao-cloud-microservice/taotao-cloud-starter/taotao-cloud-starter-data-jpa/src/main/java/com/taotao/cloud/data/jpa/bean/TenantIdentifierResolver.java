 /*
  * Copyright 2017-2020 original authors
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * https://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.taotao.cloud.data.jpa.bean;

 import com.taotao.cloud.common.context.TenantContextHolder;
 import org.apache.commons.lang3.StringUtils;
 import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

 /**
  * jpa 多租户识别解析器
  *
  * @author shuigedeng
  * @version 1.0.0.0
  * @since 2020/9/28 17:29
  */
 public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	 //private final DatabaseManager databaseManager;
	 //
	 //public TenantIdentifierResolver(@Autowired @Lazy final DatabaseManager databaseManager) {
	 // this.databaseManager = databaseManager;
	 //}

	 @Override
	 public String resolveCurrentTenantIdentifier() {
		 //if (StringUtils.isBlank(TenantContextHolder.getTenant())) {
		 // return databaseManager.getDefaultSchemaName();
		 //}
		 //
		 //return databaseManager.getSchemaNameByCompanyId(TenantContextHolder.getTenant());
		 String tenant = TenantContextHolder.getTenant();
		 if (StringUtils.isBlank(tenant)) {
			 return "1";
		 }

		 return tenant;
	 }

	 @Override
	 public boolean validateExistingCurrentSessions() {
		 return true;
	 }
 }
