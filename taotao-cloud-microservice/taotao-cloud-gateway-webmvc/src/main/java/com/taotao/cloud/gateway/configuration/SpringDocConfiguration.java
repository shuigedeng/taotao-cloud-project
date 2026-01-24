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

package com.taotao.cloud.gateway.configuration;

// import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.taotao.boot.common.support.version.TtcVersion;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.cloud.gateway.server.mvc.config.RouteProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * springdoc 配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/03/04 13:48
 */
//@Profile(value = {"dev"})
// @EnableKnife4j
@Configuration
public class SpringDocConfiguration {

    @Bean
    public List<GroupedOpenApi> apis(
            SwaggerUiConfigProperties swaggerUiConfigParameters, GatewayMvcProperties gatewayMvcProperties) {
        List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteProperties> routeProperties = gatewayMvcProperties.getRoutes();

        // for (RouteDefinition definition : definitions) {
        //	LogUtil.info("spring cloud gateway route definition : {}, uri: {}",
        //		definition.getId(),
        //		definition.getUri().toString());
        // }

        Set<SwaggerUrl> urls = new HashSet<>();

        Optional.ofNullable(routeProperties)
                .ifPresent(
                        definition -> {
                            definition.stream()
                                    .filter(
                                            routeDefinition ->
                                                    routeDefinition
                                                            .getId()
                                                            .startsWith("taotao-cloud"))
                                    .forEach(
                                            routeDefinition -> {
                                                String id = routeDefinition.getId();

                                                Map<String, Object> metadata =
                                                        routeDefinition.getMetadata();
                                                String name = (String) metadata.get("name");

                                                SwaggerUrl url = new SwaggerUrl();
                                                url.setName(name);
                                                url.setDisplayName(name);
                                                url.setUrl("/v3/api-docs/" + id);
                                                urls.add(url);

                                                GroupedOpenApi build =
                                                        GroupedOpenApi.builder()
                                                                .pathsToMatch("/" + id + "/**")
                                                                .group(id)
                                                                .build();
                                                groups.add(build);
                                            });
                        });

        swaggerUiConfigParameters.setConfigUrl("/v3/api-docs/swagger-config");
        swaggerUiConfigParameters.setUrls(urls);
        return groups;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .tags(new ArrayList<>())
                .extensions(new HashMap<>())
                .openapi("TAOTAO CLOUD API")
                .paths(new Paths().addPathItem("", new PathItem()).addPathItem("", new PathItem()))
                .servers(new ArrayList<>())
                .security(new ArrayList<>())
                .schemaRequirement(
                        "",
                        new SecurityScheme()
                                .scheme("")
                                .description("")
                                .extensions(new HashMap<>())
                                .bearerFormat("")
                                .name(""))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("")
                                .extensions(new HashMap<>())
                                .url(""))
                .components(
                        new Components()
                                .schemas(new HashMap<>())
                                .responses(new HashMap<>())
                                .parameters(new HashMap<>())
                                .examples(new HashMap<>())
                                .requestBodies(new HashMap<>())
                                .headers(new HashMap<>())
                                .securitySchemes(new HashMap<>())
                                .links(new HashMap<>())
                                .callbacks(new HashMap<>())
                                .extensions(new HashMap<>()))
                .info(
                        new Info()
                                .title("TAOTAO CLOUD API")
                                .version(TtcVersion.getVersion())
                                .description("TAOTAO CLOUD API")
                                .extensions(new HashMap<>())
                                .contact(
                                        new Contact()
                                                .name("")
                                                .url("")
                                                .email("")
                                                .extensions(new HashMap<>()))
                                .termsOfService("")
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://springdoc.org")));
    }
}
