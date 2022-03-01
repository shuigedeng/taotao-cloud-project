package com.taotao.cloud.sys.biz.tools.database.dtos;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExportProcessDto {
    private String relativePath;
    private boolean finish;
    private String percent;

    public ExportProcessDto() {
    }

    public ExportProcessDto(String relativePath, String percent) {
        this.relativePath = relativePath;
        this.percent = percent;
    }

    public ExportProcessDto(String relativePath, float current , float total) {
        this.relativePath = relativePath;
        BigDecimal bigDecimal = new BigDecimal(current).divide(new BigDecimal(total)).setScale(2, RoundingMode.HALF_DOWN);
        this.percent = bigDecimal.toString();
        float diff = 1e-6f;
        if (Math.abs(current - total) < diff){
            this.finish = true;
        }
    }

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}
}
