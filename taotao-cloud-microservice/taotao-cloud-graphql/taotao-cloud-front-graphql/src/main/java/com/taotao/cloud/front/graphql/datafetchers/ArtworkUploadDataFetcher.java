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
package com.taotao.cloud.front.graphql.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

/**
 * ArtworkUploadDataFetcher
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/02/19 13:42
 */
@DgsComponent
public class ArtworkUploadDataFetcher {
	@DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.AddArtwork)
	public List<Image> uploadArtwork(@InputArgument("showId") Integer showId, @InputArgument("upload") MultipartFile multipartFile) throws IOException {
		Path uploadDir = Paths.get("uploaded-images");
		if(!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		Path newFile = uploadDir.resolve("show-" + showId + "-" + UUID.randomUUID() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
		try(OutputStream outputStream = Files.newOutputStream(newFile)) {
			outputStream.write(multipartFile.getBytes());
		}

		return Files.list(uploadDir)
			.filter(f -> f.getFileName().toString().startsWith("show-" + showId))
			.map(f -> f.getFileName().toString())
			.map(fileName -> Image.newBuilder().url(fileName).build()).collect(Collectors.toList());

	}
}
