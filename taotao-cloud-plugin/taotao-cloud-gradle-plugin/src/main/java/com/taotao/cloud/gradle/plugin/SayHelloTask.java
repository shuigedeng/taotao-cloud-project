package com.taotao.cloud.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class SayHelloTask extends DefaultTask {

	@TaskAction
	public void hello() {
		System.out.println("Hello, World!");
	}
}
