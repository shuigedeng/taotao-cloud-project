/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.common.utils.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 资源文件处理工具类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-23 08:59:20
 */
public class ResourceUtil {

    private static volatile ResourceUtil INSTANCE;

    private final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

    private ResourceUtil() {
        this.pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    }

    private static ResourceUtil getInstance() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (ResourceUtil.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new ResourceUtil();
                }
            }
        }

        return INSTANCE;
    }

    private PathMatchingResourcePatternResolver getPathMatchingResourcePatternResolver() {
        return this.pathMatchingResourcePatternResolver;
    }

    private static PathMatchingResourcePatternResolver getResolver() {
        return getInstance().getPathMatchingResourcePatternResolver();
    }

    public static Resource getResource(String location) {
        return getResolver().getResource(location);
    }

    public static File getFile(String location) throws IOException {
        return getResource(location).getFile();
    }

    public static InputStream getInputStream(String location) throws IOException {
        return getResource(location).getInputStream();
    }

    public static String getFilename(String location) {
        return ResourceUtil.getResource(location).getFilename();
    }

    public static URI getURI(String location) throws IOException {
        return getResource(location).getURI();
    }

    public static URL getURL(String location) throws IOException {
        return getResource(location).getURL();
    }

    public static long contentLength(String location) throws IOException {
        return getResource(location).contentLength();
    }

    public static long lastModified(String location) throws IOException {
        return getResource(location).lastModified();
    }

    public static boolean exists(String location) {
        return getResource(location).exists();
    }

    public static boolean isFile(String location) {
        return getResource(location).isFile();
    }

    public static boolean isReadable(String location) {
        return getResource(location).isReadable();
    }

    public static boolean isOpen(String location) {
        return ResourceUtil.getResource(location).isOpen();
    }

    public static Resource[] getResources(String locationPattern) throws IOException {
        return getResolver().getResources(locationPattern);
    }

    public static boolean isUrl(String location) {
        return org.springframework.util.ResourceUtils.isUrl(location);
    }

    public static boolean isClasspathUrl(String location) {
        return StringUtils.startsWith(location, ResourceLoader.CLASSPATH_URL_PREFIX);
    }

    public static boolean isClasspathAllUrl(String location) {
        return StringUtils.startsWith(location, ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
    }

    public static boolean isJarUrl(URL url) {
        return org.springframework.util.ResourceUtils.isJarURL(url);
    }

    public static boolean isFileUrl(URL url) {
        return org.springframework.util.ResourceUtils.isFileURL(url);
    }
}
