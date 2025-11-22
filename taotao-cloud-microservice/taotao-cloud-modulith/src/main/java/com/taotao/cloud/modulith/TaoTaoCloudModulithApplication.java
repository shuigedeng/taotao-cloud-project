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

package com.taotao.cloud.modulith;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.webmvc.annotation.TaoTaoBootApplication;
import org.springframework.modulith.core.ApplicationModules;

@TaoTaoBootApplication
public class TaoTaoCloudModulithApplication {

    public static void main(String[] args) {
        new StartupSpringApplication(TaoTaoCloudModulithApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-modulith")
                .setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
        verifyPackageConformity();
    }

    static void verifyPackageConformity() {

        System.out.println("\n--- 验证 Spring Modulith 模块结构 ---");

        // 1. 获取应用程序模块模型
        ApplicationModules modules = ApplicationModules.of(TaoTaoCloudModulithApplication.class);

        // 2. 打印模块信息（可选，用于观察和调试）
        System.out.println("--- 检测到的模块信息 ---");
        modules.forEach(System.out::println);
        System.out.println("--------------------");

        // 3. 验证模块是否符合模块化约束
        // 这会检查所有模块间的依赖是否符合Modulith的规则，例如：
        // - 没有循环依赖
        // - 模块没有直接访问其他模块的内部类
        modules.verify();

        System.out.println("--- 模块结构验证成功！---");
        System.out.println("--- 验证 Spring Modulith 模块结构完毕 ---\n");
    }
}
