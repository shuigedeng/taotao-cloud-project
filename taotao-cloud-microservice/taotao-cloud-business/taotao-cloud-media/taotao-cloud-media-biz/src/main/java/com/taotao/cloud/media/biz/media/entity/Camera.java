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

package com.taotao.cloud.media.biz.media.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/** camera相机 */
@TableName("camera")
// @Entity
// @Table(name="camera")	//jpa自动创建表
public class Camera implements Serializable {
    /** */
    private static final long serialVersionUID = -5575352151805386129L;

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;
    private String remark;
    private int flv;
    private int hls;
    private int ffmpeg;
    private int autoClose;
    private int type = 0;
    private String mediaKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getFlv() {
        return flv;
    }

    public void setFlv(int flv) {
        this.flv = flv;
    }

    public int getHls() {
        return hls;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    public int getFfmpeg() {
        return ffmpeg;
    }

    public void setFfmpeg(int ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public int getAutoClose() {
        return autoClose;
    }

    public void setAutoClose(int autoClose) {
        this.autoClose = autoClose;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }
}
