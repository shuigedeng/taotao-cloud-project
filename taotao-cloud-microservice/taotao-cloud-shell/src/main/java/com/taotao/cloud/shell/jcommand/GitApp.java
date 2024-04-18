package com.taotao.cloud.shell.jcommand;

import com.beust.jcommander.JCommander;

public class GitApp {

    public static void main(String[] args) {
        //args = new String[] {"clone", "ht2tp://www.wdbyte.com/test.git"};
        GitCommandOptions gitCommandOptions = new GitCommandOptions();
        GitCommandCommit commandCommit = new GitCommandCommit();
        GitCommandAdd commandAdd = new GitCommandAdd();
        JCommander commander = JCommander.newBuilder()
            .programName("git-app")
            .addObject(gitCommandOptions)
            .addCommand(commandCommit)
            .addCommand(commandAdd)
            .build();
        commander.parse(args);
        // 打印帮助信息
        if (gitCommandOptions.isHelp()) {
            commander.usage();
            return;
        }
        if (gitCommandOptions.isVersion()) {
            //LogUtils.info("git version 2.24.3 (Apple Git-128)");
            return;
        }
        if (gitCommandOptions.getCloneUrl() != null) {
			//LogUtils.info("clone " + gitCommandOptions.getCloneUrl());
        }
        String parsedCommand = commander.getParsedCommand();
        if ("commit".equals(parsedCommand)) {
			// LogUtils.info(commandCommit.getComment());
        }
        if ("add".equals(parsedCommand)) {
            for (String file : commandAdd.getFiles()) {
				//LogUtils.info("暂存文件：" + file);
            }
        }
    }
}
