package com.taotao.cloud.sys.biz.tools.database.dtos;


/**
 * 数据预览查询结果
 */
public class ExportPreviewDto {
    private DynamicQueryDto body;
    private long total;

    public ExportPreviewDto() {
    }

    public ExportPreviewDto(DynamicQueryDto body, long total) {
        this.body = body;
        this.total = total;
    }

	public DynamicQueryDto getBody() {
		return body;
	}

	public void setBody(DynamicQueryDto body) {
		this.body = body;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
