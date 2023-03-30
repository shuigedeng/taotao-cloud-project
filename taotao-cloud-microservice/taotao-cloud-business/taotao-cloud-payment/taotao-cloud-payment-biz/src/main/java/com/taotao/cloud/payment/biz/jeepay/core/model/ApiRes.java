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

package com.taotao.cloud.payment.biz.jeepay.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.payment.biz.jeepay.core.constants.ApiCodeEnum;
import com.taotao.cloud.payment.biz.jeepay.core.utils.JsonKit;
import com.taotao.cloud.payment.biz.jeepay.jeepay.util.JeepayKit;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * 接口返回对象
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:35
 */
@Data
@AllArgsConstructor
public class ApiRes {

    /** 业务响应码 * */
    private Integer code;

    /** 业务响应信息 * */
    private String msg;

    /** 数据对象 * */
    private Object data;

    /** 签名值 * */
    private String sign;

    /** 输出json格式字符串 * */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    /** 业务处理成功 * */
    public static ApiRes ok() {
        return ok(null);
    }

    /** 业务处理成功 * */
    public static ApiRes ok(Object data) {
        return new ApiRes(ApiCodeEnum.SUCCESS.getCode(), ApiCodeEnum.SUCCESS.getMsg(), data, null);
    }

    /** 业务处理成功, 自动签名 * */
    public static ApiRes okWithSign(Object data, String mchKey) {

        if (data == null) {
            return new ApiRes(
                    ApiCodeEnum.SUCCESS.getCode(), ApiCodeEnum.SUCCESS.getMsg(), null, null);
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
        String sign = JeepayKit.getSign(jsonObject, mchKey);
        return new ApiRes(ApiCodeEnum.SUCCESS.getCode(), ApiCodeEnum.SUCCESS.getMsg(), data, sign);
    }

    /** 业务处理成功, 返回简单json格式 * */
    public static ApiRes ok4newJson(String key, Object val) {
        return ok(JsonKit.newJson(key, val));
    }

    /** 业务处理成功， 封装分页数据， 仅返回必要参数 * */
    public static ApiRes page(IPage iPage) {

        JSONObject result = new JSONObject();
        result.put("records", iPage.getRecords()); // 记录明细
        result.put("total", iPage.getTotal()); // 总条数
        result.put("current", iPage.getCurrent()); // 当前页码
        result.put("hasNext", iPage.getPages() > iPage.getCurrent()); // 是否有下一页
        return ok(result);
    }

    /** 业务处理失败 * */
    public static ApiRes fail(ApiCodeEnum apiCodeEnum, String... params) {

        if (params == null || params.length <= 0) {
            return new ApiRes(apiCodeEnum.getCode(), apiCodeEnum.getMsg(), null, null);
        }
        return new ApiRes(
                apiCodeEnum.getCode(), String.format(apiCodeEnum.getMsg(), params), null, null);
    }

    /** 自定义错误信息, 原封不用的返回输入的错误信息 * */
    public static ApiRes customFail(String customMsg) {
        return new ApiRes(ApiCodeEnum.CUSTOM_FAIL.getCode(), customMsg, null, null);
    }
}
