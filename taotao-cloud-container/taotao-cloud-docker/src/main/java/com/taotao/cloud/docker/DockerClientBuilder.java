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
package com.taotao.cloud.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.EventsResultCallback;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * DockerClient
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/08 14:21
 */
public class DockerClientBuilder {

	public static void main(String[] args) throws InterruptedException, IOException {
		DockerClient dockerClient = createDockerClient();

		// docker info
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);

		// 搜索镜像
		List<SearchItem> dockerSearch = dockerClient.searchImagesCmd("busybox").exec();
		System.out.println("Search returned" + dockerSearch.toString());

		// docker pull
		dockerClient.pullImageCmd("busybox:latest").exec(new ResultCallback<PullResponseItem>() {
			@Override
			public void onStart(Closeable closeable) {

			}

			@Override
			public void onNext(PullResponseItem object) {
				System.out.println(object.getStatus());
			}

			@Override
			public void onError(Throwable throwable) {
				throwable.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("pull finished");
			}

			@Override
			public void close() throws IOException {

			}
		});

		// 创建container 并测试
		// create
		CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
			.withCmd("/bin/bash")
			.exec();
		// start
		dockerClient.startContainerCmd(container.getId()).exec();
		dockerClient.stopContainerCmd(container.getId()).exec();
		dockerClient.removeContainerCmd(container.getId()).exec();

		EventsResultCallback callback = new EventsResultCallback() {
			@Override
			public void onNext(Event event) {
				System.out.println("Event: " + event);
				super.onNext(event);
			}
		};

		dockerClient.eventsCmd().exec(callback).awaitCompletion().close();
	}

	public static DockerClient createDockerClient() {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
			.withDockerHost("tcp://my-docker-host.tld:2376")
			.withDockerTlsVerify(true)
			.withDockerCertPath("/home/user/.docker/certs")
			.withDockerConfig("/home/user/.docker")
			.withApiVersion("1.30") // optional
			.withRegistryUrl("https://index.docker.io/v1/")
			.withRegistryUsername("dockeruser")
			.withRegistryPassword("ilovedocker")
			.withRegistryEmail("dockeruser@github.com")
			.build();

		DockerHttpClient httpClient = new OkDockerHttpClient.Builder()
			.dockerHost(config.getDockerHost())
			.sslConfig(config.getSSLConfig())
			.build();

		return DockerClientImpl.getInstance(config, httpClient);
	}


}
