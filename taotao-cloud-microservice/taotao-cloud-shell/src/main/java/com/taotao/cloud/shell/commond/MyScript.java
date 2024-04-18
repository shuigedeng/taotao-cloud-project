package com.taotao.cloud.shell.commond;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Script;

// 实现接口org.springframework.shell.standard.commands.Script.Command
@ShellComponent
public class MyScript implements Script.Command {
	// 注意：命令名称与内置命令保持一致
	@ShellMethod("Read and execute commands from a file.")
	public void script() {
		// 实现自定义逻辑
		//LogUtils.info("override default script command");
	}

	// 指定被覆盖的内置命令分组为“Built-In Commands”
	// @ShellMethod(value = "Read and execute commands from a file.", group = "Built-In Commands")
	// public void script() {
	// 	LogUtils.info("override default script command");
	// }

}
