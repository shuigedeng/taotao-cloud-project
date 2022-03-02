package com.taotao.cloud.sys.api.dto.kafka;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MBeanMonitorInfo {
    private BigDecimal fifteenMinute;
    private BigDecimal fiveMinute;
    private BigDecimal meanRate;
    private BigDecimal oneMinute;
    private String mBean;

    public MBeanMonitorInfo() {
    }

    public MBeanMonitorInfo( String mBean,BigDecimal fifteenMinute, BigDecimal fiveMinute, BigDecimal meanRate, BigDecimal oneMinute) {
        this.fifteenMinute = fifteenMinute;
        this.fiveMinute = fiveMinute;
        this.meanRate = meanRate;
        this.oneMinute = oneMinute;
        this.mBean = mBean;
    }

    public String getFifteenMinute() {
        return fifteenMinute.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getFiveMinute() {
        return fiveMinute.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getMeanRate() {
        return meanRate.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getOneMinute() {
        return oneMinute.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getmBean() {
        return mBean;
    }

    /**
     * 合并多个 broker 的数据
     * @param mBeanInfo
     */
    public void addData(MBeanMonitorInfo mBeanInfo) {
        this.fifteenMinute.add(mBeanInfo.fifteenMinute);
        this.fiveMinute.add(mBeanInfo.fiveMinute);
        this.meanRate.add(mBeanInfo.meanRate);
        this.oneMinute.add(mBeanInfo.oneMinute);
    }

	public void setFifteenMinute(BigDecimal fifteenMinute) {
		this.fifteenMinute = fifteenMinute;
	}

	public void setFiveMinute(BigDecimal fiveMinute) {
		this.fiveMinute = fiveMinute;
	}

	public void setMeanRate(BigDecimal meanRate) {
		this.meanRate = meanRate;
	}

	public void setOneMinute(BigDecimal oneMinute) {
		this.oneMinute = oneMinute;
	}

	public void setmBean(String mBean) {
		this.mBean = mBean;
	}
}
