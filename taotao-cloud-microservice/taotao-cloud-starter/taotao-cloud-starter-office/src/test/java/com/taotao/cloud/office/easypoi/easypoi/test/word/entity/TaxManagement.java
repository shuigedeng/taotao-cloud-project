/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.office.easypoi.easypoi.test.word.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 纳税管理
 * @author JueYue
 *   2015年11月24日 下午10:52:11
 */
public class TaxManagement {

    @Excel(name = "税种")
    private String type;
    @Excel(name = "2014年")
    private String presum;
    @Excel(name = "2015年")
    private String thissum;
    @Excel(name = "当年所属期间")
    private String curmonth;
    @Excel(name = "采集截止日期")
    private String now;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPresum() {
        return presum;
    }

    public void setPresum(String presum) {
        this.presum = presum;
    }

    public String getThissum() {
        return thissum;
    }

    public void setThissum(String thissum) {
        this.thissum = thissum;
    }

    public String getCurmonth() {
        return curmonth;
    }

    public void setCurmonth(String curmonth) {
        this.curmonth = curmonth;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

}
