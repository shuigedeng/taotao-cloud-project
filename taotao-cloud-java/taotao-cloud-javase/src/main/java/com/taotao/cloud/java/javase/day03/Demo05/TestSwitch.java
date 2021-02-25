package com.taotao.cloud.java.javase.day03.Demo05;

public class TestSwitch{

	public static void main(String[] args){
		
		/*
			每日食谱

			星期一：湘菜
			星期二：川菜
			星期三：粤菜
			星期四：浙菜
			星期五：川菜
			星期六：川菜
			星期日：徽菜

		*/

		int weekDay = 8;

		switch( weekDay ){ //根据weekDay的值，找到匹配的case，并执行逻辑代码
			default:
				System.out.println("请输入1~7之间的整数!");
				break;
			case 1:
				System.out.println("湘菜");
				break;
			case 3:
				System.out.println("粤菜");
				break;
			case 4:
				System.out.println("浙菜");
				break;
			case 2:
			case 5:
			case 6:
				System.out.println("川菜");
				break;
			case 7:
				System.out.println("徽菜");
				break;
		}

		System.out.println("程序结束...");

	}
}
