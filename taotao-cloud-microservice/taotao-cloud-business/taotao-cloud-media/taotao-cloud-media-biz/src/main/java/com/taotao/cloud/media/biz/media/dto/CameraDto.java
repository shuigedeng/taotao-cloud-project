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

package com.taotao.cloud.media.biz.media.dto;

import java.io.Serializable;

/** camera相机 */
public class CameraDto implements Serializable {
    /** */
    private static final long serialVersionUID = -5575352151805386129L;

    /** rtsp、rtmp、d:/flv/test.mp4、desktop */
    private String url;

    /** 流备注 */
    private String remark;

    /** flv开启状态 */
    private boolean enabledFlv = true;

    /** hls开启状态 */
    private boolean enabledHls = false;

    /** 是否启用ffmpeg，启用ffmpeg则不用javacv */
    private boolean enabledFFmpeg = false;

    /** 无人拉流观看是否自动关闭流 */
    private boolean autoClose;

    /** md5 key，媒体标识，区分不同媒体 */
    private String mediaKey;

    /** 网络超时 ffmpeg默认5秒，这里设置15秒 */
    private String netTimeout = "15000000";
    /** 读写超时，默认5秒 */
    private String readOrWriteTimeout = "15000000";

    /** 无人拉流观看持续多久自动关闭，默认1分钟 */
    private long noClientsDuration = 60000;

    /** 0网络流，1本地视频 */
    private int type = 0;

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

    public boolean isEnabledFFmpeg() {
        return enabledFFmpeg;
    }

    public void setEnabledFFmpeg(boolean enabledFFmpeg) {
        this.enabledFFmpeg = enabledFFmpeg;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    public String getNetTimeout() {
        return netTimeout;
    }

    public void setNetTimeout(String netTimeout) {
        this.netTimeout = netTimeout;
    }

    public String getReadOrWriteTimeout() {
        return readOrWriteTimeout;
    }

    public void setReadOrWriteTimeout(String readOrWriteTimeout) {
        this.readOrWriteTimeout = readOrWriteTimeout;
    }

    public long getNoClientsDuration() {
        return noClientsDuration;
    }

    public void setNoClientsDuration(long noClientsDuration) {
        this.noClientsDuration = noClientsDuration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
