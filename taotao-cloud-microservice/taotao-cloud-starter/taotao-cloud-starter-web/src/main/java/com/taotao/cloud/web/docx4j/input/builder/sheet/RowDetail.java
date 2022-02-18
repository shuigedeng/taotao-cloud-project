package com.taotao.cloud.web.docx4j.input.builder.sheet;

import java.util.Objects;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 行错误信息及原始信息
 */
public class RowDetail extends RowMessage {
    /**
     * 原始数据 <列索引, 列原始值>
     */
    private Map<Integer, String> data;

    /**
     * 从0开始列单元格值
     * @param to 索引闭区间
     * @return 单元格值列表
     */
    public List<String> cellValues(int to) {
        return this.cellValues(0, to);
    }

    /**
     * 从from开始to截止单元格值
     * @param from 索引开区间
     * @param to   索引闭区间
     * @return 单元格值列表
     */
    public List<String> cellValues(int from, int to) {
        return
            IntStream.rangeClosed(from, to)
                .mapToObj(this.data::get)
                .collect(Collectors.toList());
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RowDetail rowDetail = (RowDetail) o;
		return Objects.equals(data, rowDetail.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(data);
	}

	public Map<Integer, String> getData() {
		return data;
	}

	public void setData(Map<Integer, String> data) {
		this.data = data;
	}
}
