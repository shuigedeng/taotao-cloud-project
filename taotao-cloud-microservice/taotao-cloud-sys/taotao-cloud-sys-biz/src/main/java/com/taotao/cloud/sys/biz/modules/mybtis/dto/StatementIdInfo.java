package com.taotao.cloud.sys.biz.modules.mybtis.dto;

import lombok.Data;
import org.apache.ibatis.mapping.ParameterMapping;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 语句信息
 */
@Data
public class StatementIdInfo {
    private String id;
    private List<ParameterInfo> parameterInfos = new ArrayList<>();

    public StatementIdInfo() {
    }

    public StatementIdInfo(String id) {
        this.id = id;
    }

    public String getSqlId(){
        return id.substring(id.lastIndexOf('.') + 1);
    }

    public void addParameter(ParameterInfo parameterInfo){
        parameterInfos.add(parameterInfo);
    }

    @Data
    public static class ParameterInfo{
        private String name;
        private String type;
        private String typeSimpleName;
        // 支持 java 泛型
        private List<String> parameterTypes = new ArrayList<>();

        public ParameterInfo() {
        }

        public ParameterInfo(String name, Class type) {
            this.name = name;
            this.type = type.getName();
            this.typeSimpleName = type.getSimpleName();
        }

        public ParameterInfo(String name, Class type,Type genericType) {
            this.name = name;
            this.type = type.getName();
            this.typeSimpleName = type.getSimpleName();

            if (List.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)){
                ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) genericType;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class actualType = (Class) actualTypeArgument;
                    parameterTypes.add(actualType.getName());
                }

            }
        }
    }
}
