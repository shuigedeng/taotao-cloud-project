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

package com.taotao.cloud.offline.weblog.step2_mapreduce_pre.utils;

import com.taotao.cloud.bigdata.offline.weblog.step2_mapreduce_pre.mrbean.WebLogBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

/**
 * @ClassName: step2_mapreduce_pre.com.bigdata.log.click.utils
 * @Author WebLogParser
 * @Date 18-8-10
 * @Version V1.0.0
 * @Description: 日志预处理 主要过滤静态url 生成WebLogBean
 */
public class WebLogParser {

    private static SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
    private static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static WebLogBean parser(String line) {
        // 解析一行日志数据
        WebLogBean bean = new WebLogBean();
        String[] arr = line.split(" ");
        if (arr.length > 11) {
            bean.setRemote_addr(arr[0]);
            bean.setRemote_user(arr[1]);
            String local_time = formatDate(arr[3].substring(1));
            if (null == local_time) {
                bean.setValid(false);
            } else {
                bean.setRemote_time_local(local_time);
            }

            bean.setRequest_method_url(arr[6]);
            bean.setStatus(arr[8]);
            bean.setReponse_body_bytes(arr[9]);
            bean.setHttp_referer(arr[10]);

            // 如果useragent元素较多，拼接useragent
            if (arr.length > 12) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    sb.append(arr[i]);
                }
                bean.setHttp_user_agent(sb.toString());
            } else {
                bean.setHttp_user_agent(arr[11]);
            }

            if (Integer.parseInt(bean.getStatus()) > 400) {
                bean.setValid(false);
            }

        } else {
            bean.setValid(false);
        }

        return bean;
    }

    public static void filterStaticResource(WebLogBean webLogBean, Set<String> sets) {
        if (sets.contains(webLogBean.getRequest_method_url())) {
            webLogBean.setValid(false);
        }
    }

    private static String formatDate(String time_local) {
        try {
            return df2.format(df1.parse(time_local));
        } catch (ParseException e) {
            return null;
        }
    }
}
