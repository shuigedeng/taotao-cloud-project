package com.taotao.cloud.seckill.biz.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPackage {
    public static void main(String[] args) {
        List<Integer> amountList = RedPackage(10000, 5);
        for (Integer amount : amountList) {
            System.out.println("搶到金額：" + new BigDecimal(amount).divide(new BigDecimal(100)) + "元");
        }
    }

    public static List<Integer> RedPackage(int TotalAmount, Integer TotalPeople) {
        List<Integer> amountList = new ArrayList<>();
        Integer restAmount = TotalAmount;//剩餘金額
        Integer restPeople = TotalPeople;//剩餘人數
        Random random = new Random();
        for (int i = 0; i < TotalPeople - 1; i++) {
            System.out.println(restPeople);
            //隨機範圍：[1,剩餘人均金額的兩倍]
            int amount = random.nextInt(restAmount / restPeople * 2 - 1) + 1;
            restAmount -= amount;
            restPeople--;
            amountList.add(amount);
        }
        amountList.add(restAmount);
        return amountList;
    }
}
