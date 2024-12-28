package com.taotao.cloud.job.common.module;

import com.taotao.cloud.job.common.utils.JsonUtils;
import lombok.Data;
import org.springframework.boot.json.JsonParser;

/**
 * @author shuigedeng
 * @since 2022/3/22
 */
public class LifeCycle {

    public static final LifeCycle EMPTY_LIFE_CYCLE = new LifeCycle();

    private Long start;

    private Long end;


    public static LifeCycle parse(String lifeCycle){
        try {
            return JsonUtils.parseObject(lifeCycle,LifeCycle.class);
        }catch (Exception e){
            // ignore
            return EMPTY_LIFE_CYCLE;
        }
    }

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}
}
