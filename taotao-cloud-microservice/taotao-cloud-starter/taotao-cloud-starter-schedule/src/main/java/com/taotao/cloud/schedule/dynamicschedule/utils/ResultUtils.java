package com.taotao.cloud.schedule.dynamicschedule.utils;


import com.example.dynamicschedule.base.ResultCode;
import com.example.dynamicschedule.base.ResultDTO;

/**
 *返回信息工具
 */
public final class ResultUtils {
    private ResultUtils() {
    }

    public static ResultDTO getSuccess(Object result) {
        return new ResultDTO(ResultCode.SUCCESS, result);
    }



    public static ResultDTO getFail(Object result) {
        return new ResultDTO(ResultCode.FAIL, result);
    }

    public static ResultDTO getParamError(Object result) {
        return new ResultDTO(ResultCode.PARAM_ERROR, result);
    }

    public static ResultDTO getResourceInvalid(Object result) {
        return new ResultDTO(ResultCode.RESOURCE_INVALID, result);
    }

    public static ResultDTO getTokenInvalid(Object result) {
        return new ResultDTO(ResultCode.TOKEN_INVALID, result);
    }

    public static ResultDTO getCustomErrordesc(String errorDesc, Object result) {
        return new ResultDTO(ResultCode.CUSTOM_ERRORDESC, errorDesc, result);
    }

    public static ResultDTO getResultDTO(int code, String errorDesc, Object result) {
        return new ResultDTO(code, errorDesc, result);
    }
}
