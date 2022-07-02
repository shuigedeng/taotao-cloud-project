package com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 导入结果详情
 */
public class ImportDetail<T> implements Serializable {
    /**
     * 总记录数
     */
    private int total;
    /**
     * 有效记录数
     */
    private  int valid;
    /**
     * 无效记录数
     */
    private int invalid;
    /**
     * 错误数据行信息
     */
    private final List<RowDetail> invalidRecords = new ArrayList<>();
public ImportDetail(){}
    ImportDetail(ImportResult<T> result, String separator) {
        this.valid = result.getValidRecords().size();
        this.invalid = result.getInvalidRecordMessage().size();
        this.total = this.valid + this.invalid;
        // 使用错误消息保证顺序
        result.getInvalidRecordMessage()
            .forEach((key, value) -> {
                RowDetail detail = new RowDetail();
                detail.setRow(key);
                // 记录原始数据
                detail.setData(result.getOrigin().get(key));

                // 错误消息拼接
                detail.setMessage(
                    Optional.of(value)
                        .filter(it -> it.size() > 1)
                        // 一行数据存在多个错误
                        .map(it ->
                            IntStream.range(0, it.size())
                                .mapToObj(row -> String.format("%d.%s", row + 1, it.get(row)))
                                .collect(Collectors.joining(separator))
                        )
                        // 仅存在一个错误
                        .orElseGet(() -> value.get(0))
                );

                this.invalidRecords.add(detail);
            });
    }

    /**
     * 清空导入明细结果
     * @return 清空前数据
     */
    public List<RowDetail> clear() {
        List<RowDetail> copy = new ArrayList<>(this.invalidRecords);
        this.invalidRecords.clear();
        return copy;
    }

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public List<RowDetail> getInvalidRecords() {
		return invalidRecords;
	}
}
