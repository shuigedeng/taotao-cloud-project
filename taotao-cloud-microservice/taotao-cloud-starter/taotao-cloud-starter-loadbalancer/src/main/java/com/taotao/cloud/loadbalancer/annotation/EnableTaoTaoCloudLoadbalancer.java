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
 package com.taotao.cloud.loadbalancer.annotation;

 import com.taotao.cloud.loadbalancer.version.VersionLoadbalancerAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

 /**
  *
  *
  * @author shuigedeng
  * @version 1.0.0
  * @since 2020/4/5 13:40
  */
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.RUNTIME)
 @LoadBalancerClients(defaultConfiguration = VersionLoadbalancerAutoConfiguration.class)
 public @interface EnableTaoTaoCloudLoadbalancer {

 }
