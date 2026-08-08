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
 * @Author VisitBean
 * @Date 18-8-10
 * @Version V1.0.0
 * @Description:
 */
public class VisitBean implements Writable {
    //    点击流模型Visits表
    private String session; // session
    private String remote_addr; // 客户端地址
    private String inTime; // 进入时间
    private String outTime; // 出去时间
    private String inPage; // 从那个页面进来
    private String outPage; // 从那个页面出去
    private String referal; //
    private int pageVisits; // 页面点击次数

    public void set(
            String session,
            String remote_addr,
            String inTime,
            String outTime,
            String inPage,
            String outPage,
            String referal,
            int pageVisits) {
        this.session = session;
        this.remote_addr = remote_addr;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inPage = inPage;
        this.outPage = outPage;
        this.referal = referal;
        this.pageVisits = pageVisits;
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

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInPage() {
        return inPage;
    }

    public void setInPage(String inPage) {
        this.inPage = inPage;
    }

    public String getOutPage() {
        return outPage;
    }

    public void setOutPage(String outPage) {
        this.outPage = outPage;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public int getPageVisits() {
        return pageVisits;
    }

    public void setPageVisits(int pageVisits) {
        this.pageVisits = pageVisits;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.session = in.readUTF();
        this.remote_addr = in.readUTF();
        this.inTime = in.readUTF();
        this.outTime = in.readUTF();
        this.inPage = in.readUTF();
        this.outPage = in.readUTF();
        this.referal = in.readUTF();
        this.pageVisits = in.readInt();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(session);
        out.writeUTF(remote_addr);
        out.writeUTF(inTime);
        out.writeUTF(outTime);
        out.writeUTF(inPage);
        out.writeUTF(outPage);
        out.writeUTF(referal);
        out.writeInt(pageVisits);
    }

    @Override
    public String toString() {
        return session
                + ","
                + remote_addr
                + "\001"
                + inTime
                + "\001"
                + outTime
                + "\001"
                + inPage
                + "\001"
                + outPage
                + "\001"
                + referal
                + "\001"
                + pageVisits;
    }
}
