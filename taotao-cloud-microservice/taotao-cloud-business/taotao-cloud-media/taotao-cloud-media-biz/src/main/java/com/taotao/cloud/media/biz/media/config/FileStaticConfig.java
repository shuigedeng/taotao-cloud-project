package com.taotao.cloud.media.biz.media.config;//package com.zj.config;
// 
//import java.io.File;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.system.ApplicationHome;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import cn.hutool.core.util.StrUtil;
// 
///**
// * 静态资源配置
// * 
// *
// */
//@Configuration
//public class FileStaticConfig implements WebMvcConfigurer {
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
//}
