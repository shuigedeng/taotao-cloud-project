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

/** 自定义登录句柄保存对象 */
public class MyNativeLong {

    /** 用户句柄 */
    private NativeLong lUserID;

    /** 预览句柄 */
    private NativeLong lRealHandle;

    /** 通道句柄 */
    private NativeLong lChannel;

    /** 最后一次使用时间 */
    private Date lastUse;

    /** 是否正在使用 */
    private boolean isUse;

    /** 开始控制摄像头,设置使用时间,正在使用中 */
    public void start() {
        this.lastUse = new Date();
        this.isUse = true;
    }

    /** 停止控制摄像头,已经不再使用 */
    public void down() {
        this.isUse = false;
    }

    public NativeLong getlRealHandle() {
        return lRealHandle;
    }

    public void setlRealHandle(NativeLong lRealHandle) {
        this.lRealHandle = lRealHandle;
    }

    public NativeLong getlUserID() {
        return lUserID;
    }

    public void setlUserID(NativeLong lUserID) {
        this.lUserID = lUserID;
    }

    public NativeLong getlChannel() {
        return lChannel;
    }

    public void setlChannel(NativeLong lChannel) {
        this.lChannel = lChannel;
    }

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean isUse) {
        this.isUse = isUse;
    }
}
