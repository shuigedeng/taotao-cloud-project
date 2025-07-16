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

package com.taotao.cloud.hadoop.mr.component.secondarysort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * OrderBean
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:34
 */
public class OrderBean implements WritableComparable<OrderBean> {

    private Text itemid;
    private DoubleWritable amount;

    public OrderBean() {}

    public OrderBean(Text itemid, DoubleWritable amount) {
        set(itemid, amount);
    }

    public void set(Text itemid, DoubleWritable amount) {
        this.itemid = itemid;
        this.amount = amount;
    }

    public Text getItemid() {
        return itemid;
    }

    public DoubleWritable getAmount() {
        return amount;
    }

    @Override
    public int compareTo(OrderBean o) {
        int cmp = this.itemid.compareTo(o.getItemid());
        if (cmp == 0) {
            cmp = -this.amount.compareTo(o.getAmount());
        }
        return cmp;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(itemid.toString());
        out.writeDouble(amount.get());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        String readUTF = in.readUTF();
        double readDouble = in.readDouble();

        this.itemid = new Text(readUTF);
        this.amount = new DoubleWritable(readDouble);
    }

    @Override
    public String toString() {
        return itemid.toString() + "\t" + amount.get();
    }
}
