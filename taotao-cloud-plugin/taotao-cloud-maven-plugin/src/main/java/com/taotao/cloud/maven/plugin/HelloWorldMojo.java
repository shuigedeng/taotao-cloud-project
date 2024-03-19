/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.taotao.cloud.maven.plugin;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Prints a message.
 *
 * @author Andrea Di Giorgi
 * @goal hello-world
 */

@Mojo(name = "hello-world")
public class HelloWorldMojo extends AbstractMojo {

	@Override
	public void execute() {
		Log log = getLog();

		if (log.isInfoEnabled()) {
			String message = _message;

			if (_upperCase) {
				message = message.toUpperCase();
			}

			log.info("Message from " + baseDir + ": " + message);
		}
	}

	/**
	 * The message to print.
	 *
	 * @parameter
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * @parameter
	 */
	public void setUpperCase(boolean upperCase) {
		_upperCase = upperCase;
	}

	/**
	 * @parameter default-value="${project.basedir}
	 * @readonly
	 */
	protected File baseDir;

	private String _message = "Hello, World!";
	private boolean _upperCase;

}
