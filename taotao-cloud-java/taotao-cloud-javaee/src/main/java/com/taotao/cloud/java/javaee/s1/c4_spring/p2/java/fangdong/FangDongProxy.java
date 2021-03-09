package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.fangdong;

// 静态代理类
public class FangDongProxy  implements FangDongService{

    private FangDongService fangDongService = new FangDongServiceImpl();
    @Override
    public void zufang() {

        // 辅助功能、额外功能
        System.out.println("发布租房信息");
        System.out.println("带租客看房");
        //核心= 原始业务类
        fangDongService.zufang();
        // 辅助功能、额外功能
        System.out.println("发布租房信息");
        System.out.println("带租客看房");

    }
}
