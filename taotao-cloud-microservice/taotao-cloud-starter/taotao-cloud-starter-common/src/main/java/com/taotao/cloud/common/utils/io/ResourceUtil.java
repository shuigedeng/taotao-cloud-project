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

package com.taotao.cloud.common.utils.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
        return getResource(location).getFilename();
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
        return getResource(location).isOpen();
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

	/**
	 * 根据文件路径读取文件内容
	 *
	 * @param fileInPath
	 * @throws IOException
	 */
	public static void getFileContent(Object fileInPath) throws IOException {
		BufferedReader br = null;
		if (fileInPath == null) {
			return;
		}
		if (fileInPath instanceof String) {
			br = new BufferedReader(new FileReader((String) fileInPath));
		} else if (fileInPath instanceof InputStream) {
			br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
		}
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}

	/**
	 * 方式一
	 *
	 * 主要核心方法是使用getResource和getPath方法，这里的getResource("")里面是空字符串
	 */
	public void function1(String fileName) throws IOException {
		String path = this.getClass().getClassLoader().getResource(fileName).getPath();//注意getResource("")里面是空字符串
		System.out.println(path);
		String filePath = path + fileName;
		System.out.println(filePath);
		getFileContent(filePath);
	}


	/**
	 * 方式二
	 *
	 * 主要核心方法是使用getResource和getPath方法，直接通过getResource(fileName)方法获取文件路径，注意如果是路径中带有中文一定要使用URLDecoder.decode解码
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void function2(String fileName) throws IOException {
		String path = this.getClass().getClassLoader().getResource(fileName).getPath();//注意getResource("")里面是空字符串
		System.out.println(path);
		String filePath = URLDecoder.decode(path, StandardCharsets.UTF_8);//如果路径中带有中文会被URLEncoder,因此这里需要解码
		System.out.println(filePath);
		getFileContent(filePath);
	}

	/**
	 * 直接通过文件名+getFile()来获取
	 *
	 * 方式三
	 *
	 * 直接通过文件名+getFile()来获取文件。如果是文件路径的话getFile和getPath效果是一样的，如果是URL路径的话getPath是带有参数的路径
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void function3(String fileName) throws IOException {
		String path = this.getClass().getClassLoader().getResource(fileName).getFile();//注意getResource("")里面是空字符串
		System.out.println(path);
		String filePath = URLDecoder.decode(path, StandardCharsets.UTF_8);//如果路径中带有中文会被URLEncoder,因此这里需要解码
		System.out.println(filePath);
		getFileContent(filePath);
	}

	/**
	 * 直接使用getResourceAsStream方法获取流
	 * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
	 *直接使用getResourceAsStream方法获取流，上面的几种方式都需要获取文件路径，但是在SpringBoot中所有文件都在jar包中，没有一个实际的路径，因此可以使用以下方式
	 * @param fileName
	 * @throws IOException
	 */
	public void function4(String fileName) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
		getFileContent(in);
	}


	/**
	 * 直接使用getResourceAsStream方法获取流
	 * 如果不使用getClassLoader，可以使用getResourceAsStream("/配置测试.txt")直接从resources根路径下获取
	 *主要也是使用getResourceAsStream方法获取流，不使用getClassLoader可以使用getResourceAsStream("/配置测试.txt")直接从resources根路径下获取，SpringBoot中所有文件都在jar包中，没有一个实际的路径，因此可以使用以下方式
	 * @param fileName
	 * @throws IOException
	 */
	public void function5(String fileName) throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/" + fileName);
		getFileContent(in);
	}
	/**
	 * 通过ClassPathResource类获取，建议SpringBoot中使用
	 * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
	 *通过ClassPathResource类获取文件流，SpringBoot中所有文件都在jar包中，没有一个实际的路径，因此可以使用以下方式
	 * @param fileName
	 * @throws IOException
	 */
	public void function6(String fileName) throws IOException {
		ClassPathResource classPathResource = new ClassPathResource(fileName);
		InputStream inputStream = classPathResource.getInputStream();
		getFileContent(inputStream);
	}

	/**
	 * 通过绝对路径获取项目中文件的位置（不能用于服务器）
	 * 通过绝对路径获取项目中文件的位置，只是本地绝对路径，不能用于服务器获取。
	 * @param fileName
	 * @throws IOException
	 */
	public void function7(String fileName) throws IOException {
		String rootPath = System.getProperty("user.dir");//E:\WorkSpace\Git\spring-framework-learning-example
		String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
		getFileContent(filePath);
	}

	/**
	 * 通过绝对路径获取项目中文件的位置（不能用于服务器）
	 * 通过new File("")获取当前的绝对路径，只是本地绝对路径，不能用于服务器获取。
	 * @param fileName
	 * @throws IOException
	 */
	public void function8(String fileName) throws IOException {
		//参数为空
		File directory = new File("");
		//规范路径：getCanonicalPath() 方法返回绝对路径，会把 ..\ 、.\ 这样的符号解析掉
		String rootCanonicalPath = directory.getCanonicalPath();
		//绝对路径：getAbsolutePath() 方法返回文件的绝对路径，如果构造的时候是全路径就直接返回全路径，如果构造时是相对路径，就返回当前目录的路径 + 构造 File 对象时的路径
		String rootAbsolutePath =directory.getAbsolutePath();
		System.out.println(rootCanonicalPath);
		System.out.println(rootAbsolutePath);
		String filePath = rootCanonicalPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
		getFileContent(filePath);
	}

	/**
	 * 通过绝对路径获取项目中文件的位置
	 * 主要是通过设置环境变量，将文件放在环境变量中，原理也是通过绝对路径获取。
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void function9(String fileName) throws IOException {
		System.setProperty("TEST_ROOT","E:\\WorkSpace\\Git\\spring-framework-learning-example");
		//参数为空
		String rootPath = System.getProperty("TEST_ROOT");
		System.out.println(rootPath);
		String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
		getFileContent(filePath);
	}
}
