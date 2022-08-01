package com.taotao.cloud.sys.biz.modules.dubbo.dtos;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@Getter
public class DubboLoadMethodParam {
    private String connName;
    private String serviceClassName;
    private String methods;
    private String classloaderName;

    public DubboLoadMethodParam() {
    }

    public DubboLoadMethodParam(String connName, String serviceClassName, String methods, String classloaderName) {
        this.connName = connName;
        this.serviceClassName = serviceClassName;
        this.methods = methods;
        this.classloaderName = classloaderName;
    }

    public String [] methodArray(){
        return StringUtils.split(methods,',');
    }

    public List<String> methodList(){
        String[] strings = methodArray();
        if (strings != null) {
            return Arrays.asList(strings);
        }
        return null;
    }
}
