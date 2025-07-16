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

package com.taotao.cloud.bff.graphql.project;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpringProjectsClient {

    // private static final TypeReferences.CollectionModelType<Release> releaseCollection =
    // 		new TypeReferences.CollectionModelType<Release>() {};
    //
    // private final Traverson traverson;
    //
    // public SpringProjectsClient(RestTemplateBuilder builder) {
    // 	List<HttpMessageConverter<?>> converters =
    // Traverson.getDefaultMessageConverters(MediaTypes.HAL_JSON);
    // 	RestTemplate restTemplate = builder.messageConverters(converters).build();
    // 	this.traverson = new Traverson(URI.create("https://spring.io/api/"), MediaTypes.HAL_JSON);
    // 	this.traverson.setRestOperations(restTemplate);
    // }

    public Project fetchProject(String projectSlug) {
        // return this.traverson.follow("projects")
        // 		.follow(Hop.rel("project").withParameter("id", projectSlug))
        // 		.toObject(Project.class);
        return null;
    }

    public List<Release> fetchProjectReleases(String projectSlug) {
        // CollectionModel<Release> releases = this.traverson.follow("projects")
        // 		.follow(Hop.rel("project").withParameter("id",
        // projectSlug)).follow(Hop.rel("releases"))
        // 		.toObject(releaseCollection);
        // return new ArrayList(releases.getContent());
        return null;
    }
}
