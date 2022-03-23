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
package com.taotao.cloud.sys.api.vo.file;

/**
 * UploadFileVO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/20 13:58
 */
public class UploadFileVO {
	private Long id;
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public static UploadFileVOBuilder builder() {
		return new UploadFileVOBuilder();
	}

	public static final class UploadFileVOBuilder {

		private Long id;
		private String url;

		private UploadFileVOBuilder() {
		}



		public UploadFileVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UploadFileVOBuilder url(String url) {
			this.url = url;
			return this;
		}

		public UploadFileVO build() {
			UploadFileVO uploadFileVO = new UploadFileVO();
			uploadFileVO.setId(id);
			uploadFileVO.setUrl(url);
			return uploadFileVO;
		}
	}
}
