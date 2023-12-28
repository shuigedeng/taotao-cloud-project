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

package com.taotao.cloud.wechat.biz.wechatpush.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class JiNianRi {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 距离date还有多少天
     *
     * @param date
     * @return
     */
    public static int before(String date) {
        int day = 0;
        try {
            long time = simpleDateFormat.parse(date).getTime() - System.currentTimeMillis();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            LogUtils.error(e);
        }
        return day;
    }

    /**
     * 已经过去date多少天
     *
     * @param date
     * @return
     */
    public static int after(String date) {
        int day = 0;
        try {
            long time =
                    System.currentTimeMillis() - simpleDateFormat.parse(date).getTime();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            LogUtils.error(e);
        }
        return day;
    }

    public static int getJieHun(String jieHun) {
        return before(jieHun);
    }

    public static int getLinZhen(String linZheng) {
        return before(linZheng);
    }

    public static int getLianAi(String lianAi) {
        return after(lianAi);
    }

    public static int getShengRi(String shengRi) {
        return before(shengRi);
    }

    public static void main(String[] args) {
        //        LogUtils.info(getJieHun());
    }
}
