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

package com.taotao.cloud.media.biz.media.config; // package com.zj.config;
//
// import java.io.File;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.system.ApplicationHome;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
/// **
// * 静态资源配置
// *
// *
// */
// @Configuration
// public class FileStaticConfig implements WebMvcConfigurer {
//
//	@Value("${mediaserver.path}")
//	private String prePath;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//    	if(StrUtil.isBlank(prePath)) {
//    		StringBuffer path = new StringBuffer();
//    		ApplicationHome app = new ApplicationHome();
//			File dir = app.getDir();
//			path.append(dir.getAbsolutePath());
//			path.append(File.separator);
//			path.append("record");
//			prePath = path.toString();
//    	}
//
//        /**
//         *  /ts/xxx   指文件的访问方式  如：localhost:8080/ts/abc.wav
//         *  file:d/voice/  指静态文件存放在服务器上的位置
//         */
////        registry.addResourceHandler("/ts/**").addResourceLocations("file:"+"d:/flv/out/");
//        registry.addResourceHandler("/ts/**").addResourceLocations("file:"+prePath+"/");
//    }
// }
