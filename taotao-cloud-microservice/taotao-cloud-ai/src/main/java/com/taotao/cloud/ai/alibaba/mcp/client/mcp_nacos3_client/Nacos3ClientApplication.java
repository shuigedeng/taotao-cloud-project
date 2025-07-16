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

package com.taotao.cloud.ai.alibaba.mcp.client.mcp_nacos3_client;

import java.util.Scanner;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @date 2025/6/4 18:09
 */
@SpringBootApplication
public class Nacos3ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nacos3ClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(
            ChatClient.Builder chatClientBuilder,
            @Qualifier("loadbalancedMcpAsyncToolCallbacks") ToolCallbackProvider tools,
            ConfigurableApplicationContext context) {

        return args -> {
            var chatClient = chatClientBuilder.defaultToolCallbacks(tools).build();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\n>>> QUESTION: ");
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                if (userInput.isEmpty()) {
                    userInput = "北京时间现在几点钟";
                }
                System.out.println(
                        "\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());
            }
            scanner.close();
            context.close();
        };
    }
}
