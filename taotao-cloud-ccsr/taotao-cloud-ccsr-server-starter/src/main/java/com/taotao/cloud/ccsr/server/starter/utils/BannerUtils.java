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

package com.taotao.cloud.ccsr.server.starter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * BannerUtils
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class BannerUtils {

    private final ResourceLoader resourceLoader;

    public BannerUtils( ResourceLoader resourceLoader ) {
        this.resourceLoader = resourceLoader;
    }

    public void print() {
        // Load the default banner from resources
        Resource defaultBannerResource = resourceLoader.getResource("classpath:ccsr_banner.txt");
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(defaultBannerResource.getInputStream()))) {
            String line;
            while (( line = reader.readLine() ) != null) {
                System.out.println(line);
            }
        } catch (IOException ignored) {
        }
    }
}
