package com.taotao.cloud.open.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.open.common.exception.OpenApiException;
import com.taotao.cloud.open.common.model.InParams;

/**
 * 通用工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:29
 */
public class CommonUtil {

    /**
     * 获取待签名的内容
     *
     * @param inParams 入参
     * @return 签名的内容
     */
    public static byte[] getSignContent(InParams inParams) {
        //使用数据+uuid作为签名的内容，保证无参函数调用也能经过签名的验证
        byte[] bodyBytes = inParams.getBodyBytes();
        return ArrayUtil.addAll(bodyBytes, inParams.getUuid().getBytes());
    }

    /**
     * 拼接url地址
     *
     * @param baseUrl 基础路径
     * @param path    待拼接的路径
     * @return 完整的url地址
     */
    public static String completeUrl(String baseUrl, String path) {
        if (StrUtil.isBlank(baseUrl)) {
            throw new OpenApiException("URL基础路径不能为空");
        }
        String separator = "/";
        String formattedUrl = baseUrl;
        if (baseUrl.endsWith(separator)) {
            formattedUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (StrUtil.isNotBlank(path)) {
            path = path.startsWith(separator) ? path.substring(1) : path;
            return formattedUrl + separator + path;
        }
        return baseUrl;
    }

    /**
     * 拷贝实例（浅拷贝）
     *
     * @param obj 源对象
     * @param <T> 对象泛型
     * @return 新对象
     */
    public static <T> T cloneInstance(T obj) {
        try {
            //创建新实例以免影响原来的对象
            T tmp = (T) obj.getClass().newInstance();
            BeanUtil.copyProperties(obj, tmp);
            return tmp;
        } catch (Exception ex) {
            LogUtils.error("克隆新实例失败", ex);
        }
        return null;
    }
}
