package com.taotao.cloud.shell.commond;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * 为命令分组
 */
@ShellComponent
@ShellCommandGroup("分组的命令")
public class GroupCommands {

    @ShellMethod("命令1")
    public void download1() {
    }

    @ShellMethod("命令2")
    public void download2() {
    }

    @ShellMethod("命令3")
    public void download3() {
    }


    @ShellMethod(value = "命令4",group = "其他组")
    public void download4() {
    }

}
