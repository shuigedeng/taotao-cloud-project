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

package com.taotao.cloud.bff.graphql.repository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ArtifactRepositoriesInitializer implements ApplicationRunner {

    // private final ArtifactRepositories repositories;
    //
    // public ArtifactRepositoriesInitializer(ArtifactRepositories repositories) {
    // 	this.repositories = repositories;
    // }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // List<ArtifactRepository> repositoryList = Arrays.asList(
        // 		new ArtifactRepository("spring-releases", "Spring Releases",
        // "https://repo.spring.io/libs-releases"),
        // 		new ArtifactRepository("spring-milestones", "Spring Milestones",
        // "https://repo.spring.io/libs-milestones"),
        // 		new ArtifactRepository("spring-snapshots", "Spring Snapshots",
        // "https://repo.spring.io/libs-snapshots"));
        // repositories.saveAll(repositoryList);
    }
}
