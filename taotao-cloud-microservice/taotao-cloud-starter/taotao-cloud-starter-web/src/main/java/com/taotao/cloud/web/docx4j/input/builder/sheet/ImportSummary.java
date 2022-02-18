package com.taotao.cloud.web.docx4j.input.builder.sheet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.ss.formula.functions.T;

/**
 * 导入汇总
 */
public class ImportSummary implements Serializable {
    /**
     * 总记录数
     */
    private int total;
    /**
     * 有效记录数
     */
    private int valid;
    /**
     * 无效记录数
     */
    private int invalid;
    /**
     * 非法记录
     */
    private final List<RowMessage> invalidRecords = new ArrayList<>();
public ImportSummary(){}
    <T> ImportSummary(ImportResult<T> result, String separator) {
        this.valid = result.getValidRecords().size();
        this.invalid = result.getInvalidRecordMessage().size();
        this.total = this.valid + this.invalid;
        result.getInvalidRecordMessage()
            .forEach((key, value) ->
                this.invalidRecords.add(
                    new RowMessage(
                        key + 1,
                        // 错误消息拼接
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
                    )
                )
            );
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

	public List<RowMessage> getInvalidRecords() {
		return invalidRecords;
	}
}
