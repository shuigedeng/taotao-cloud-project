package com.taotao.cloud.sys.biz.modules.core.service.data;

import javax.validation.constraints.Pattern;

import com.sanri.tools.modules.core.service.data.randomstring.RandomStringGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用正则表达式生成随机数据
 */
@Service
@Slf4j
public class RegexRandomDataService extends RandomDataService{
    public static final RandomStringGenerator generator = new RandomStringGenerator();
    /**
     * 从正则表达式生成随机数据
     * @param expression
     * @return
     */
    public static String regexRandom(String expression)  {
        return generator.generateByRegex(expression);
    }

    /**
     * 重写 populateDataOrigin ,读取标记的正则表达式,然后使用正则表达式生成数据
     * @param columnName
     * @param propertyType
     * @return
     */
    @Override
    protected Object populateDataOrigin(String columnName, Class<?> propertyType) {
        Pattern pattern = AnnotationUtils.getAnnotation(propertyType, Pattern.class);
        if(pattern == null || propertyType.isArray() || (!String.class.isAssignableFrom(propertyType))){
            return super.populateDataOrigin(columnName,propertyType);
        }
        String regexp = pattern.regexp();
        return regexRandom(regexp);
    }
}
