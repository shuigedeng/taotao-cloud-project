/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.exception;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity.ConnectionKey;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * Thrown by a {@link UsersConnectionRepository} when attempting to add a {@link ConnectionData} and a connection
 * already exists with the given key.
 * @author Keith Donald
 * @see UsersConnectionRepository#addConnection(ConnectionData)
 */
public final class DuplicateConnectionException extends ConnectionRepositoryException {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	private final ConnectionKey connectionKey;

	public DuplicateConnectionException(ConnectionKey connectionKey) {
		super("The connection with key " + connectionKey + " already exists");
		this.connectionKey = connectionKey;
	}

	/**
	 * The connection key that already exists.
	 * @return The connection key that already exists.
	 */
	@SuppressWarnings("unused")
	public ConnectionKey getConnectionKey() {
		return connectionKey;
	}
}
