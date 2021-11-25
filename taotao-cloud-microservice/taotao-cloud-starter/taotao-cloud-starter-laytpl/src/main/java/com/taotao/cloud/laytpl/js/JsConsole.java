/*
 * Copyright 2002-2021 the original author or authors.
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

package com.taotao.cloud.laytpl.js;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 映射 js 中的 console
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class JsConsole {

	private static Logger log = LoggerFactory.getLogger(JsConsole.class);


	public void debug() {
		log.debug("debug by console.");
	}

	public void debug(String message) {
		log.debug(message);
	}

	public void debug(String message, Object... args) {
		log.debug(message, args);
	}

	public void log() {
		log.info("log by console.");
	}

	public void log(String message) {
		log.info(message);
	}

	public void log(String message, Object... args) {
		log.info(message, args);
	}

	public void info() {
		log.info("info by console.");
	}

	public void info(String message) {
		log.info(message);
	}

	public void info(String message, Object... args) {
		log.info(message, args);
	}

	public void warn() {
		log.warn("warn by console.");
	}

	public void warn(String message) {
		log.warn(message);
	}

	public void warn(String message, Object... args) {
		log.warn(message, args);
	}

	public void trace() {
		log.trace("trace by console.");
	}

	public void trace(String message) {
		log.trace(message);
	}

	public void trace(String message, Object... args) {
		log.trace(message, args);
	}

	public void error() {
		log.error("error by console.");
	}

	public void error(String message) {
		log.error(message);
	}

	public void error(String message, Object... args) {
		log.error(message, args);
	}
}
