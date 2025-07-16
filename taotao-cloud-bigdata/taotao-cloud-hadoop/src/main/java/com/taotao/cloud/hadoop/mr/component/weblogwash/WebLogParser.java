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

package com.taotao.cloud.hadoop.mr.component.weblogwash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * WebLogParser
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:35
 */
public class WebLogParser {

    static SimpleDateFormat sd1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
    static SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static WebLogBean parser(String line) {
        WebLogBean webLogBean = new WebLogBean();
        String[] arr = line.split(" ");
        if (arr.length > 11) {
            webLogBean.setRemote_addr(arr[0]);
            webLogBean.setRemote_user(arr[1]);
            webLogBean.setTime_local(parseTime(arr[3].substring(1)));
            webLogBean.setRequest(arr[6]);
            webLogBean.setStatus(arr[8]);
            webLogBean.setBody_bytes_sent(arr[9]);
            webLogBean.setHttp_referer(arr[10]);

            if (arr.length > 12) {
                webLogBean.setHttp_user_agent(arr[11] + " " + arr[12]);
            } else {
                webLogBean.setHttp_user_agent(arr[11]);
            }
            // 大于400，HTTP错误
            if (Integer.parseInt(webLogBean.getStatus()) >= 400) {
                webLogBean.setValid(false);
            }
        } else {
            webLogBean.setValid(false);
        }
        return webLogBean;
    }

    public static String parseTime(String dt) {
        String timeString = "";
        try {
            Date parse = sd1.parse(dt);
            timeString = sd2.format(parse);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeString;
    }
}
