package com.taotao.cloud.ccsr.server.starter.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BannerUtils {

    private final ResourceLoader resourceLoader;

    public BannerUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void print() {
        // Load the default banner from resources
        Resource defaultBannerResource = resourceLoader.getResource("classpath:ccsr_banner.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(defaultBannerResource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ignored) {
        }
    }
}
