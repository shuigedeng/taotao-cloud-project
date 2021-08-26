package com.taotao.cloud.opencv.biz.common.utils;

import org.opencv.core.Rect;

public class RectComp implements Comparable<Object> {
	private Rect rm;

	public Rect getRm() {
		return rm;
	}

	public void setRm(Rect rm) {
		this.rm = rm;
	}

	public RectComp() {
		super();
	}

	public RectComp(Rect rm) {
		super();
		this.rm = rm;
	}

	// @Override
	// public int compareTo(Object object) {
	// if(this == object){
	// return 0;
	// } else if (object != null && object instanceof RectComp) {
	// RectComp rect = (RectComp) object;
	// if (rm.x <= rect.rm.x) {
	// return -1;
	// }else{
	// return 1;
	// }
	// }else{
	// return -1;
	// }
	// }
	@Override
	// 按面积排序，最大的放第一个
	public int compareTo(Object object) {
		if(this == object){
			return 0;
		} else if (object != null && object instanceof RectComp) {
			RectComp rect = (RectComp) object;
			if (rm.area() >= rect.rm.area()) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return -1;
		}
	}

}
