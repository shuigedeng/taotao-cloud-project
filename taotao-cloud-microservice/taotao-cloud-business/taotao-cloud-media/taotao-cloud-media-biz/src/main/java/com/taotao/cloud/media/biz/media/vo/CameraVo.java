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

package com.taotao.cloud.media.biz.media.vo;

/** */
public class CameraVo {

    private String id;
    /** 播放地址 */
    private String url;

    /** 备注 */
    private String remark;

    /** 启用flv */
    private boolean enabledFlv = false;

    /** 启用hls */
    private boolean enabledHls = false;

    /** javacv/ffmpeg */
    private String mode = "未开启";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isEnabledFlv() {
        return enabledFlv;
    }

    public void setEnabledFlv(boolean enabledFlv) {
        this.enabledFlv = enabledFlv;
    }

    public boolean isEnabledHls() {
        return enabledHls;
    }

    public void setEnabledHls(boolean enabledHls) {
        this.enabledHls = enabledHls;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
