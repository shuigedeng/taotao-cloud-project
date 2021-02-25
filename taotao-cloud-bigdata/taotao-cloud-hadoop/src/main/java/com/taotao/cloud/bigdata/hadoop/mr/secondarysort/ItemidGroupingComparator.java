package com.taotao.cloud.bigdata.hadoop.mr.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 利用reduce端的GroupingComparator来实现将一组bean看成相同的key
 *
 * @author duanhaitao@itcast.cn
 */
public class ItemidGroupingComparator extends WritableComparator {
    //传入作为key的bean的class类型，以及制定需要让框架做反射获取实例对象
    protected ItemidGroupingComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean abean = (OrderBean) a;
        OrderBean bbean = (OrderBean) b;

        //比较两个bean时，指定只比较bean中的orderid
        return abean.getItemid().compareTo(bbean.getItemid());
    }
}
