package com.taotao.cloud.shell.commond;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * 基础命令
 */
@ShellComponent
public class BaseCommands {

    /**
     * 基础的命令
     * @param a
     * @param b
     * 输入：add 2 3
     * 输出：5
     * @return
     */
    @ShellMethod("输入两个整数，获取相加结果")
    public int add(int a, int b) {
        return a + b;
    }


    /**
     * 指定命令名称，此时不能通过add2访问命令
     * 默认情况下，方法名称即使命令名称，但是通过指定key值来表示命令的名称
     * 输入：sum 3 5
     * 输出：8
     * @return
     */
    @ShellMethod(value = "输入两个整数，获取相加结果", key = "sum")
    public int add2(int a, int b) {
        return a + b;
    }
}
