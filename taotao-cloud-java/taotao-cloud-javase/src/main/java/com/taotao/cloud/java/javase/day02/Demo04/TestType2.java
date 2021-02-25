package com.taotao.cloud.java.javase.day02.Demo04;

/**
 基本数据类型（小数/浮点数）
 */
public class TestType2{
	public static void main(String[] args){

		//float 单精度浮点型、double 双精度浮点型


		double d = 1.2;

		System.out.println(d);


		double d2 = 1;

		System.out.println(d2);


		float f = 1.5F; //任何“小数字面值”默认类型都是double，如果要存储到float变量中，需显示追加“F”

		System.out.println(f);

		//科学计数法
		double d3 = 2E3; // 2 * 10 ^ 3
		System.out.println(d3);

		double d4 = 2E7; // 2 * 10 ^ 7   20000000.0
		System.out.println(d4);

		float f2 = 5E4F; // 5 * 10 ^ 4 追加F代表float
		System.out.println(f2);


		//取值范围的差异

		//float正数取值范围：0.0000000000000000000000000000000000000000000014F ~ 340000000000000000000000000000000000000.0F

		float floatMin = 0.0000000000000000000000000000000000000000000014F; //float的最小正数

		float floatMax = 340000000000000000000000000000000000000.0F; //float的最大正数

		System.out.println(floatMin);

		System.out.println(floatMax);


		//float负数取值范围：-340000000000000000000000000000000000000.0F ~ -0.0000000000000000000000000000000000000000000014F

		float floatMin2 = -340000000000000000000000000000000000000.0F;//个十百千万亿兆京垓。。。。。那由他

		float floatMax2 = -0.0000000000000000000000000000000000000000000014F;

		System.out.println(floatMin2);

		System.out.println(floatMax2);


		//double正数取值范围：
		double doubleMin = 4.9E-324;//0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049;
		double doubleMax = 1.7E308;//1700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0;

		System.out.println(doubleMin);
		System.out.println(doubleMax);

	}
}
