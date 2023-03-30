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

package com.taotao.cloud.media.biz.opencv.common.utils;

import org.opencv.core.Rect;

public class RectComp implements Comparable<Object> {
    private Rect rm;

    public Rect getRm() {
        return rm;
    }

    public void setRm(Rect rm) {
        this.rm = rm;
    }

    public RectComp() {
        super();
    }

    public RectComp(Rect rm) {
        super();
        this.rm = rm;
    }

    // @Override
    // public int compareTo(Object object) {
    // if(this == object){
    // return 0;
    // } else if (object != null && object instanceof RectComp) {
    // RectComp rect = (RectComp) object;
    // if (rm.x <= rect.rm.x) {
    // return -1;
    // }else{
    // return 1;
    // }
    // }else{
    // return -1;
    // }
    // }
    @Override
    // 按面积排序，最大的放第一个
    public int compareTo(Object object) {
        if (this == object) {
            return 0;
        } else if (object != null && object instanceof RectComp) {
            RectComp rect = (RectComp) object;
            if (rm.area() >= rect.rm.area()) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }
}
