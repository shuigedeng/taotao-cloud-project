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
 * @Author PageViewsBean
 * @Date 18-8-10
 * @Version V1.0.0
 * @Description:
 */
public class PageViewsBean implements Writable {
    //    页面点击流模型Pageviews表

    //    (按session聚集的访问页面信息)(每个session中的每个url也即是访问页面,的记录信息, 想差半个小时了就认为是下一个session了)

    private String session; // Session
    private String remote_addr; // IP地址
    private String timestr; // 访问时间
    private String request_url; // 请求url
    private String step; // 第几步
    private String staylong; // 停留时长
    private String referal; // 上一个页面
    private String useragent; // useragent
    private String bytes_send; // 字节数
    private String status; // 状态

    public PageViewsBean() {}

    public void set(
            String session,
            String remote_addr,
            String timestr,
            String request_url,
            String step,
            String staylong,
            String referal,
            String useragent,
            String bytes_send,
            String status) {
        this.session = session;
        this.remote_addr = remote_addr;
        this.timestr = timestr;
        this.request_url = request_url;
        this.step = step;
        this.staylong = staylong;
        this.referal = referal;
        this.useragent = useragent;
        this.bytes_send = bytes_send;
        this.status = status;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getTimestr() {
        return timestr;
    }

    public void setTimestr(String timestr) {
        this.timestr = timestr;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStaylong() {
        return staylong;
    }

    public void setStaylong(String staylong) {
        this.staylong = staylong;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getBytes_send() {
        return bytes_send;
    }

    public void setBytes_send(String bytes_send) {
        this.bytes_send = bytes_send;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.session);
        sb.append(",").append(this.getRemote_addr());
        sb.append(",").append(this.getTimestr());
        sb.append(",").append(this.getRequest_url());
        sb.append(",").append(this.getStep());
        sb.append(",").append(this.getStaylong());
        sb.append(",").append(this.getReferal());
        sb.append(",").append(this.getUseragent());
        sb.append(",").append(this.getBytes_send());
        sb.append(",").append(this.getStatus());
        return sb.toString();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(session);
        dataOutput.writeUTF(remote_addr);
        dataOutput.writeUTF(timestr);
        dataOutput.writeUTF(request_url);
        dataOutput.writeUTF(step);
        dataOutput.writeUTF(staylong);
        dataOutput.writeUTF(referal);
        dataOutput.writeUTF(useragent);
        dataOutput.writeUTF(bytes_send);
        dataOutput.writeUTF(status);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.session = dataInput.readUTF();
        ;
        this.remote_addr = dataInput.readUTF();
        ;
        this.timestr = dataInput.readUTF();
        ;
        this.request_url = dataInput.readUTF();
        ;
        this.step = dataInput.readUTF();
        ;
        this.staylong = dataInput.readUTF();
        ;
        this.referal = dataInput.readUTF();
        ;
        this.useragent = dataInput.readUTF();
        ;
        this.bytes_send = dataInput.readUTF();
        ;
        this.status = dataInput.readUTF();
        ;
    }
}
