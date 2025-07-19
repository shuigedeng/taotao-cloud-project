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

package com.taotao.cloud.offline.weblog.step2_mapreduce_pre.mrbean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

/**
 * @ClassName: step2_mapreduce_pre.com.bigdata.log.click.mrbean
 * @Author WebLogBean
 * @Date 18-8-10
 * @Version V1.0.0
 * @Description:
 */
public class WebLogBean implements Writable {
    //    实现Hadoop的序列化接口,比java的序列化更轻量

    private boolean valid = true; // 判断数据是否合法
    private String remote_addr; // 记录客户端的ip地址
    private String remote_user; // 记录客户端用户名称,忽略属性"-"
    private String remote_time_local; // 记录客户端访问时间与时区
    private String request_method_url; // 记录请求的url与http协议
    private String status; // 记录请求状态；成功是200
    private String reponse_body_bytes; // 记录发送给客户端文件主体内容大小
    private String http_referer; // 用来记录从那个页面链接访问过来的
    private String http_user_agent; // 记录客户浏览器的相关信息

    public WebLogBean() {}

    public void set(
            boolean valid,
            String remote_addr,
            String remote_user,
            String remote_time_local,
            String request_method_url,
            String status,
            String reponse_body_bytes,
            String http_referer,
            String http_user_agent) {
        this.valid = valid;
        this.remote_addr = remote_addr;
        this.remote_user = remote_user;
        this.remote_time_local = remote_time_local;
        this.request_method_url = request_method_url;
        this.status = status;
        this.reponse_body_bytes = reponse_body_bytes;
        this.http_referer = http_referer;
        this.http_user_agent = http_user_agent;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public String getRemote_time_local() {
        return remote_time_local;
    }

    public void setRemote_time_local(String remote_time_local) {
        this.remote_time_local = remote_time_local;
    }

    public String getRequest_method_url() {
        return request_method_url;
    }

    public void setRequest_method_url(String request_method_url) {
        this.request_method_url = request_method_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReponse_body_bytes() {
        return reponse_body_bytes;
    }

    public void setReponse_body_bytes(String reponse_body_bytes) {
        this.reponse_body_bytes = reponse_body_bytes;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.valid);
        sb.append(",").append(this.getRemote_addr());
        sb.append(",").append(this.getRemote_user());
        sb.append(",").append(this.getRemote_time_local());
        sb.append(",").append(this.getRequest_method_url());
        sb.append(",").append(this.getStatus());
        sb.append(",").append(this.getReponse_body_bytes());
        sb.append(",").append(this.getHttp_referer());
        sb.append(",").append(this.getHttp_user_agent());
        return sb.toString();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(valid);
        dataOutput.writeUTF(null == remote_addr ? "" : remote_addr);
        dataOutput.writeUTF(null == remote_user ? "" : remote_user);
        dataOutput.writeUTF(null == remote_time_local ? "" : remote_time_local);
        dataOutput.writeUTF(null == request_method_url ? "" : request_method_url);
        dataOutput.writeUTF(null == status ? "" : status);
        dataOutput.writeUTF(null == reponse_body_bytes ? "" : reponse_body_bytes);
        dataOutput.writeUTF(null == http_referer ? "" : http_referer);
        dataOutput.writeUTF(null == http_user_agent ? "" : http_user_agent);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.valid = dataInput.readBoolean();
        this.remote_addr = dataInput.readUTF();
        this.remote_user = dataInput.readUTF();
        this.remote_time_local = dataInput.readUTF();
        this.request_method_url = dataInput.readUTF();
        this.status = dataInput.readUTF();
        this.reponse_body_bytes = dataInput.readUTF();
        this.http_referer = dataInput.readUTF();
        this.http_user_agent = dataInput.readUTF();
    }
}
