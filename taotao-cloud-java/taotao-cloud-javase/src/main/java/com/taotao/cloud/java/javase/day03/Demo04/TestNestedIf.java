package com.taotao.cloud.java.javase.day03.Demo04;

public class TestNestedIf{

	public static void main(String[] args){
		
		/*
			运动会百米赛跑
			用时10秒之内的人进入总决赛，否则淘汰
		*/

		int timer = 9;

		char sex = '男';

		if(timer <= 10){//外层条件

			//进入总决赛
			if(sex == '男'){//内层条件
				System.out.println("男子组决赛");
			}else{
				System.out.println("女子组决赛");
			}

		}else{
			System.out.println("淘汰");
		}

	}
}
