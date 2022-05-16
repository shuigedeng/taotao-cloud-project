package com.taotao.cloud.elasticsearch.esearchx.model;

import java.io.Serializable;
import java.util.List;

/**
 * ElasticSearch 数据块
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:07:35
 */
public class EsData<T> implements Serializable {
    final long total;
    final List<T> list;
    final double maxScore;

    /**
     * 获取总记录数
     * */
    public long getTotal() {
        return total;
    }

    /**
     * 获取最大评分
     * */
    public double getMaxScore() {
        return maxScore;
    }

    /**
     * 获取列表
     * */
    public List<T> getList() {
        return list;
    }

    public int getListSize() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public EsData(long total, double maxScore, List<T> list) {
        this.total = total;
        this.maxScore = maxScore;
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", list=" + list +
                ", maxScore=" + maxScore +
                '}';
    }
}
