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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 控制参数枚举 */
public enum CloudCode {

    /** 云台开启 */
    START(0, "云台开启"),

    /** 云台关闭 */
    END(1, "云台关闭"),

    /** 左上 */
    UP_LEFT(HCNetSDK.UP_LEFT, "左上"),

    /** 右下 */
    DOWN_RIGHT(HCNetSDK.DOWN_RIGHT, "右下"),

    /** 上 */
    TILT_UP(HCNetSDK.TILT_UP, "上"),

    /** 下 */
    TILT_DOWN(HCNetSDK.TILT_DOWN, "下"),

    /** 右上 */
    UP_RIGHT(HCNetSDK.UP_RIGHT, "右上"),

    /** 左下 */
    DOWN_LEFT(HCNetSDK.DOWN_LEFT, "左下"),

    /** 左 */
    PAN_LEFT(HCNetSDK.PAN_LEFT, "左"),

    /** 右 */
    PAN_RIGHT(HCNetSDK.PAN_RIGHT, "右"),

    /** 调焦 - */
    ZOOM_IN(HCNetSDK.ZOOM_IN, "调焦 -"),

    /** 调焦 + */
    ZOOM_OUT(HCNetSDK.ZOOM_OUT, "调焦 +"),

    /** 聚焦 - */
    FOCUS_NEAR(HCNetSDK.FOCUS_NEAR, "聚焦 -"),

    /** 聚焦 + */
    FOCUS_FAR(HCNetSDK.FOCUS_FAR, "聚焦 +"),

    /** 光圈 + */
    IRIS_OPEN(HCNetSDK.IRIS_OPEN, "光圈 +"),

    /** 光圈 - */
    IRIS_CLOSE(HCNetSDK.IRIS_CLOSE, "光圈 -"),

    /** 左右自动扫描 */
    PAN_AUTO(HCNetSDK.PAN_AUTO, "左右自动扫描"),

    /** 速度1 */
    SPEED_LV1(1, "速度1"),

    /** 速度2 */
    SPEED_LV2(2, "速度2"),

    /** 速度3 */
    SPEED_LV3(3, "速度3"),

    /** 速度4 */
    SPEED_LV4(4, "速度4"),

    /** 速度5 */
    SPEED_LV5(5, "速度5"),

    /** 速度6 */
    SPEED_LV6(6, "速度6");

    private int key;

    private String name;

    /** 构造方法 */
    private CloudCode(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    /**
     * 返回所有的类型list
     *
     * @return
     */
    public static List<CloudCode> toList() {
        List<CloudCode> list = new ArrayList<CloudCode>();
        CloudCode[] s = CloudCode.values();
        for (CloudCode sit : s) {
            list.add(sit);
        }
        return list;
    }

    /**
     * 返回所有的类型map
     *
     * @return
     */
    public static List<Map<String, Object>> toMap() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CloudCode _s : CloudCode.toList()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", _s.getKey());
            map.put("name", _s.getName());
            list.add(map);
        }
        return list;
    }
}
