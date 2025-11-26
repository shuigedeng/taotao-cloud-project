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

package com.taotao.cloud.payment.biz.jeepay.jeepay.response;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.disruptor.util.StringUtils;
import com.taotao.cloud.payment.biz.jeepay.core.utils.JeepayKit;
import java.io.Serializable;

/**
 * Jeepay响应抽象类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public abstract class JeepayResponse implements Serializable {

    private static final long serialVersionUID = -2637191198247207952L;

    private Integer code;
    private String msg;
    private String sign;
    private JSONObject data;

    /**
     * 校验响应数据签名是否正确
     *
     * @param apiKey
     * @return
     */
    public boolean checkSign(String apiKey) {
        if (data == null && StringUtils.isEmpty(getSign())) return true;
        return sign.equals(JeepayKit.getSign(getData(), apiKey));
    }

    /**
     * 校验是否成功(只判断code为0，具体业务要看实际情况)
     *
     * @param apiKey
     * @return
     */
    public boolean isSuccess(String apiKey) {
        if (StringUtils.isEmpty(apiKey)) return code == 0;
        return code == 0 && checkSign(apiKey);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
