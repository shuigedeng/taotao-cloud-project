package com.taotao.cloud.common.model.query.generator;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.query.code.ParamTypeEnum;
import com.taotao.cloud.common.model.query.entity.QueryParam;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 参数值转换
 */
public class ParamValueTypeConvert {

    /**
     * 查询条件值处理
     * @param queryParam 查询参数
     * @return 处理完的查询条件值
     */
    public static Object initQueryParamValue(QueryParam queryParam) {
        Object paramValue = queryParam.getParamValue();
        // 空值不进行处理
        if (Objects.isNull(paramValue)){
            return null;
        }
        // 未传入参数类型原样返回
        if (StrUtil.isBlank(queryParam.getParamType())){
            return paramValue;
        }
        ParamTypeEnum paramTypeEnum = Optional.ofNullable(ParamTypeEnum.getByCode(queryParam.getParamType()))
                .orElseThrow(() -> new BusinessException("不支持的数据类型"));
        switch (paramTypeEnum){
            // 原样返回
            case NUMBER:
            case STRING:
            case BOOLEAN:
            case DATE:
            case TIME:
            case DATE_TIME:
                return convertType(paramValue,paramTypeEnum);
            case LIST:{
                Collection<?> collection = (Collection<?>) paramValue;
                return collection.stream()
                        .map(o -> convertType(o,paramTypeEnum))
                        .collect(Collectors.toList());
            }
            default:
                return null;
        }
    }

    /**
     * 数据类型解析
     * @param paramValue 参数
     * @param paramTypeEnum 参数类型
     * @return 解析完的数据
     */
    private static Object convertType(Object paramValue, ParamTypeEnum paramTypeEnum){

        ParamTypeEnum typeEnum = Optional.ofNullable(ParamTypeEnum.getByCode(paramTypeEnum.getCode()))
                .orElseThrow(() -> new BusinessException("不支持的数据类型"));
        switch (typeEnum){
            // 原样返回
            case NUMBER:
            case STRING:
            case BOOLEAN:
                return paramValue;
            case DATE:
                return LocalDateTimeUtil.parseDate((String)paramValue, DatePattern.NORM_DATE_PATTERN);
            case TIME:
                return LocalDateTimeUtil.parse((String)paramValue, DatePattern.NORM_TIME_PATTERN).toLocalTime();
            case DATE_TIME:
                return LocalDateTimeUtil.parse((String)paramValue, DatePattern.NORM_DATETIME_PATTERN);
            case LIST:
                return paramValue;
            default:
                throw new BusinessException("类型错误");
        }
    }
}
