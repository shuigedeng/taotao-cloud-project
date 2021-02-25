package com.taotao.cloud.spring.source.spring_annotation.aop;

public class MathCalculator {
	public int div(int i, int j) {
		System.out.println("MathCalculator...div...");
		return i / j;
	}
}
