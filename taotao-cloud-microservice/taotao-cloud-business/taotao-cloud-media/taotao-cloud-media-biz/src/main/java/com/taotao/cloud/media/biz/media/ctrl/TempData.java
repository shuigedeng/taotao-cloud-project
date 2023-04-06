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

package com.taotao.cloud.media.biz.media.ctrl;

import com.sun.jna.NativeLong;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 全局缓存变量 */
public class TempData {

    private TempData() {}

    private static TempData tempData = null;

    private Map<String, MyNativeLong> nativeLongMap;

    public static TempData getTempData() {
        if (tempData == null) {
            tempData = new TempData();
            tempData.nativeLongMap = new HashMap<String, MyNativeLong>();
            return tempData;
        }
        return TempData.tempData;
    }

    /**
     * 获取指定IP摄像头的预览句柄
     *
     * @return
     */
    public MyNativeLong getNativeLong(String IPKey) {
        return nativeLongMap.get(IPKey);
    }

    /**
     * 设置指定IP摄像头的句柄
     *
     * @param IPKey 摄像头的ip地址
     * @param IuserID 用户句柄
     * @param lRealHandle 预览句柄
     * @param lChannel 通道句柄
     */
    public void setNativeLong(String IPKey, NativeLong lUserID, NativeLong lRealHandle, NativeLong lChannel) {
        MyNativeLong myNativeLong = new MyNativeLong();

        myNativeLong.setlUserID(lUserID);
        myNativeLong.setlRealHandle(lRealHandle);
        myNativeLong.setlChannel(lChannel);

        myNativeLong.setUse(true);
        myNativeLong.setLastUse(new Date());

        nativeLongMap.put(IPKey, myNativeLong);
    }

    /**
     * 移除
     *
     * @param IPKey
     */
    public void removeNativeLong(String IPKey) {
        nativeLongMap.remove(IPKey);
    }
}
