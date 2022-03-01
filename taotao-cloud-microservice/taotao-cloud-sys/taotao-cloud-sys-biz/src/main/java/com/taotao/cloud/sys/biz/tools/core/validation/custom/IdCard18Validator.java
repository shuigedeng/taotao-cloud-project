package com.taotao.cloud.sys.biz.tools.core.validation.custom;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class IdCard18Validator implements ConstraintValidator<IdCard18,String> {
    @Override
    public void initialize(IdCard18 constraintAnnotation) {
        try {
            URL resource = IdCard18Validator.class.getResource("/areaCodes");
            if(resource == null){
	            LogUtil.warn("身份证地区码未找到,请在资源目录下放置 areaCodes 文件用来验证身份证");
                return ;
            }
            URLConnection urlConnection = resource.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            String areaCodes = IOUtils.toString(inputStream, Charset.forName("utf-8"));
            if(StringUtils.isNotBlank(areaCodes)){
                IdCard18Model.areaCodes = StringUtils.split(areaCodes,',');
            }
        } catch (IOException e) {
	        LogUtil.error("读取身份证地区码异常,检查资源目录的 areaCodes 文件 {}",e.getMessage());
        }
    }

    @Override
    public boolean isValid(String idcard, ConstraintValidatorContext constraintValidatorContext) {
        try{
            IdCard18Model idCard18Model = new IdCard18Model(idcard);
            return true;
        }catch (IllegalArgumentException e){
	        LogUtil.error(e.getMessage());
            return false;
        }
    }
}
