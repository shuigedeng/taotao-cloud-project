package com.taotao.cloud.file.biz.largefile.annotation;


import com.taotao.cloud.file.biz.largefile.enu.UploadModeEnum;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface UploadMode {

	UploadModeEnum mode();

}
